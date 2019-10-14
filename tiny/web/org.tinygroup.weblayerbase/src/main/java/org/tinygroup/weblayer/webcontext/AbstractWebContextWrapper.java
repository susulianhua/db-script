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
package org.tinygroup.weblayer.webcontext;

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.weblayer.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static org.tinygroup.commons.tools.Assert.assertNotNull;
import static org.tinygroup.weblayer.webcontext.WebLayerConstant.*;

/**
 * 功能说明:抽象的包装上下文,类属性值都是从被包装的上下文对象中获得
 * <p/>
 * <p>
 * 开发人员: renhui <br>
 * 开发时间: 2013-4-28 <br>
 * <br>
 */
public abstract class AbstractWebContextWrapper extends ContextImpl implements
        WebContext {
    private static final String ARRAY_EXTENDS = "_array";
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ServletContext servletContext;
    private WebContext wrappedContext;

    private Map<String, WebContextScope> scopes = new HashMap<String, WebContextScope>();
    private ParamNameVerificationSupport verificationSupport;

    {
        scopes.put("requestScope", new RequestScope());
        scopes.put("sessionScope", new SessionScope());
        scopes.put("applicationScope", new ApplicationScope());
    }

    public AbstractWebContextWrapper() {

    }

    public AbstractWebContextWrapper(WebContext wrappedContext) {
        assertNotNull(wrappedContext, "wrappedContext");
        this.wrappedContext = wrappedContext;
        this.request = wrappedContext.getRequest();
        this.response = wrappedContext.getResponse();
        this.servletContext = wrappedContext.getServletContext();
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
        super.put("httpServletResponse", response);
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
        super.put("httpServletRequest", request);
    }

    @SuppressWarnings("unchecked")
    private <T> T findInRequest(String name) {
        if (request != null && verificationSupport == null) {
            verificationSupport = new ParamNameVerificationSupport(request);
        }
        if (request != null) {
            return (T) verificationSupport.getValue(name);
        }
        return null;
    }

    private boolean existInRequest(String name) {
        if (request != null && verificationSupport == null) {
            verificationSupport = new ParamNameVerificationSupport(request);
        }
        if (request != null) {
            return verificationSupport.isExist(name);
        }
        return false;
    }

    /**
     * 改写get方法，使得可以从父环境中查找，同时，也可以从子环境中查找 先找自己，再找子，再找父
     */
    public <T> T get(String name) {
        T result = getFromWrapperContext(name, this);
        if (result != null) {
            return result;
        }
        return (T) findInRequest(name);
    }

    public boolean exist(String name) {
        boolean exist = existFromWrapperContext(name, this);
        if (exist) {
            return true;
        }
        return existInRequest(name);
    }

    protected <T> T getFromWrapperContext(String name, WebContext webContext) {
        T result = getFromSubContext(name, webContext);
        if (result != null) {
            return result;
        }
        if (webContext.getWrappedWebContext() != null) {
            result = getFromWrapperContext(name,
                    webContext.getWrappedWebContext());
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    protected boolean existFromWrapperContext(String name, WebContext webContext) {
        boolean exist = existFromSubContext(name, webContext);
        if (exist) {
            return true;
        }
        if (webContext.getWrappedWebContext() != null) {
            exist = existFromWrapperContext(name,
                    webContext.getWrappedWebContext());
            if (exist) {
                return true;
            }
        }
        return false;
    }

    private <T> T getFromSubContext(String name, Context context) {
        Map<Context, String> nodeMap = new HashMap<Context, String>();
        return (T) findNodeMap(name, context, nodeMap);
    }

    private boolean existFromSubContext(String name, Context context) {
        Map<Context, String> nodeMap = new HashMap<Context, String>();
        return existNodeMap(name, context, nodeMap);
    }

    public void init(HttpServletRequest request, HttpServletResponse response,
                     ServletContext servletContext) {
        setRequest(request);
        setResponse(response);
        setServletContext(servletContext);
    }

    public WebContext getWrappedWebContext() {
        return wrappedContext;
    }

    public WebContext getWrappedWebContext(String contextName) {
        return (WebContext) getSubContext(contextName);
    }

    public void putSubWebContext(String contextName, WebContext webContext) {
        putContext(contextName, webContext);

    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
        super.put("httpServletContext", servletContext);
    }

    public boolean isRequestFinished() {
        return false;
    }

    public void setObject(String scope, String key, Object value) {
        WebContextScope webContextScope = scopes.get(scope);
        if (webContextScope != null) {
            webContextScope.setObject(key, value);
            return;
        }
        throw new RuntimeException(String.format("找不到%s对应的请求范围", scope));
    }

    public Object getObject(String scope, String key) {
        WebContextScope webContextScope = scopes.get(scope);
        if (webContextScope != null) {
            return webContextScope.getObject(key);
        }
        throw new RuntimeException(String.format("找不到%s对应的请求范围", scope));
    }

    interface WebContextScope {
        String getScope();

        void setObject(String key, Object value);

        Object getObject(String key);
    }

    /**
     * 请求参数名称的验证
     *
     * @author renhui
     */
    interface ParamNameVerification {
        /**
         * 根据请求参数获取对应的值
         *
         * @param name
         * @return
         */
        Object getValue(String name);

        /**
         * 请求参数是否存在
         *
         * @param name
         * @return
         */
        boolean isExist(String name);
    }

    class RequestScope implements WebContextScope {
        private String scope = "requestScope";

        public String getScope() {
            return scope;
        }

        public void setObject(String key, Object value) {
            getRequest().setAttribute(key, value);
        }

        public Object getObject(String key) {
            return getRequest().getAttribute(key);
        }
    }

    class SessionScope implements WebContextScope {
        private String scope = "sessionScope";

        public String getScope() {
            return scope;
        }

        public void setObject(String key, Object value) {
            getRequest().getSession().setAttribute(key, value);
        }

        public Object getObject(String key) {
            return getRequest().getSession().getAttribute(key);
        }
    }

    class ApplicationScope implements WebContextScope {
        private String scope = "applicationScope";

        public String getScope() {
            return scope;
        }

        public void setObject(String key, Object value) {
            getServletContext().setAttribute(key, value);
        }

        public Object getObject(String key) {
            return getServletContext().getAttribute(key);
        }
    }

    class ParamNameVerificationSupport implements ParamNameVerification {

        List<ParamNameVerification> verifications = new ArrayList<AbstractWebContextWrapper.ParamNameVerification>();

        ParamNameVerificationSupport(HttpServletRequest request) {
            verifications.add(new RequestMethodInfoVerification(request));
            verifications
                    .add(new RequestAttributeParamNameVerification(request));
            verifications.add(new RequestParamNameVerification(request));
            verifications
                    .add(new SessionAttributeParamNameVerification(request));
            verifications.add(new CookieParamNameVerification(request));
            verifications.add(new HeaderNameVerification(request));
            verifications.add(new ApplicationAttributeParamNameVerification(
                    request));
        }

        public Object getValue(String name) {
            for (ParamNameVerification verification : verifications) {
                Object value = verification.getValue(name);
                if (value != null) {
                    return value;
                }
            }
            return null;
        }

        public boolean isExist(String name) {
            boolean exist = false;
            for (ParamNameVerification verification : verifications) {
                exist = verification.isExist(name);
                if (exist) {
                    break;
                }
            }
            return exist;
        }

    }

    class RequestMethodInfoVerification implements ParamNameVerification {

        private Map<String, Object> requestInfoMap = new HashMap<String, Object>();

        RequestMethodInfoVerification(HttpServletRequest request) {
            requestInfoMap
                    .put(REQUEST_REMOTE_ADDR, request.getRemoteAddr());
            requestInfoMap
                    .put(REQUEST_REMOTE_HOST, request.getRemoteHost());
            requestInfoMap
                    .put(REQUEST_REMOTE_USER, request.getRemoteUser());
            requestInfoMap.put(REQUEST_SCHEME, request.getScheme());
            requestInfoMap
                    .put(REQUEST_SERVER_NAME, request.getServerName());
            requestInfoMap.put(REQUEST_SERVLET_PATH,
                    request.getServletPath());
            requestInfoMap
                    .put(REQUEST_SERVER_PORT, request.getServerPort());
            requestInfoMap.put(REQUEST_AUTH_TYPE, request.getAuthType());
            requestInfoMap.put(REQUEST_ENCODING,
                    request.getCharacterEncoding());
            requestInfoMap.put(REQUEST_CONTENT_TYPE,
                    request.getContentType());
            requestInfoMap.put(REQUEST_CONTEXT_PATH,
                    request.getContextPath());
            requestInfoMap.put(REQUEST_LOCAL_ADDR, request.getLocalAddr());
            requestInfoMap.put(REQUEST_LOCAL_NAME, request.getLocalName());
            requestInfoMap.put(REQUEST_LOCAL_PORT, request.getLocalPort());
            requestInfoMap.put(REQUEST_PATH_INFO, request.getPathInfo());
            requestInfoMap.put(REQUEST_PATH_TRANSLATED,
                    request.getPathTranslated());
            requestInfoMap.put(REQUEST_PROTOCOL, request.getProtocol());
            requestInfoMap.put(REQUEST_QUERY_STRING,
                    request.getQueryString());
            requestInfoMap
                    .put(REQUEST_REQUEST_URI, request.getRequestURI());
        }

        public Object getValue(String name) {
            return requestInfoMap.get(name);
        }

        public boolean isExist(String name) {
            return requestInfoMap.containsKey(name);
        }
    }

    class RequestAttributeParamNameVerification implements
            ParamNameVerification {
        HttpServletRequest request;

        RequestAttributeParamNameVerification(HttpServletRequest request) {
            this.request = request;
        }

        public Object getValue(String name) {
            return request.getAttribute(name);
        }

        public boolean isExist(String name) {
            Enumeration enumeration = request.getAttributeNames();
            while (enumeration.hasMoreElements()) {
                if (enumeration.nextElement().equals(name)) {
                    return true;
                }
            }
            return false;
        }
    }

    class RequestParamNameVerification implements ParamNameVerification {
        HttpServletRequest request;

        RequestParamNameVerification(HttpServletRequest request) {
            this.request = request;
        }

        public Object getValue(String name) {
            String[] values = request.getParameterValues(name);
            if (values == null) {
                values = request.getParameterValues(name + ARRAY_EXTENDS);
            } else if (values != null && values.length == 1) {
                return values[0];
            }
            return values;
        }

        public boolean isExist(String name) {
            Map parameterMap = request.getParameterMap();
            return (parameterMap.containsKey(name) || parameterMap
                    .containsKey(name + ARRAY_EXTENDS)) ? true : false;
        }
    }

    class SessionAttributeParamNameVerification implements
            ParamNameVerification {
        HttpServletRequest request;

        SessionAttributeParamNameVerification(HttpServletRequest request) {
            this.request = request;
        }

        public Object getValue(String name) {
            return request.getSession().getAttribute(name);
        }

        public boolean isExist(String name) {
            Enumeration enumeration = request.getSession().getAttributeNames();
            while (enumeration.hasMoreElements()) {
                if (enumeration.nextElement().equals(name)) {
                    return true;
                }
            }
            return false;
        }
    }

    class ApplicationAttributeParamNameVerification implements
            ParamNameVerification {
        HttpServletRequest request;

        ApplicationAttributeParamNameVerification(HttpServletRequest request) {
            this.request = request;
        }

        public Object getValue(String name) {
            return request.getSession().getServletContext().getAttribute(name);
        }

        public boolean isExist(String name) {
            Enumeration enumeration = request.getSession().getServletContext()
                    .getAttributeNames();
            while (enumeration.hasMoreElements()) {
                if (enumeration.nextElement().equals(name)) {
                    return true;
                }
            }
            return false;
        }
    }

    class CookieParamNameVerification implements ParamNameVerification {
        HttpServletRequest request;

        CookieParamNameVerification(HttpServletRequest request) {
            this.request = request;
        }

        public Object getValue(String name) {
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals(name)) {
                        return cookie.getValue();
                    }
                }
            }
            return null;
        }

        public boolean isExist(String name) {
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals(name)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    class HeaderNameVerification implements ParamNameVerification {
        HttpServletRequest request;

        HeaderNameVerification(HttpServletRequest request) {
            this.request = request;
        }

        public Object getValue(String name) {
            return request.getHeader(name);
        }

        public boolean isExist(String name) {
            Enumeration enumeration = request.getHeaderNames();
            while (enumeration.hasMoreElements()) {
                if (enumeration.nextElement().equals(name)) {
                    return true;
                }
            }
            return false;
        }
    }
}
