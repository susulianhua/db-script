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
package org.tinygroup.weblayer;

import org.apache.commons.lang.StringUtils;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.config.ConfigurationManager;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.fileresolver.FullContextFileRepository;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.weblayer.configmanager.TinyListenerConfigManager;
import org.tinygroup.weblayer.configmanager.TinyListenerConfigManagerHolder;
import org.tinygroup.weblayer.impl.WebContextImpl;
import org.tinygroup.weblayer.listener.ServletContextHolder;
import org.tinygroup.weblayer.webcontext.util.WebContextUtil;
import org.tinygroup.xmlparser.node.XmlNode;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class TinyHttpFilter implements Filter {
    public static final String DEFAULT_POST_DATA_KEY = "$_post_data_key";
    public static final String DEFAULT_POST_NODE_NAME = "$_post_node_name";
    public static final String DEFAULT_PAGE_KEY = "$_default_page";
    public static final String REQUEST_REMOTE_HOST_MDC_KEY = "req.remoteHost";
    public static final String REQUEST_USER_AGENT_MDC_KEY = "req.userAgent";
    public static final String REQUEST_REQUEST_URI = "req.requestURI";
    public static final String REQUEST_QUERY_STRING = "req.queryString";
    public static final String REQUEST_REQUEST_URL = "req.requestURL";
    public static final String REQUEST_X_FORWARDED_FOR = "req.xForwardedFor";
    public static final String REQUEST_ID = "req.id";
    public static final String REQUEST_REMOTE_ADDR = "req.remoteAddr";
    private static final String REQUEST_URI_WITH_QUERY_STRING = "req.requestURIWithQueryString";
    private static final String EXCLUDE_PATH = "excludePath";
    private static final String CAN_REPEAT_READ = "canRepeatRead";
    private static final String PASSTHRU_PATH = "passthruPath";
    private static final Logger logger = LoggerFactory
            .getLogger(TinyHttpFilter.class);
    private static final String POST_DATA_PROCESS = "post-data-process";

    private static final String DATA_MAPPING = "data-mapping";

    private static final String HOST_PATTERN = "host-pattern";

    private static final String URL_PATTERN = "url-pattern";

    private static final String POST_DATA_KEY = "post-data-key";
    private static String[] defaultFiles = {"index.page", "index.htm",
            "index.html", "index.jsp"};
    private TinyProcessorManager tinyProcessorManager;
    private TinyFilterManager tinyFilterManager;
    private List<Pattern> excludePatterns = new ArrayList<Pattern>();
    private List<Pattern> passthruPatterns = new ArrayList<Pattern>();
    private boolean repeatRead = true;
    private Map<String, String> hostPatternMapping = new HashMap<String, String>();
    private Map<String, String> postDataUrlPattern = new HashMap<String, String>();
    private String postDataKey;
    private FilterWrapper wrapper;
    private FullContextFileRepository fullContextFileRepository;

    public void destroy() {
        destroyTinyProcessors();
        destroyTinyFilters();
    }

    private void destroyTinyFilters() {
        tinyFilterManager.destoryTinyResources();
    }

    /**
     * 销毁tiny-processors
     */
    private void destroyTinyProcessors() {
        tinyProcessorManager.destoryTinyResources();
    }

    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            InputStreamRepeatRead inputStreamRepeatRead = new InputStreamRepeatRead(
                    repeatRead);
            request = inputStreamRepeatRead.requestWrapper(request);
            requestInitListener(request);
            putMDC(request, response);
            String servletPath = WebContextUtil.getServletPath(request);
            if (isExcluded(servletPath)) {
                logger.logMessage(LogLevel.DEBUG, "请求路径:<{}>,被拒绝", servletPath);
                filterChain.doFilter(request, response);
                return;
            }

            WebContext context = new WebContextImpl();
            context.put("springUtil", BeanContainerFactory
                    .getBeanContainer(getClass().getClassLoader()));
            postDataProcess(servletPath, request, context);
            context.putSubContext("applicationproperties", new ContextImpl(
                    ConfigurationUtil.getConfigurationManager()
                            .getConfiguration()));
            putRequestInfo(request, context);
            context.init(request, response,
                    ServletContextHolder.getServletContext());
            if (servletPath.endsWith("/")) {
                for (String defaultFile : defaultFiles) {
                    String tmpPath = servletPath + defaultFile;
                    FileObject fileObject = fullContextFileRepository
                            .getFileObject(tmpPath);
                    if (fileObject != null && fileObject.isExist()) {
                        servletPath = tmpPath;
                        request.setAttribute(DEFAULT_PAGE_KEY, servletPath);
                        break;
                    }
                }
            }
            FilterHandler handler = new TinyFilterHandler(servletPath,
                    filterChain, context, tinyFilterManager,
                    tinyProcessorManager, passthruPatterns);
            if (wrapper != null) {
                wrapper.filterWrapper(context, handler);
            } else {
                handler.tinyFilterProcessor(request, response);
            }
        } finally {
            requestDestroyListener(servletRequest);// 抛出异常也要保证执行
            clearMDC(request, response);
        }
    }

    private void clearMDC(HttpServletRequest request,
                          HttpServletResponse response) {
        LoggerFactory.removeThreadVariable(REQUEST_REQUEST_URI);
        LoggerFactory.removeThreadVariable(REQUEST_REQUEST_URL);
        LoggerFactory.removeThreadVariable(REQUEST_QUERY_STRING);
        LoggerFactory.removeThreadVariable(REQUEST_REMOTE_ADDR);
        LoggerFactory.removeThreadVariable(REQUEST_ID);
        LoggerFactory.removeThreadVariable(REQUEST_URI_WITH_QUERY_STRING);
        LoggerFactory.removeThreadVariable(REQUEST_REMOTE_HOST_MDC_KEY);
        LoggerFactory.removeThreadVariable(REQUEST_USER_AGENT_MDC_KEY);
        LoggerFactory.removeThreadVariable(REQUEST_X_FORWARDED_FOR);
    }

    private void putMDC(HttpServletRequest request, HttpServletResponse response) {
        LoggerFactory.putThreadVariable(REQUEST_REQUEST_URI,
                request.getRequestURI());
        StringBuffer requestURL = request.getRequestURL();
        if (requestURL != null) {
            LoggerFactory.putThreadVariable(REQUEST_REQUEST_URL,
                    requestURL.toString());
        }
        LoggerFactory.putThreadVariable(REQUEST_QUERY_STRING,
                StringUtils.defaultString(request.getQueryString()));
        LoggerFactory.putThreadVariable(REQUEST_URI_WITH_QUERY_STRING,
                request.getRequestURI()
                        + (request.getQueryString() == null ? "" : "?"
                        + request.getQueryString()));
        LoggerFactory.putThreadVariable(REQUEST_REMOTE_ADDR,
                StringUtils.defaultString(request.getRemoteAddr()));
        LoggerFactory.putThreadVariable(REQUEST_ID,
                StringUtils.remove(UUID.randomUUID().toString(), "-"));
        LoggerFactory.putThreadVariable(REQUEST_REMOTE_HOST_MDC_KEY,
                request.getRemoteHost());
        LoggerFactory.putThreadVariable(REQUEST_USER_AGENT_MDC_KEY,
                request.getHeader("User-Agent"));
        LoggerFactory.putThreadVariable(REQUEST_X_FORWARDED_FOR,
                request.getHeader("X-Forwarded-For"));
    }

    private void requestInitListener(ServletRequest servletRequest) {
        TinyListenerConfigManager configManager = TinyListenerConfigManagerHolder
                .getInstance();
        List<ServletRequestListener> listeners = configManager
                .getRequestListeners();
        ServletRequestEvent event = new ServletRequestEvent(
                ServletContextHolder.getServletContext(), servletRequest);
        for (ServletRequestListener listener : listeners) {
            logger.logMessage(LogLevel.DEBUG,
                    "ServletRequestListener:[{0}] will be requestInitialized",
                    listener);
            listener.requestInitialized(event);
            logger.logMessage(LogLevel.DEBUG,
                    "ServletRequestListener:[{0}] requestInitialized", listener);
        }
    }

    private void requestDestroyListener(ServletRequest servletRequest) {
        TinyListenerConfigManager configManager = TinyListenerConfigManagerHolder
                .getInstance();
        List<ServletRequestListener> listeners = configManager
                .getRequestListeners();
        ServletRequestEvent event = new ServletRequestEvent(
                ServletContextHolder.getServletContext(), servletRequest);
        for (ServletRequestListener listener : listeners) {
            logger.logMessage(LogLevel.DEBUG,
                    "ServletRequestListener:[{0}] will be requestDestroyed",
                    listener);
            listener.requestDestroyed(event);
            logger.logMessage(LogLevel.DEBUG,
                    "ServletRequestListener:[{0}] requestDestroyed", listener);
        }
    }

    private void postDataProcess(String servletPath,
                                 HttpServletRequest request, WebContext context) throws IOException {
        if (isPostMethod(request)) {
            String remoteHost = request.getRemoteHost();
            String remoteAddr = request.getRemoteAddr();
            for (String nodeName : hostPatternMapping.keySet()) {
                String hostPattern = hostPatternMapping.get(nodeName);
                String urlPattern = postDataUrlPattern.get(nodeName);
                if ((Pattern.matches(hostPattern, remoteHost) || Pattern
                        .matches(hostPattern, remoteAddr))
                        && Pattern.matches(urlPattern, servletPath)) {
                    context.put(postDataKey,
                            StreamUtil
                                    .readBytes(request.getInputStream(), true)
                                    .toByteArray());
                    break;
                }
            }
        }

    }

    private boolean isPostMethod(HttpServletRequest request) {
        return request.getMethod().equalsIgnoreCase("post");
    }

    private void initPostDataProcess() {
        ConfigurationManager appConfigManager = ConfigurationUtil
                .getConfigurationManager();
        XmlNode parserNode = appConfigManager.getApplicationConfiguration()
                .getSubNode(POST_DATA_PROCESS);
        if (parserNode != null) {
            postDataKey = StringUtil.defaultIfBlank(
                    parserNode.getAttribute(POST_DATA_KEY),
                    DEFAULT_POST_DATA_KEY);
            List<XmlNode> dataMapNode = parserNode.getSubNodes(DATA_MAPPING);
            if (!CollectionUtil.isEmpty(dataMapNode)) {
                for (int i = 0; i < dataMapNode.size(); i++) {
                    XmlNode xmlNode = dataMapNode.get(i);
                    String hostsPattern = xmlNode.getAttribute(HOST_PATTERN);
                    String urlPattern = xmlNode.getAttribute(URL_PATTERN);
                    String nodeName = xmlNode.getAttribute("name");
                    if (StringUtil.isBlank(nodeName)) {
                        nodeName = DEFAULT_POST_NODE_NAME + i;
                    }
                    if (StringUtil.isBlank(hostsPattern)) {
                        hostsPattern = ".*";
                    }
                    if (StringUtil.isBlank(urlPattern)) {
                        urlPattern = ".*";
                    }
                    hostPatternMapping.put(nodeName, hostsPattern);
                    postDataUrlPattern.put(nodeName, urlPattern);
                }
            }
        }
    }

    private void putRequestInfo(HttpServletRequest request, WebContext context) {
        String path = request.getContextPath();
        if (path == null) {
            path = "";
        }
        context.put(WebContextUtil.TINY_CONTEXT_PATH, path);
        context.put(WebContextUtil.TINY_REQUEST_URI, request.getRequestURI());
        String servletPath = request.getServletPath();
        if (servletPath == null || servletPath.length() == 0) {
            servletPath = request.getPathInfo();
        }
        context.put(WebContextUtil.TINY_SERVLET_PATH, servletPath);
    }

    /**
     * 请求是否被排除
     *
     * @param servletPath
     * @return
     */
    private boolean isExcluded(String servletPath) {
        for (Pattern pattern : excludePatterns) {
            if (pattern.matcher(servletPath).matches()) {
                return true;
            }
        }
        return false;
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        logger.logMessage(LogLevel.INFO, "filter初始化开始...");
        fullContextFileRepository = BeanContainerFactory.getBeanContainer(
                this.getClass().getClassLoader()).getBean(
                "fullContextFileRepository");
        String openStr = filterConfig.getInitParameter(CAN_REPEAT_READ);
        if (!StringUtil.isBlank(openStr)) {
            repeatRead = Boolean.parseBoolean(openStr);
        }
        initExcludePattern(filterConfig);

        initPassthruPattern(filterConfig);

        initTinyFilters();

        initTinyFilterWrapper();

        initTinyProcessors();

        initPostDataProcess();

        logger.logMessage(LogLevel.INFO, "filter初始化结束...");

    }

    private void initTinyFilterWrapper() {
        wrapper = tinyFilterManager.getFilterWrapper();
    }

    private void initExcludePattern(FilterConfig filterConfig) {
        excludePatterns.clear();// 先清空
        String excludePath = filterConfig.getInitParameter(EXCLUDE_PATH);
        if (excludePath != null) {
            String[] excludeArray = excludePath.split(",");
            for (String path : excludeArray) {
                excludePatterns.add(Pattern.compile(path));
            }
        }
    }

    private void initPassthruPattern(FilterConfig filterConfig) {
        passthruPatterns.clear();// 先清空
        String passthruPath = filterConfig.getInitParameter(PASSTHRU_PATH);
        if (passthruPath != null) {
            String[] excludeArray = passthruPath.split(",");
            for (String path : excludeArray) {
                passthruPatterns.add(Pattern.compile(path));
            }
        }
    }

    private void initTinyFilters() throws ServletException {
        tinyFilterManager = BeanContainerFactory.getBeanContainer(
                this.getClass().getClassLoader()).getBean(
                TinyFilterManager.TINY_FILTER_MANAGER);
        tinyFilterManager.initTinyResources();
    }

    /**
     * tiny-processors初始化
     *
     * @throws IOException
     * @throws ServletException
     */
    private void initTinyProcessors() throws ServletException {
        tinyProcessorManager = BeanContainerFactory.getBeanContainer(
                this.getClass().getClassLoader()).getBean(
                TinyProcessorManager.TINY_PROCESSOR_MANAGER);
        tinyProcessorManager.initTinyResources();
    }
}
