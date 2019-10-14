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
package org.tinygroup.jspengine.compiler;

import org.tinygroup.jspengine.Constants;
import org.tinygroup.jspengine.JspCompilationContext;
import org.tinygroup.jspengine.Options;
import org.tinygroup.jspengine.org.apache.commons.logging.Log;
import org.tinygroup.jspengine.org.apache.commons.logging.LogFactory;
import org.tinygroup.jspengine.runtime.JspFactoryImpl;
import org.tinygroup.jspengine.security.SecurityClassLoad;
import org.tinygroup.jspengine.servlet.JspServletWrapper;
import org.tinygroup.jspengine.util.SystemLogHandler;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspFactory;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Policy;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class for tracking JSP compile time file dependencies when the &060;%@include
 * file="..."%&062; directive is used.
 * <p>
 * A background thread periodically checks the files a JSP page is dependent
 * upon. If a dpendent file changes the JSP page which included it is
 * recompiled.
 * <p>
 * Only used if a web application context is a directory.
 *
 * @author Glenn L. Nielsen
 * @version $Revision: 1.14.6.1 $
 */
public final class JspRuntimeContext implements Runnable {

    // Logger
    private static Log log = LogFactory.getLog(JspRuntimeContext.class);

    /**
     * Preload classes required at runtime by a JSP servlet so that we don't get
     * a defineClassInPackage security exception.
     */
    static {
        JspFactoryImpl factory = new JspFactoryImpl();
        SecurityClassLoad
                .securityClassLoad(factory.getClass().getClassLoader());
        if (System.getSecurityManager() != null) {
            String basePackage = "org.tinygroup.jspengine.";
            try {
                factory.getClass()
                        .getClassLoader()
                        .loadClass(
                                basePackage
                                        + "runtime.JspFactoryImpl$PrivilegedGetPageContext");
                factory.getClass()
                        .getClassLoader()
                        .loadClass(
                                basePackage
                                        + "runtime.JspFactoryImpl$PrivilegedReleasePageContext");
                factory.getClass().getClassLoader()
                        .loadClass(basePackage + "runtime.JspRuntimeLibrary");
                factory.getClass()
                        .getClassLoader()
                        .loadClass(
                                basePackage
                                        + "runtime.JspRuntimeLibrary$PrivilegedIntrospectHelper");
                factory.getClass()
                        .getClassLoader()
                        .loadClass(
                                basePackage
                                        + "runtime.ServletResponseWrapperInclude");
                factory.getClass().getClassLoader()
                        .loadClass(basePackage + "servlet.JspServletWrapper");
            } catch (ClassNotFoundException ex) {
                log.error("Jasper JspRuntimeContext preload of class failed: "
                        + ex.getMessage(), ex);
            }
        }

        JspFactory.setDefaultFactory(factory);
    }

    /*
     * Counts how many times the webapp's JSPs have been reloaded.
     */
    private int jspReloadCount;

    // ----------------------------------------------------------- Constructors
    /**
     * This web applications ServletContext
     */
    private ServletContext context;

    // ----------------------------------------------------- Instance Variables
    private Options options;
    private ClassLoader parentClassLoader;
    private PermissionCollection permissionCollection;
    private CodeSource codeSource;
    private String classpath;
    /**
     * Maps JSP pages to their JspServletWrapper's
     */
    private Map<String, JspServletWrapper> jsps;
    /**
     * Maps class names to in-memory bytecodes
     */
    private Map<String, byte[]> bytecodes;
    private Map<String, Long> bytecodeBirthTimes;
    /**
     * Maps classes in packages compiled by the JSP compiler. Used only by
     * Jsr199Compiler. Should be Map<String, ArrayList<JavaFileObject>>, is this
     * way now so not to be dependent on the JSP199 API at build time.
     */
    private Map<String, ArrayList<Object>> packageMap;
    /**
     * The background thread.
     */
    private Thread thread = null;
    /**
     * The background thread completion semaphore.
     */
    private boolean threadDone = false;
    /**
     * Name to register for the background thread.
     */
    private String threadName = "JspRuntimeContext";

    /**
     * Create a JspRuntimeContext for a web application context.
     * <p>
     * Loads in any previously generated dependencies from file.
     *
     * @param context ServletContext for web application
     */
    public JspRuntimeContext(ServletContext context, Options options) {

        System.setErr(new SystemLogHandler(System.err));

        this.context = context;
        this.options = options;

        int hashSize = options.getInitialCapacity();
        jsps = new ConcurrentHashMap<String, JspServletWrapper>(hashSize);

        bytecodes = new ConcurrentHashMap<String, byte[]>(hashSize);
        bytecodeBirthTimes = new ConcurrentHashMap<String, Long>(hashSize);

        // Get the parent class loader
        parentClassLoader = Thread.currentThread()
                .getContextClassLoader();
        if (parentClassLoader == null) {
            parentClassLoader = this.getClass()
                    .getClassLoader();
        }

        if (log.isTraceEnabled()) {
            if (parentClassLoader != null) {
                log.trace(Localizer.getMessage(
                        "jsp.message.parent_class_loader_is",
                        parentClassLoader.toString()));
            } else {
                log.trace(Localizer.getMessage(
                        "jsp.message.parent_class_loader_is", "<none>"));
            }
        }

        initClassPath();

        if (context instanceof org.tinygroup.jspengine.servlet.JspCServletContext) {
            return;
        }

        if (Constants.IS_SECURITY_ENABLED) {
            initSecurity();
        }

        // If this web application context is running from a
        // directory, start the background compilation thread
        String appBase = context.getRealPath("/");
        if (!options.getDevelopment() && appBase != null
                && options.getCheckInterval() > 0) {
            if (appBase.endsWith(File.separator)) {
                appBase = appBase.substring(0, appBase.length() - 1);
            }
            String directory = appBase.substring(appBase
                    .lastIndexOf(File.separator));
            threadName = threadName + "[" + directory + "]";
            threadStart();
        }
    }

    // ------------------------------------------------------ Public Methods

    /**
     * Add a new JspServletWrapper.
     *
     * @param jspUri JSP URI
     * @param jsw    Servlet wrapper for JSP
     */
    public void addWrapper(String jspUri, JspServletWrapper jsw) {
        jsps.remove(jspUri);
        jsps.put(jspUri, jsw);
    }

    /**
     * Get an already existing JspServletWrapper.
     *
     * @param jspUri JSP URI
     * @return JspServletWrapper for JSP
     */
    public JspServletWrapper getWrapper(String jspUri) {
        return jsps.get(jspUri);
    }

    /**
     * Remove a JspServletWrapper.
     *
     * @param jspUri JSP URI of JspServletWrapper to remove
     */
    public void removeWrapper(String jspUri) {
        jsps.remove(jspUri);
    }

    /**
     * Returns the number of JSPs for which JspServletWrappers exist, i.e., the
     * number of JSPs that have been loaded into the webapp.
     *
     * @return The number of JSPs that have been loaded into the webapp
     */
    public int getJspCount() {
        return jsps.size();
    }

    /**
     * Get the SecurityManager Policy CodeSource for this web applicaiton
     * context.
     *
     * @return CodeSource for JSP
     */
    public CodeSource getCodeSource() {
        return codeSource;
    }

    /**
     * Get the parent URLClassLoader.
     *
     * @return URLClassLoader parent
     */
    public ClassLoader getParentClassLoader() {
        return parentClassLoader;
    }

    /**
     * Get the SecurityManager PermissionCollection for this web application
     * context.
     *
     * @return PermissionCollection permissions
     */
    public PermissionCollection getPermissionCollection() {
        return permissionCollection;
    }

    /**
     * Process a "destory" event for this web application context.
     */
    public void destroy() {

        if (System.err instanceof SystemLogHandler)
            System.setErr(((SystemLogHandler) System.err).getWrapped());

        threadStop();

        for (JspServletWrapper jsw : jsps.values()) {
            jsw.destroy();
        }

        parentClassLoader = null;
    }

    /**
     * Increments the JSP reload counter.
     */
    public synchronized void incrementJspReloadCount() {
        jspReloadCount++;
    }

    /**
     * Gets the current value of the JSP reload counter.
     *
     * @return The current value of the JSP reload counter
     */
    public int getJspReloadCount() {
        return jspReloadCount;
    }

    /**
     * Resets the JSP reload counter.
     *
     * @param count Value to which to reset the JSP reload counter
     */
    public synchronized void setJspReloadCount(int count) {
        this.jspReloadCount = count;
    }

    /**
     * Save the bytecode for the class in a map. The current time is noted.
     *
     * @param name     The name of the class
     * @param bytecode The bytecode in byte array
     */
    public void setBytecode(String name, byte[] bytecode) {
        if (bytecode == null) {
            bytecodes.remove(name);
            bytecodeBirthTimes.remove(name);
            return;
        }
        bytecodes.put(name, bytecode);
        bytecodeBirthTimes.put(name, Long.valueOf(System.currentTimeMillis()));
    }

    public void adjustBytecodeTime(String name, long reference) {
        Long time = bytecodeBirthTimes.get(name);
        if (time == null)
            return;

        if (time.longValue() < reference) {
            bytecodeBirthTimes.put(name, Long.valueOf(reference));
        }
    }

    /**
     * Get the class-name to bytecode map
     */
    public Map<String, byte[]> getBytecodes() {
        return bytecodes;
    }

    /**
     * Retrieve the bytecode associated with the class
     */
    public byte[] getBytecode(String name) {
        return bytecodes.get(name);
    }

    /**
     * Retrieve the time the bytecode for a class was created
     */
    public long getBytecodeBirthTime(String name) {
        Long time = bytecodeBirthTimes.get(name);
        return (time != null ? time.longValue() : 0);
    }

    /**
     * The packageMap keeps track of the bytecode files in a package generated
     * by a java compiler. This is in turn loaded by the java compiler during
     * compilation. This is gets around the fact that JSR199 API does not
     * provide a way for the compiler use current classloader.
     */
    public Map<String, ArrayList<Object>> getPackageMap() {
        if (packageMap == null) {
            packageMap = new HashMap<String, ArrayList<Object>>();
        }
        return packageMap;
    }

    /**
     * Save the bytecode for a class to disk.
     */
    public void saveBytecode(String className, String classFileName) {
        byte[] bytecode = getBytecode(className);
        if (bytecode != null) {
            try {
                FileOutputStream fos = new FileOutputStream(classFileName);
                fos.write(bytecode);
                fos.close();
            } catch (IOException ex) {
                context.log("Error in saving bytecode for " + className
                        + " to " + classFileName, ex);
            }
        }
    }

    // -------------------------------------------------------- Private Methods

    /**
     * Method used by background thread to check the JSP dependencies registered
     * with this class for JSP's.
     */
    private void checkCompile() {
        for (JspServletWrapper jsw : jsps.values()) {
            if (jsw.isTagFile()) {
                // Skip tag files in background compiliations, since modified
                // tag files will be recompiled anyway when their client JSP
                // pages are compiled. This also avoids problems when the
                // tag files and their clients are not modified simultaneously.
                continue;
            }

            JspCompilationContext ctxt = jsw.getJspEngineContext();
            // JspServletWrapper also synchronizes on this when
            // it detects it has to do a reload
            synchronized (jsw) {
                try {
                    ctxt.compile();
                } catch (FileNotFoundException ex) {
                    ctxt.incrementRemoved();
                } catch (Throwable t) {
                    jsw.getServletContext().log("Background compile failed", t);
                }
            }
        }
    }

    /**
     * The classpath that is passed off to the Java compiler.
     */
    public String getClassPath() {
        return classpath;
    }

    /**
     * Method used to initialize classpath for compiles.
     */
    private void initClassPath() {
        if (parentClassLoader instanceof URLClassLoader) {
            URL[] urls = ((URLClassLoader) parentClassLoader).getURLs();
            StringBuffer cpath = new StringBuffer();
            String sep = System.getProperty("path.separator");

            for (int i = 0; i < urls.length; i++) {
                // Tomcat 4 can use URL's other than file URL's,
                // a protocol other than file: will generate a
                // bad file system path, so only add file:
                // protocol URL's to the classpath.

                if (urls[i].getProtocol().equals("file")) {
                    cpath.append(urls[i].getFile() + sep);
                }
            }

            cpath.append(options.getScratchDir() + sep);

            String cp = (String) context
                    .getAttribute(Constants.SERVLET_CLASSPATH);
            if (cp == null || cp.equals("")) {
                cp = options.getClassPath();
            }

            classpath = cpath.toString() + cp;

            // START GlassFish Issue 845
            try {
                classpath = URLDecoder.decode(classpath, "UTF-8");

            } catch (UnsupportedEncodingException e) {
                if (log.isDebugEnabled())
                    log.debug("Exception decoding classpath : " + classpath, e);
            }
        }
        // END GlassFish Issue 845
    }

    /**
     * Method used to initialize SecurityManager data.
     */
    private void initSecurity() {

        // Setup the PermissionCollection for this web app context
        // based on the permissions configured for the root of the
        // web app context directory, then add a file read permission
        // for that directory.
        Policy policy = Policy.getPolicy();
        if (policy != null) {
            try {
                // Get the permissions for the web app context
                String docBase = context.getRealPath("/");
                if (docBase == null) {
                    docBase = options.getScratchDir().toString();
                }
                String codeBase = docBase;
                if (!codeBase.endsWith(File.separator)) {
                    codeBase = codeBase + File.separator;
                }
                File contextDir = new File(codeBase);
                URL url = contextDir.getCanonicalFile().toURL();
                codeSource = new CodeSource(url, (Certificate[]) null);
                permissionCollection = policy.getPermissions(codeSource);

                // Create a file read permission for web app context directory
                if (!docBase.endsWith(File.separator)) {
                    permissionCollection
                            .add(new FilePermission(docBase, "read"));
                    docBase = docBase + File.separator;
                } else {
                    permissionCollection.add(new FilePermission(docBase
                            .substring(0, docBase.length() - 1), "read"));
                }
                docBase = docBase + "-";
                permissionCollection.add(new FilePermission(docBase, "read"));

                // Create a file read permission for web app tempdir (work)
                // directory
                String workDir = options.getScratchDir().toString();
                if (!workDir.endsWith(File.separator)) {
                    permissionCollection
                            .add(new FilePermission(workDir, "read"));
                    workDir = workDir + File.separator;
                }
                workDir = workDir + "-";
                permissionCollection.add(new FilePermission(workDir, "read"));

                // Allow the JSP to access
                // org.tinygroup.jspengine.runtime.HttpJspBase
                permissionCollection.add(new RuntimePermission(
                        "accessClassInPackage.org.tinygroup.jspengine.runtime"));

                if (parentClassLoader instanceof URLClassLoader) {
                    URL[] urls = ((URLClassLoader) parentClassLoader).getURLs();
                    String jarUrl = null;
                    String jndiUrl = null;
                    for (int i = 0; i < urls.length; i++) {
                        if (jndiUrl == null
                                && urls[i].toString().startsWith("jndi:")) {
                            jndiUrl = urls[i].toString() + "-";
                        }
                        if (jarUrl == null
                                && urls[i].toString().startsWith("jar:jndi:")) {
                            jarUrl = urls[i].toString();
                            jarUrl = jarUrl.substring(0, jarUrl.length() - 2);
                            jarUrl = jarUrl.substring(0,
                                    jarUrl.lastIndexOf('/'))
                                    + "/-";
                        }
                    }
                    if (jarUrl != null) {
                        permissionCollection.add(new FilePermission(jarUrl,
                                "read"));
                        permissionCollection.add(new FilePermission(jarUrl
                                .substring(4), "read"));
                    }
                    if (jndiUrl != null)
                        permissionCollection.add(new FilePermission(jndiUrl,
                                "read"));
                }
            } catch (Exception e) {
                context.log("Security Init for context failed", e);
            }
        }
    }

    // -------------------------------------------------------- Thread Support

    /**
     * Start the background thread that will periodically check for changes to
     * compile time included files in a JSP.
     *
     * @throws IllegalStateException if we should not be starting a background thread now
     */
    protected void threadStart() {

        // Has the background thread already been started?
        if (thread != null) {
            return;
        }

        // Start the background thread
        threadDone = false;
        thread = new Thread(this, threadName);
        thread.setDaemon(true);
        thread.start();

    }

    /**
     * Stop the background thread that is periodically checking for changes to
     * compile time included files in a JSP.
     */
    protected void threadStop() {

        if (thread == null) {
            return;
        }

        threadDone = true;
        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {
        }

        thread = null;

    }

    /**
     * Sleep for the duration specified by the <code>checkInterval</code>
     * property.
     */
    protected void threadSleep() {

        try {
            Thread.sleep(options.getCheckInterval() * 1000L);
        } catch (InterruptedException e) {
        }

    }

    // ------------------------------------------------------ Background Thread

    /**
     * The background thread that checks for changes to files included by a JSP
     * and flags that a recompile is required.
     */
    public void run() {

        // Loop until the termination semaphore is set
        while (!threadDone) {

            // Wait for our check interval
            threadSleep();

            // Check for included files which are newer than the
            // JSP which uses them.
            try {
                checkCompile();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

    }

}
