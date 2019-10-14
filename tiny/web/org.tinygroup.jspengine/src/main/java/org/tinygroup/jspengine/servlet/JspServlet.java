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
package org.tinygroup.jspengine.servlet;

// START PWC 6300204

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.jspengine.Constants;
import org.tinygroup.jspengine.EmbeddedServletOptions;
import org.tinygroup.jspengine.Options;
import org.tinygroup.jspengine.compiler.JspRuntimeContext;
import org.tinygroup.jspengine.compiler.Localizer;
import org.tinygroup.jspengine.org.apache.commons.logging.Log;
import org.tinygroup.jspengine.org.apache.commons.logging.LogFactory;
import org.tinygroup.jspengine.runtime.JspApplicationContextImpl;
import org.tinygroup.jspengine.runtime.ResourceInjector;
import org.tinygroup.weblayer.impl.TinyServletConfig;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.TagLibraryInfo;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

// END PWC 6300204
// START GlassFish 750
// END GlassFish 750
// START SJSWS 6232180
// END SJSWS 6232180
// START GlassFish 747
// END GlassFish 747
// START GlassFish 750
// END GlassFish 750

/**
 * The JSP engine (a.k.a Jasper).
 * <p>
 * The servlet container is responsible for providing a URLClassLoader for the
 * web application context Jasper is being used in. Jasper will try get the
 * Tomcat ServletContext attribute for its ServletContext class loader, if that
 * fails, it uses the parent class loader. In either case, it must be a
 * URLClassLoader.
 *
 * @author Anil K. Vijendran
 * @author Harish Prabandham
 * @author Remy Maucherat
 * @author Kin-man Chung
 * @author Glenn Nielsen
 */
public class JspServlet extends HttpServlet {

    // Logger
    private static Log log = LogFactory.getLog(JspServlet.class);

    private ServletContext context;
    private ServletConfig config;
    private Options options;
    private JspRuntimeContext rctxt;

    // START S1AS
    // jsp error count
    private int countErrors;
    private Object errorCountLk = new Object();
    // END S1AS

    // START SJSWS 6232180
    private String httpMethodsString = null;
    private HashSet httpMethodsSet = null;
    // END SJSWS 6232180

    // START GlassFish 750
    private ConcurrentHashMap<String, TagLibraryInfo> taglibs;
    private ConcurrentHashMap<String, URL> tagFileJarUrls;

    // END GlassFish 750

    /*
     * Initializes this JspServlet.
     */
    public void init(ServletConfig config) throws ServletException {

        if (!(config instanceof TinyServletConfig)) {
            JspServlet servlet = BeanContainerFactory.getBeanContainer(
                    this.getClass().getClassLoader()).getBean("jspServlet");
            TinyServletConfig tinyServletConfig = (TinyServletConfig) servlet.getServletConfig();
            if (tinyServletConfig == null) {
                tinyServletConfig = new TinyServletConfig();
            }
            tinyServletConfig.setServletConfig(config);
            servlet.setServletConfig(tinyServletConfig);
            config = tinyServletConfig;
        }
        this.config = config;
        this.context = config.getServletContext();

        // Initialize the JSP Runtime Context
        options = new EmbeddedServletOptions(config, context);
        rctxt = new JspRuntimeContext(context, options);

        // Instantiate and init our resource injector
        String resourceInjectorClassName = config
                .getInitParameter(Constants.JSP_RESOURCE_INJECTOR_CONTEXT_ATTRIBUTE);
        if (resourceInjectorClassName != null) {
            try {
                ResourceInjector ri = (ResourceInjector) Class.forName(
                        resourceInjectorClassName).newInstance();
                ri.setContext(this.context);
                this.context.setAttribute(
                        Constants.JSP_RESOURCE_INJECTOR_CONTEXT_ATTRIBUTE, ri);
            } catch (Exception e) {
                throw new ServletException(e);
            }
        }

        // START SJSWS 6232180
        // Determine which HTTP methods to service ("*" means all)
        httpMethodsString = config.getInitParameter("httpMethods");
        if (httpMethodsString != null && !httpMethodsString.equals("*")) {
            httpMethodsSet = new HashSet();
            StringTokenizer tokenizer = new StringTokenizer(httpMethodsString,
                    ", \t\n\r\f");
            while (tokenizer.hasMoreTokens()) {
                httpMethodsSet.add(tokenizer.nextToken());
            }
        }
        // END SJSWS 6232180

        // START GlassFish 750
        taglibs = new ConcurrentHashMap<String, TagLibraryInfo>();
        context.setAttribute(Constants.JSP_TAGLIBRARY_CACHE, taglibs);

        tagFileJarUrls = new ConcurrentHashMap<String, URL>();
        context.setAttribute(Constants.JSP_TAGFILE_JAR_URLS_CACHE,
                tagFileJarUrls);
        // END GlassFish 750

        if (log.isTraceEnabled()) {
            log.trace(Localizer.getMessage("jsp.message.scratch.dir.is",
                    options.getScratchDir().toString()));
            log.trace(Localizer.getMessage("jsp.message.dont.modify.servlets"));
        }
    }

    /**
     * Returns the number of JSPs for which JspServletWrappers exist, i.e., the
     * number of JSPs that have been loaded into the webapp with which this
     * JspServlet is associated.
     * <p>
     * <p>
     * This info may be used for monitoring purposes.
     *
     * @return The number of JSPs that have been loaded into the webapp with
     * which this JspServlet is associated
     */
    public int getJspCount() {
        return this.rctxt.getJspCount();
    }

    /**
     * Gets the number of JSPs that have been reloaded.
     * <p>
     * <p>
     * This info may be used for monitoring purposes.
     *
     * @return The number of JSPs (in the webapp with which this JspServlet is
     * associated) that have been reloaded
     */
    public int getJspReloadCount() {
        return this.rctxt.getJspReloadCount();
    }

    /**
     * Resets the JSP reload counter.
     *
     * @param count Value to which to reset the JSP reload counter
     */
    public void setJspReloadCount(int count) {
        this.rctxt.setJspReloadCount(count);
    }

    // START S1AS

    /**
     * Gets the number of errors triggered by JSP invocations.
     *
     * @return The number of errors triggered by JSP invocations
     */
    public int getJspErrorCount() {
        return this.countErrors;
    }

    // END S1AS

    /**
     * <p>
     * Look for a <em>precompilation request</em> as described in Section 8.4.2
     * of the JSP 1.2 Specification. <strong>WARNING</strong> - we cannot use
     * <code>request.getParameter()</code> for this, because that will trigger
     * parsing all of the request parameters, and not give a servlet the
     * opportunity to call <code>request.setCharacterEncoding()</code> first.
     * </p>
     *
     * @param request The servlet requset we are processing
     * @throws ServletException if an invalid parameter value for the
     *                          <code>jsp_precompile</code> parameter name is specified
     */
    boolean preCompile(HttpServletRequest request) throws ServletException {

        String queryString = request.getQueryString();
        if (queryString == null) {
            return (false);
        }
        int start = queryString.indexOf(Constants.PRECOMPILE);
        if (start < 0) {
            return (false);
        }
        queryString = queryString.substring(start
                + Constants.PRECOMPILE.length());
        if (queryString.length() == 0) {
            return (true); // ?jsp_precompile
        }
        if (queryString.startsWith("&")) {
            return (true); // ?jsp_precompile&foo=bar...
        }
        if (!queryString.startsWith("=")) {
            return (false); // part of some other name or value
        }
        int limit = queryString.length();
        int ampersand = queryString.indexOf("&");
        if (ampersand > 0) {
            limit = ampersand;
        }
        String value = queryString.substring(1, limit);
        if (value.equals("true")) {
            return (true); // ?jsp_precompile=true
        } else if (value.equals("false")) {
            // Spec says if jsp_precompile=false, the request should not
            // be delivered to the JSP page; the easiest way to implement
            // this is to set the flag to true, and precompile the page anyway.
            // This still conforms to the spec, since it says the
            // precompilation request can be ignored.
            return (true); // ?jsp_precompile=false
        } else {
            throw new ServletException("Cannot have request parameter "
                    + Constants.PRECOMPILE + " set to " + value);
        }

    }

    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // START SJSWS 6232180
        if (httpMethodsSet != null) {
            String method = request.getMethod();
            if (method == null) {
                return;
            }
            boolean isSupportedMethod = httpMethodsSet.contains(method);
            if (!isSupportedMethod) {
                if (method.equals("OPTIONS")) {
                    response.addHeader("Allow", httpMethodsString);
                } else {
                    super.service(request, response);
                }
                return;
            }
        }
        // END SJSWS 6232180

        String jspUri = null;

        String jspFile = (String) request.getAttribute(Constants.JSP_FILE);
        if (jspFile != null) {
            // JSP is specified via <jsp-file> in <servlet> declaration
            jspUri = jspFile;
            request.removeAttribute(Constants.JSP_FILE);
        } else {
            /*
             * Check to see if the requested JSP has been the target of a
             * RequestDispatcher.include()
             */
            jspUri = (String) request.getAttribute(Constants.INC_SERVLET_PATH);
            if (jspUri != null) {
                /*
                 * Requested JSP has been target of RequestDispatcher.include().
                 * Its path is assembled from the relevant
                 * javax.servlet.include.* request attributes
                 */
                String pathInfo = (String) request
                        .getAttribute("javax.servlet.include.path_info");
                if (pathInfo != null) {
                    jspUri += pathInfo;
                }
            } else {
                /*
                 * Requested JSP has not been the target of a
                 * RequestDispatcher.include(). Reconstruct its path from the
                 * request's getServletPath() and getPathInfo()
                 */
                jspUri = request.getServletPath();
                String pathInfo = request.getPathInfo();
                if (pathInfo != null) {
                    jspUri += pathInfo;
                }
            }
        }

        if (log.isDebugEnabled()) {
            StringBuffer msg = new StringBuffer();
            msg.append("JspEngine --> [" + jspUri);
            msg.append("] ServletPath: [" + request.getServletPath());
            msg.append("] PathInfo: [" + request.getPathInfo());
            msg.append("] RealPath: [" + context.getRealPath(jspUri));
            msg.append("] RequestURI: [" + request.getRequestURI());
            msg.append("] QueryString: [" + request.getQueryString());
            msg.append("]");
            log.debug(msg);
        }

        try {
            boolean precompile = preCompile(request);
            serviceJspFile(request, response, jspUri, null, precompile);
        } catch (RuntimeException e) {
            // STARTS S1AS
            incrementErrorCount();
            // END S1AS
            throw e;
        } catch (ServletException e) {
            // STARTS S1AS
            incrementErrorCount();
            // END S1AS
            throw e;
        } catch (IOException e) {
            // STARTS S1AS
            incrementErrorCount();
            // END S1AS
            throw e;
        } catch (Throwable e) {
            // STARTS S1AS
            incrementErrorCount();
            // END S1AS
            throw new ServletException(e);
        }

    }

    public void destroy() {
        if (log.isDebugEnabled()) {
            log.debug("JspServlet.destroy()");
        }

        rctxt.destroy();
        JspApplicationContextImpl.removeJspApplicationContext(context);

        // START GlassFish 750
        taglibs.clear();
        tagFileJarUrls.clear();
        // END GlassFish 750

        // START GlassFish 747
        HashMap tldUriToLocationMap = (HashMap) context
                .getAttribute(Constants.JSP_TLD_URI_TO_LOCATION_MAP);
        if (tldUriToLocationMap != null) {
            tldUriToLocationMap.clear();
        }
        // END GlassFish 747
    }

    // -------------------------------------------------------- Private Methods

    private void serviceJspFile(HttpServletRequest request,
                                HttpServletResponse response, String jspUri, Throwable exception,
                                boolean precompile) throws ServletException, IOException {

        JspServletWrapper wrapper = rctxt
                .getWrapper(jspUri);
        if (wrapper == null) {
            synchronized (this) {
                wrapper = rctxt.getWrapper(jspUri);
                if (wrapper == null) {
                    // Check if the requested JSP page exists, to avoid
                    // creating unnecessary directories and files.
                    /*
                     * START PWC 6181923 if (null ==
                     * context.getResource(jspUri)) {
                     */
                    // START PWC 6181923
                    if (null == context.getResource(jspUri)
                            && !options.getUsePrecompiled()) {
                        // END PWC 6181923

                        // START PWC 6300204
                        String includeRequestUri = (String) request
                                .getAttribute("javax.servlet.include.request_uri");
                        if (includeRequestUri != null) {
                            // Missing JSP resource has been the target of a
                            // RequestDispatcher.include().
                            // Throw an exception (rather than returning a
                            // 404 response error code), because any call to
                            // response.sendError() must be ignored by the
                            // servlet engine when issued from within an
                            // included resource (as per the Servlet spec).
                            throw new FileNotFoundException(jspUri);
                        }
                        // END PWC 6300204

                        /*
                         * RIMOD PWC 6282167, 4878272
                         * response.sendError(HttpServletResponse.SC_NOT_FOUND,
                         * jspUri);
                         */
                        // START PWC 6282167, 4878272
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        log.error(Localizer.getMessage(
                                "jsp.error.file.not.found",
                                context.getRealPath(jspUri)));
                        // END PWC 6282167, 4878272
                        return;
                    }
                    boolean isErrorPage = exception != null;
                    wrapper = new JspServletWrapper(config, options, jspUri,
                            isErrorPage, rctxt);
                    rctxt.addWrapper(jspUri, wrapper);
                }
            }
        }

        wrapper.service(request, response, precompile);

    }

    // STARTS S1AS
    private void incrementErrorCount() {
        synchronized (errorCountLk) {
            countErrors++;
        }
    }

    // END S1AS


    public ServletConfig getServletConfig() {
        return config;
    }

    public void setServletConfig(ServletConfig config) {
        this.config = config;
    }


}
