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
package org.tinygroup.springmvc.coc;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UrlPathHelper;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.springmvc.coc.impl.ConventionHandlerMethodResolver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author renhui
 */
public class ConventionHelper {
    public static final String CONVENTION_RESOURCE_NAME = ConventionHelper.class
            .getName()
            + "_RESOURCE_NAME";
    private final Set<String> CONVENTIONAL_URLS = new HashSet<String>();
    private PathMatcher pathMatcher = new AntPathMatcher();
    private UrlPathHelper urlPathHelper = new UrlPathHelper();
    private Map<Class<?>, CustomHandlerMethodResolver> conventionHandlerMethodResolverRegistry = new HashMap<Class<?>, CustomHandlerMethodResolver>();
    private ConventionComponentIdentifier conventionComponentIdentifier;

    public void setConventionComponentIdentifier(ConventionComponentIdentifier conventionComponentIdentifier) {
        this.conventionComponentIdentifier = conventionComponentIdentifier;
    }

    protected CustomHandlerMethodResolver newCustomHandlerMethodResolver(Class<?> handlerType) {
        return new ConventionHandlerMethodResolver(handlerType, this);
    }

    public boolean isConventional(String url) {
        return CONVENTIONAL_URLS.contains(url);
    }

    public PathMatcher getPathMatcher() {
        return pathMatcher;
    }

    public void setPathMatcher(PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
    }

    public UrlPathHelper getUrlPathHelper() {
        return urlPathHelper;
    }

    public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
        this.urlPathHelper = urlPathHelper;
    }

    public boolean isHandler(Object obj) {
        return conventionComponentIdentifier.isComponent(obj.getClass().getName());
    }


    // with no @requestMapping typelevel
    public String[] determineUrl(Object handler) {
        // already check. return null;
        CustomHandlerMethodResolver conventionHandlerMethodResolver = conventionHandlerMethodResolverRegistry
                .get(handler.getClass());
        if (conventionHandlerMethodResolver != null) {
            return null;
        }

        conventionHandlerMethodResolver = newCustomHandlerMethodResolver(handler.getClass());
        Set<String> urlSet = conventionHandlerMethodResolver.resolve();
        if (!CollectionUtil.isEmpty(urlSet)) {
            // set to cache
            conventionHandlerMethodResolverRegistry.put(handler.getClass(),
                    conventionHandlerMethodResolver);

            CONVENTIONAL_URLS.addAll(urlSet);
            String[] urls = new String[urlSet.size()];
            urlSet.toArray(urls);
            return urls;
        }
        return null;
    }

    public CustomHandlerMethodResolver getConventionHandlerMethodResolver(Class<?> handlerType) {
        return conventionHandlerMethodResolverRegistry.get(handlerType);
    }

    public String getHandlerName(Class<?> handlerType) {
        String sn = handlerType.getSimpleName();
        sn = StringUtils.uncapitalize(sn);
        int idx = sn.indexOf(StringUtils.capitalize(getHandlerStyle()));
        return sn.substring(0, idx);
    }

    public String getHandlerName(Object handler) {
        return getHandlerName(handler.getClass());
    }


    public String getHandlerStyle() {
        return "controller";
    }

}
