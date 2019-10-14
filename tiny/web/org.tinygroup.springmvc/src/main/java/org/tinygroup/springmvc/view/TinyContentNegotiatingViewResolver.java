/**
 * Copyright (c) 2012-2017, www.tinygroup.org (luo_guo@icloud.com).
 * <p>
 * Licensed under the GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/gpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tinygroup.springmvc.view;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.support.WebApplicationObjectSupport;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.RedirectView;
import org.tinygroup.assembly.AssemblyService;
import org.tinygroup.assembly.DefaultAssemblyService;
import org.tinygroup.springmvc.negotiation.ContentNegotiationManager;
import org.tinygroup.springmvc.negotiation.ContentNegotiationManagerFactoryBean;
import org.tinygroup.springmvc.util.MediaTypeUtil;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * tiny模式的内容协商视图解析
 *
 * @author renhui
 */
public class TinyContentNegotiatingViewResolver extends
        WebApplicationObjectSupport implements ViewResolver, Ordered,
        InitializingBean {

    public static final String PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE = HandlerMapping.class
            .getName() + ".producibleMediaTypes";
    /**
     * The {@link MediaType} selected during content negotiation, which may be
     * more specific than the one the View is configured with. For example:
     * "application/vnd.example-v1+xml" vs "application/*+xml".
     */
    public static final String SELECTED_CONTENT_TYPE = View.class.getName()
            + ".selectedContentType";
    private static final View NOT_ACCEPTABLE_VIEW = new View() {

        public String getContentType() {
            return null;
        }

        public void render(Map<String, ?> model, HttpServletRequest request,
                           HttpServletResponse response) {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }
    };
    private final ContentNegotiationManagerFactoryBean cnmFactoryBean = new ContentNegotiationManagerFactoryBean();
    private int order = Ordered.HIGHEST_PRECEDENCE;
    private ContentNegotiationManager contentNegotiationManager;
    private boolean useNotAcceptableStatusCode = false;
    private List<View> defaultViews;
    private List<ViewResolver> viewResolvers;
    private AssemblyService<ViewResolver> assemblyService = new DefaultAssemblyService<ViewResolver>();
    private Map<String, String> mediaTypes = new ConcurrentHashMap<String, String>();

    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public ContentNegotiationManager getContentNegotiationManager() {
        return this.contentNegotiationManager;
    }

    public void setContentNegotiationManager(
            ContentNegotiationManager contentNegotiationManager) {
        this.contentNegotiationManager = contentNegotiationManager;
    }

    public boolean isUseNotAcceptableStatusCode() {
        return this.useNotAcceptableStatusCode;
    }

    public void setUseNotAcceptableStatusCode(boolean useNotAcceptableStatusCode) {
        this.useNotAcceptableStatusCode = useNotAcceptableStatusCode;
    }

    public List<View> getDefaultViews() {
        return Collections.unmodifiableList(this.defaultViews);
    }

    /**
     * Set the default views to use when a more specific view can not be
     * obtained from the {@link ViewResolver} chain.
     */
    public void setDefaultViews(List<View> defaultViews) {
        this.defaultViews = defaultViews;
    }

    public void setAssemblyService(AssemblyService<ViewResolver> assemblyService) {
        this.assemblyService = assemblyService;
    }

    public List<ViewResolver> getViewResolvers() {
        return Collections.unmodifiableList(this.viewResolvers);
    }

    /**
     * Sets the view resolvers to be wrapped by this view resolver.
     * <p>
     * If this property is not set, view resolvers will be detected
     * automatically.
     */
    public void setViewResolvers(List<ViewResolver> viewResolvers) {
        this.viewResolvers = viewResolvers;
    }

    public void setMediaTypes(Map<String, String> mediaTypes) {
        this.mediaTypes = mediaTypes;
    }

    @Override
    protected void initServletContext(ServletContext servletContext) {
        List<ViewResolver> exclusions = new ArrayList<ViewResolver>();
        exclusions.add(this.getApplicationContext().getBean(
                TinyViewResolver.class));
        try {
            Map<String, ContentNegotiatingViewResolver> contentNegotiatingViewResolvers = BeanFactoryUtils
                    .beansOfTypeIncludingAncestors(
                            this.getApplicationContext(),
                            ContentNegotiatingViewResolver.class);
            if (!contentNegotiatingViewResolvers.isEmpty()) {
                exclusions.addAll(contentNegotiatingViewResolvers.values());
            }
        } catch (Exception e) {
        }
        exclusions.add(this);
        assemblyService.setApplicationContext(getApplicationContext());
        assemblyService.setExclusions(exclusions);
        Collection<ViewResolver> matchingBeans = assemblyService
                .findParticipants(ViewResolver.class);
        if (this.viewResolvers == null) {
            this.viewResolvers = new ArrayList<ViewResolver>(
                    matchingBeans.size());
            for (ViewResolver viewResolver : matchingBeans) {
                if (this != viewResolver) {
                    this.viewResolvers.add(viewResolver);
                }
            }
        } else {
            for (int i = 0; i < viewResolvers.size(); i++) {
                if (matchingBeans.contains(viewResolvers.get(i))) {
                    continue;
                }
                String name = viewResolvers.get(i).getClass().getName() + i;
                getApplicationContext().getAutowireCapableBeanFactory()
                        .initializeBean(viewResolvers.get(i), name);
            }
        }
        if (this.viewResolvers.isEmpty()) {
            logger.warn("Did not find any ViewResolvers to delegate to; please configure them using the "
                    + "'viewResolvers' property on the ContentNegotiatingViewResolver");
        }
        AnnotationAwareOrderComparator.sort(this.viewResolvers);
        if (this.defaultViews == null) {
            this.defaultViews = new ArrayList<View>();
        }
        try {
            List<View> defaultViews = new ArrayList<View>();
            Map<String, DefaultViewsStorage> defaultViewsStorages = BeanFactoryUtils
                    .beansOfTypeIncludingAncestors(
                            this.getApplicationContext(),
                            DefaultViewsStorage.class);
            if (!defaultViewsStorages.isEmpty()) {
                for (DefaultViewsStorage defaultViewsStorage : defaultViewsStorages
                        .values()) {
                    defaultViews.addAll(defaultViewsStorage.getDefaultViews());
                }
            }
            this.defaultViews.addAll(defaultViews);
        } catch (Exception e) {
        }
        cnmFactoryBean.setServletContext(servletContext);
        cnmFactoryBean.setFavorParameter(true);
        cnmFactoryBean.setUseJaf(true);
        cnmFactoryBean.setDefaultContentType(MediaType.TEXT_HTML);
        Properties mediaTypeProperties = new Properties();
        mediaTypeProperties.putAll(mediaTypes);
        try {
            Map<String, MediaTypeMapping> mappings = BeanFactoryUtils
                    .beansOfTypeIncludingAncestors(
                            this.getApplicationContext(),
                            MediaTypeMapping.class);
            if (!mappings.isEmpty()) {
                for (MediaTypeMapping mediaTypeMapping : mappings.values()) {
                    mediaTypeProperties
                            .putAll(mediaTypeMapping.getMediaTypes());
                }
            }
        } catch (Exception e) {
        }
        cnmFactoryBean.setMediaTypes(mediaTypeProperties);
    }

    public void afterPropertiesSet() {
        if (this.contentNegotiationManager == null) {
            this.cnmFactoryBean.afterPropertiesSet();
            this.contentNegotiationManager = this.cnmFactoryBean.getObject();
        }
    }

    public View resolveViewName(String viewName, Locale locale)
            throws Exception {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        Assert.isInstanceOf(ServletRequestAttributes.class, attrs);
        List<MediaType> requestedMediaTypes = getMediaTypes(((ServletRequestAttributes) attrs)
                .getRequest());
        if (requestedMediaTypes != null) {
            List<View> candidateViews = getCandidateViews(viewName, locale,
                    requestedMediaTypes);
            View bestView = getBestView(candidateViews, requestedMediaTypes,
                    attrs);
            if (bestView != null) {
                return bestView;
            }
        }
        if (this.useNotAcceptableStatusCode) {
            if (logger.isDebugEnabled()) {
                logger.debug("No acceptable view found; returning 406 (Not Acceptable) status code");
            }
            return NOT_ACCEPTABLE_VIEW;
        } else {
            logger.debug("No acceptable view found; returning null");
            return null;
        }
    }

    /**
     * Determines the list of {@link MediaType} for the given
     * {@link HttpServletRequest}.
     *
     * @param request the current servlet request
     * @return the list of media types requested, if any
     */
    protected List<MediaType> getMediaTypes(HttpServletRequest request) {
        try {
            ServletWebRequest webRequest = new ServletWebRequest(request);

            List<MediaType> acceptableMediaTypes = this.contentNegotiationManager
                    .resolveMediaTypes(webRequest);
            acceptableMediaTypes = (!acceptableMediaTypes.isEmpty() ? acceptableMediaTypes
                    : Collections.singletonList(MediaType.ALL));

            List<MediaType> producibleMediaTypes = getProducibleMediaTypes(request);
            Set<MediaType> compatibleMediaTypes = new LinkedHashSet<MediaType>();
            for (MediaType acceptable : acceptableMediaTypes) {
                for (MediaType producible : producibleMediaTypes) {
                    if (acceptable.isCompatibleWith(producible)) {
                        compatibleMediaTypes.add(getMostSpecificMediaType(
                                acceptable, producible));
                    }
                }
            }
            List<MediaType> selectedMediaTypes = new ArrayList<MediaType>(
                    compatibleMediaTypes);
            MediaType.sortBySpecificity(selectedMediaTypes);
            MediaType.sortByQualityValue(selectedMediaTypes);
            if (logger.isDebugEnabled()) {
                logger.debug("Requested media types are " + selectedMediaTypes
                        + " based on Accept header types "
                        + "and producible media types " + producibleMediaTypes
                        + ")");
            }
            return selectedMediaTypes;
        } catch (HttpMediaTypeNotAcceptableException ex) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private List<MediaType> getProducibleMediaTypes(HttpServletRequest request) {
        Set<MediaType> mediaTypes = (Set<MediaType>) request
                .getAttribute(PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE);
        if (!CollectionUtils.isEmpty(mediaTypes)) {
            return new ArrayList<MediaType>(mediaTypes);
        } else {
            return Collections.singletonList(MediaType.ALL);
        }
    }

    /**
     * Return the more specific of the acceptable and the producible media types
     * with the q-value of the former.
     */
    private MediaType getMostSpecificMediaType(MediaType acceptType,
                                               MediaType produceType) {
        produceType = MediaTypeUtil.copyQualityValue(acceptType, produceType);
        List<MediaType> mediaTypes = Arrays.asList(acceptType, produceType);
        MediaType.sortBySpecificity(mediaTypes);
        return mediaTypes.get(0);
    }

    private List<View> getCandidateViews(String viewName, Locale locale,
                                         List<MediaType> requestedMediaTypes) throws Exception {

        List<View> candidateViews = new ArrayList<View>();
        for (ViewResolver viewResolver : this.viewResolvers) {
            View view = viewResolver.resolveViewName(viewName, locale);
            if (view != null) {
                candidateViews.add(view);
            }
            for (MediaType requestedMediaType : requestedMediaTypes) {
                List<String> extensions = this.contentNegotiationManager
                        .resolveFileExtensions(requestedMediaType);
                for (String extension : extensions) {
                    String viewNameWithExtension = viewName + "." + extension;
                    view = viewResolver.resolveViewName(viewNameWithExtension,
                            locale);
                    if (view != null) {
                        candidateViews.add(view);
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(this.defaultViews)) {
            candidateViews.addAll(this.defaultViews);
        }
        return candidateViews;
    }

    private View getBestView(List<View> candidateViews,
                             List<MediaType> requestedMediaTypes, RequestAttributes attrs) {
        for (View candidateView : candidateViews) {
            if (candidateView instanceof RedirectView) {
                RedirectView smartView = (RedirectView) candidateView;
                if (logger.isDebugEnabled()) {
                    logger.debug("Returning redirect view [" + candidateView
                            + "]");
                }
                return smartView;
            }
        }
        for (MediaType mediaType : requestedMediaTypes) {
            for (View candidateView : candidateViews) {
                if (StringUtils.hasText(candidateView.getContentType())) {
                    MediaType candidateContentType = MediaType
                            .parseMediaType(candidateView.getContentType());
                    if (mediaType.isCompatibleWith(candidateContentType)) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Returning [" + candidateView
                                    + "] based on requested media type '"
                                    + mediaType + "'");
                        }
                        attrs.setAttribute(SELECTED_CONTENT_TYPE, mediaType,
                                RequestAttributes.SCOPE_REQUEST);
                        return candidateView;
                    }
                }
            }
        }
        return null;
    }

}
