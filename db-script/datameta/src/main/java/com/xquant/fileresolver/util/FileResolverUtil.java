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
package com.xquant.fileresolver.util;

import com.xquant.configuration.util.ConfigurationUtil;
import com.xquant.vfs.FileObject;
import com.xquant.fileresolver.FileResolver;
import com.xquant.vfs.VFS;
import org.apache.commons.lang.StringUtils;
import org.apache.derby.iapi.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileResolverUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileResolverUtil.class);

    private FileResolverUtil() {

    }

    public static void addClassPathPattern(FileResolver resolver) {
        resolver.addIncludePathPattern("[\\/]classes\\b");
        resolver.addIncludePathPattern("[\\/]test-classes\\b");
    }


    public static List<String> getClassPath(FileResolver resolver) {
        List<String> classPathFileObjects = new ArrayList<String>();
        String classPathProperty = System.getProperty("java.class.path").toString();
        String operateSys = System.getProperty("os.name").toLowerCase();
        String[] classPaths = null;
        if (operateSys.indexOf("windows") >= 0) {
            classPaths = classPathProperty.split(";");
        } else {
            classPaths = classPathProperty.split(":");
        }
        if (classPaths == null) {
            return classPathFileObjects;
        }
        // if (classPaths != null) {
        for (String classPath : classPaths) {
            if (classPath.length() > 0) {
                FileObject fileObject = VFS.resolveFile(classPath);
                if (isInclude(fileObject, resolver)) {
                    classPathFileObjects.add(fileObject.getAbsolutePath());
                }
            }
        }
        // }
        return classPathFileObjects;
    }

    public static boolean isInclude(FileObject fileObject, FileResolver resolver) {
        // jboss可能出现解析到根本不存在的jar包，这种情况直接忽略
        if (fileObject == null || !fileObject.isExist()) {
            return false;
        }
        return true;
    }

    public static List<String> dealJarSource(FileResolver resolver, String path) {
        List<String> list = new ArrayList<String>();
        if (path.indexOf("!") < 0) {
            path = path.substring(0, path.length() - "META-INF/MANIFEST.MF".length() - 1);
            path = path.substring(path.indexOf(':') + 1);
            list.add(path);
            return list;
        }
        String[] pathArray = path.split("!");
        if (pathArray.length <= 2) {
            list.add(pathArray[0]);
            return list;
        }
        LOGGER.info("存在jar包嵌套jar包的路径：{}", path);
        try {
            return FileDealUtil.unpack(pathArray);
        } catch (IOException e) {
            LOGGER.error("解析路径：{}发生异常", e, path);
            return list;
        }

    }

    public static List<String> getWebLibJars(FileResolver resolver) throws Exception {
        final List<String> classPaths = new ArrayList<String>();
        LOGGER.info( "查找Web工程中的jar文件列表开始...");
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> urls = loader.getResources("META-INF/MANIFEST.MF");
        Map<String, Pattern> includePathPatternMap = resolver.getIncludePathPatternMap();
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            String path = url.toString();
            // 20170614
            // 修改path.replaceAll("/./", "/")为path.replaceAll("/\\./", "/")
            // 解决路径中存在单字符时，会被replace掉的问题，参见测试用例TestReplaceAll
            path = path.replaceAll("/\\./", "/");// weblogic中存在这种情况
            // path = path.replaceAll("/./", "/");// weblogic中存在这种情况

            // 20170720 注释下面7行，将其放入dealJarSource中并进行功能扩展
            // if (path.indexOf("!") > 0) {
            // path = path.split("!")[0];
            // } else {// 专门为JBOSS vfs开头的处理
            // path = path.substring(0, path.length() -
            // "META-INF/MANIFEST.MF".length() - 1);
            // path = path.substring(path.indexOf(':') + 1);
            // }
            List<String> paths = dealJarSource(resolver, path);
            for (String searchPath : paths) {
                checkPath(resolver, classPaths, includePathPatternMap, url, searchPath);
            }

        }

        LOGGER.info( "查找Web工程中的jar文件列表完成。");
        return classPaths;
    }

    private static void checkPath(FileResolver resolver, final List<String> classPaths,
                                  Map<String, Pattern> includePathPatternMap, URL url, String path) throws IOException {
        LOGGER.debug( "开始检查路径{}是否可被扫描", path);
        FileObject fileObject = VFS.resolveFile(path);
        if (includePathPatternMap != null && !includePathPatternMap.isEmpty()) {
            if (isInclude(fileObject, resolver)) {
                LOGGER.info( "文件<{}>由于符合正则规则而被扫描。",
                        fileObject);
                addJarFile(classPaths, fileObject.getAbsolutePath());
                LOGGER.debug("检查路径{}结束", path);
                return;
            }
        }
        if (fileObject.isExist()) {
            InputStream stream = null;
            try {
                stream = url.openStream();
                Manifest mf = new Manifest(stream);
                Attributes attributes = mf.getMainAttributes();
                String isTinyProject = attributes.getValue("IsTinyProject");
                if ("true".equals(isTinyProject)) {
                    LOGGER.info("文件<{}>由于在MANIFEST.MF文件中声明了IsTinyProject: true而被扫描。",
                            fileObject);
                    addJarFile(classPaths, fileObject.getAbsolutePath());
                }
            } finally {
                if (stream != null) {
                    stream.close();
                }

            }
        }
        LOGGER.debug("检查路径{}结束", path);
    }

    private static void addJarFile(List<String> classPaths, String path) {
        LOGGER.info("扫描到jar文件<{}>。", path);
        classPaths.add(path);
    }
    public static List<String> getWebClasses() {
        List<String> allScanningPath = new ArrayList<String>();
        LOGGER.info( "查找WEB-INF/classes路径开始...");
        URL url = getURL();
        String path = url.toString();
        LOGGER.info( "WEB-INF/classes路径是:" + path);
        if (path.indexOf("!") < 0) {// 如果在目录中
            FileObject fileObject = VFS.resolveFile(path);
            allScanningPath.add(fileObject.getAbsolutePath());
            String libPath = path.replaceAll("/classes", "/lib");
            LOGGER.info("WEB-INF/lib路径是:{}" +  libPath);
            FileObject libFileObject = VFS.resolveFile(libPath);

            allScanningPath.add(libFileObject.getAbsolutePath());
            int index = path.indexOf("/classes");
            if (index > 0) {
                String webInfPath = path.substring(0, index);
                if (webInfPath.endsWith("WEB-INF")) {
                    LOGGER.info("WEB-INF路径是:" + webInfPath);
                    FileObject webInfoFileObject = VFS.resolveFile(webInfPath);
                    allScanningPath.add(webInfoFileObject.getAbsolutePath());
                }
            }

        } else {// 如果在jar包中
            path = url.getFile().split("!")[0];
            FileObject fileObject = VFS.resolveFile(path);
            allScanningPath.add(fileObject.getAbsolutePath());
            String libPath = path.substring(0, path.lastIndexOf('/'));
            LOGGER.info( "WEB-INF/lib路径是:" + libPath);
            FileObject libFileObject = VFS.resolveFile(libPath);
            allScanningPath.add(libFileObject.getAbsolutePath());
        }
        LOGGER.info( "查找WEB-INF/classes路径完成。");

        String webinfPath = ConfigurationUtil.getConfigurationManager().getConfiguration().get("TINY_WEBROOT");
        if (StringUtils.isEmpty(webinfPath)) {
            LOGGER.info( "WEBROOT变量找不到");
            return allScanningPath;
        }
        FileObject fileObject = VFS.resolveFile(webinfPath);
        allScanningPath.add(fileObject.getAbsolutePath());
        return allScanningPath;
    }

    private static URL getURL() {
        URL url = null;
        String weblogicMode = ConfigurationUtil.getConfigurationManager().getConfiguration("WEBLOGIC_MODE");
        if (weblogicMode != null && "true".equalsIgnoreCase(weblogicMode)) {
            url = FileResolverUtil.class.getClassLoader().getResource("/");
            if (url == null) {
                url = FileResolverUtil.class.getClassLoader().getResource("");
            }
        } else {
            url = FileResolverUtil.class.getResource("/");
            if (url == null) {
                url = FileResolverUtil.class.getResource("");
            }
        }
        return url;
    }

}
