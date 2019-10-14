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
package org.tinygroup.jspengine.runtime;

import org.tinygroup.jspengine.Constants;
import org.tinygroup.jspengine.org.apache.commons.logging.Log;
import org.tinygroup.jspengine.org.apache.commons.logging.LogFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspApplicationContext;
import javax.servlet.jsp.JspEngineInfo;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.PageContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.LinkedList;

/**
 * Implementation of JspFactory.
 *
 * @author Anil K. Vijendran
 * @author Kin-man Chung
 */
public class JspFactoryImpl extends JspFactory {

    private static final String SPEC_VERSION = "2.1";
    private static final boolean USE_POOL = true;
    // Logger
    private static Log log = LogFactory.getLog(JspFactoryImpl.class);
    // Per-thread pool of PageContext objects
    private ThreadLocal pool = new ThreadLocal() {
        protected synchronized Object initialValue() {
            return new LinkedList<PageContext>();
        }
    };

    public PageContext getPageContext(Servlet servlet,
                                      ServletRequest request,
                                      ServletResponse response,
                                      String errorPageURL,
                                      boolean needsSession,
                                      int bufferSize,
                                      boolean autoflush) {

        if (Constants.IS_SECURITY_ENABLED) {
            PrivilegedGetPageContext dp = new PrivilegedGetPageContext(
                    this, servlet, request, response, errorPageURL,
                    needsSession, bufferSize, autoflush);
            return (PageContext) AccessController.doPrivileged(dp);
        } else {
            return internalGetPageContext(servlet, request, response,
                    errorPageURL, needsSession,
                    bufferSize, autoflush);
        }
    }

    public void releasePageContext(PageContext pc) {
        if (pc == null)
            return;
        if (Constants.IS_SECURITY_ENABLED) {
            PrivilegedReleasePageContext dp = new PrivilegedReleasePageContext(
                    this, pc);
            AccessController.doPrivileged(dp);
        } else {
            internalReleasePageContext(pc);
        }
    }

    public JspEngineInfo getEngineInfo() {
        return new JspEngineInfo() {
            public String getSpecificationVersion() {
                return SPEC_VERSION;
            }
        };
    }

    public JspApplicationContext getJspApplicationContext
            (ServletContext context) {
        return JspApplicationContextImpl.findJspApplicationContext(context);
    }

    private PageContext internalGetPageContext(Servlet servlet,
                                               ServletRequest request,
                                               ServletResponse response,
                                               String errorPageURL,
                                               boolean needsSession,
                                               int bufferSize,
                                               boolean autoflush) {
        try {
            PageContext pc = null;
            if (USE_POOL) {
                LinkedList<PageContext> pcPool = (LinkedList<PageContext>)
                        pool.get();
                if (!pcPool.isEmpty()) {
                    pc = pcPool.removeFirst();
                }
                if (pc == null) {
                    pc = new PageContextImpl(this);
                }
            } else {
                pc = new PageContextImpl(this);
            }
            pc.initialize(servlet, request, response, errorPageURL,
                    needsSession, bufferSize, autoflush);
            return pc;
        } catch (Throwable ex) {
            /* FIXME: need to do something reasonable here!! */
            log.fatal("Exception initializing page context", ex);
            return null;
        }
    }

    private void internalReleasePageContext(PageContext pc) {
        pc.release();
        if (USE_POOL && (pc instanceof PageContextImpl)) {
            LinkedList<PageContext> pcPool = (LinkedList<PageContext>) pool.get();
            pcPool.addFirst(pc);
        }
    }

    private class PrivilegedGetPageContext implements PrivilegedAction {

        private JspFactoryImpl factory;
        private Servlet servlet;
        private ServletRequest request;
        private ServletResponse response;
        private String errorPageURL;
        private boolean needsSession;
        private int bufferSize;
        private boolean autoflush;

        PrivilegedGetPageContext(JspFactoryImpl factory,
                                 Servlet servlet,
                                 ServletRequest request,
                                 ServletResponse response,
                                 String errorPageURL,
                                 boolean needsSession,
                                 int bufferSize,
                                 boolean autoflush) {
            this.factory = factory;
            this.servlet = servlet;
            this.request = request;
            this.response = response;
            this.errorPageURL = errorPageURL;
            this.needsSession = needsSession;
            this.bufferSize = bufferSize;
            this.autoflush = autoflush;
        }

        public Object run() {
            return factory.internalGetPageContext(servlet,
                    request,
                    response,
                    errorPageURL,
                    needsSession,
                    bufferSize,
                    autoflush);
        }
    }

    private class PrivilegedReleasePageContext implements PrivilegedAction {

        private JspFactoryImpl factory;
        private PageContext pageContext;

        PrivilegedReleasePageContext(JspFactoryImpl factory,
                                     PageContext pageContext) {
            this.factory = factory;
            this.pageContext = pageContext;
        }

        public Object run() {
            factory.internalReleasePageContext(pageContext);
            return null;
        }
    }
}
