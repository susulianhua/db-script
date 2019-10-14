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
package org.tinygroup.uiengineweblayer.tinyprocessor;

import org.tinygroup.commons.file.FileDealUtil;
import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.commons.tools.FileUtil;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.fileresolver.FullContextFileRepository;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.uiengine.config.UIComponent;
import org.tinygroup.uiengine.manager.UIComponentManager;
import org.tinygroup.uiengineweblayer.UiCssInfo;
import org.tinygroup.uiengineweblayer.UiCssManager;
import org.tinygroup.uiengineweblayer.UiEngineUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.weblayer.AbstractTinyProcessor;
import org.tinygroup.weblayer.WebContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;

/**
 * UI引擎处理css及js文件的合并并输出
 *
 * @author luoguo
 */
public class UiEngineTinyProcessor extends AbstractTinyProcessor {
    public static final String UI_STORAGE_TYPE = "storageType";
    public static final String CACHE_CONTROL_NAME = "cacheControl";
    public static final String JS_NAME_PARAM = "jsName";
    public static final String CSS_NAME_PARAM = "cssName";

    private static final String CACHE_CONTROL = "max-age=86400"; //默认cache保存时间，单位s
    private static final Logger logger = LoggerFactory.getLogger(UiEngineTinyProcessor.class);
    UIComponentManager uiComponentManager;
    private FullContextFileRepository fullContextFileRepository;
    private UiCssManager uiCssManager;
    private JsResourceOperator jsResourceOperator;
    private CssResourceOperator cssResourceOperator;
    private String cacheControl;
    private String jsName = "uiengine.uijs";
    private String cssName = "uiengine.uicss";

    public UIComponentManager getUiComponentManager() {
        return uiComponentManager;
    }

    public void setUiComponentManager(UIComponentManager uiComponentManager) {
        this.uiComponentManager = uiComponentManager;
    }

    public FullContextFileRepository getFullContextFileRepository() {
        return fullContextFileRepository;
    }

    public void setFullContextFileRepository(FullContextFileRepository fullContextFileRepository) {
        this.fullContextFileRepository = fullContextFileRepository;
    }

    public UiCssManager getUiCssManager() {
        return uiCssManager;
    }

    public void setUiCssManager(UiCssManager uiCssManager) {
        this.uiCssManager = uiCssManager;
    }

    public void reallyProcess(String servletPath, WebContext context) throws ServletException, IOException {
        logger.logMessage(LogLevel.DEBUG, "{}开始处理...", servletPath);
        HttpServletResponse response = context.getResponse();
        HttpServletRequest request = context.getRequest();
        String contextPath = context.get("TINY_CONTEXT_PATH");

        long modifiedSign = 0;
        long now = System.currentTimeMillis();
        if (servletPath.endsWith(jsName)) {
            modifiedSign = jsResourceOperator.getModifiedSign();
            response.setContentType("text/javascript");
        } else if (servletPath.endsWith(cssName)) {
            modifiedSign = cssResourceOperator.getModifiedSign();
            response.setContentType("text/css");
        } else {
            throw new RuntimeException("UiEngineTinyProcessor不能处理请求：" + servletPath);
        }

        String lastModifiedSign = String.valueOf(modifiedSign); //直接转换String，反正不是时间
        String ims = request.getHeader("If-Modified-Since");
        if (ims != null && ims.equals(lastModifiedSign)) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }
        response.setHeader("Last-modified", lastModifiedSign);
        response.setHeader("Connection", "keep-alive");
        response.setHeader("Cache-Control", cacheControl);
        response.setHeader("Date", new Date(now).toGMTString());

        if (servletPath.endsWith(jsName)) {
            jsResourceOperator.writeJs(modifiedSign, response);
        } else if (servletPath.endsWith(cssName)) {
            if (context.exist(uiCssManager.getParamterName())) {
                int cssno = Integer.parseInt((String) context.get(uiCssManager.getParamterName()));
                cssResourceOperator.writeCss(cssno, response, contextPath, servletPath);
            } else {
                cssResourceOperator.writeCss(modifiedSign, response, contextPath, servletPath);
            }

        }

        logger.logMessage(LogLevel.DEBUG, "{}处理完成。", servletPath);

    }

    protected void customInit() throws ServletException {

        //初始化存储类型
        String storageType = get(UI_STORAGE_TYPE);

        //初始化Cache-Control
        String cache = get(CACHE_CONTROL_NAME);
        cacheControl = StringUtil.isEmpty(cache) ? CACHE_CONTROL : cache;

        String cssNameParam = get(CSS_NAME_PARAM);
        if (!StringUtil.isBlank(cssNameParam)) {
            cssName = cssNameParam;
        }
        String jsNameParam = get(JS_NAME_PARAM);
        if (!StringUtil.isBlank(jsNameParam)) {
            jsName = jsNameParam;
        }

        jsResourceOperator = new JsResourceOperator();
        cssResourceOperator = new CssResourceOperator();

        if (StringUtil.isEmpty(storageType)) {
            initMemoryStorage();
        } else {
            if ("file".equals(storageType)) {
                initTempFileStorage();
            } else if ("memory".equals(storageType)) {
                initMemoryStorage();
            } else {
                throw new RuntimeException(String.format("UiEngineTinyProcessor初始化失败:未知的存储类型[%s]，请检查配置文件", storageType));
            }
        }

    }

    private void initTempFileStorage() {
        String tiny_webroot = ConfigurationUtil.getConfigurationManager().getConfiguration("TINY_WEBROOT");
        if (StringUtil.isEmpty(tiny_webroot)) {
            tiny_webroot = "";
            logger.logMessage(LogLevel.WARN, "没有找到TINY_WEBROOT环境参数，请检查Web容器配置");
        } else if (!tiny_webroot.endsWith(File.separator)) {
            tiny_webroot = tiny_webroot + File.separator;
        }

        jsResourceOperator.storage = new TempFileStorage(tiny_webroot + jsName);
        cssResourceOperator.storage = new TempFileStorage(tiny_webroot + cssName);
    }

    private void initMemoryStorage() {
        jsResourceOperator.storage = new MemoryStorage();
        cssResourceOperator.storage = new MemoryStorage();
    }

    /**
     * 资源存储
     *
     * @author yancheng11334
     */
    interface ResourceStorage {
        /**
         * 资源是否还在
         *
         * @return
         */
        boolean exist();

        /**
         * 读取资源
         *
         * @return
         */
        String read();

        /**
         * 保存资源
         *
         * @param resource
         */
        void store(String resource);
    }

    /**
     * 临时文件存储，也是缺省配置
     *
     * @author yancheng11334
     */
    class TempFileStorage implements ResourceStorage {

        private File storeFile;

        public TempFileStorage(String fileName) {
            storeFile = new File(fileName);
        }

        public synchronized boolean exist() {
            return storeFile.exists();
        }

        public synchronized String read() {
            try {
                return FileUtil.readFileContent(storeFile, "UTF-8");
            } catch (Exception e) {
                throw new RuntimeException(String.format("读取文件[%s]发生异常:", storeFile.getAbsolutePath()), e);
            }
        }

        public synchronized void store(String resource) {
            try {
                FileDealUtil.write(storeFile, resource);
            } catch (Exception e) {
                throw new RuntimeException(String.format("写入文件[%s]发生异常:", storeFile.getAbsolutePath()), e);
            }
        }

    }

    /**
     * 内存方式存储
     *
     * @author yancheng11334
     */
    class MemoryStorage implements ResourceStorage {

        private String result = null;

        public boolean exist() {
            return result != null;
        }

        public String read() {
            return result;
        }

        public void store(String resource) {
            result = resource;
        }

    }

    abstract class BaseOperator {
        protected ResourceStorage storage;
        protected long lastModified = 0;

        /**
         * 计算修改码
         *
         * @return
         */
        protected abstract long getModifiedSign();
    }

    /**
     * JS资源操作
     *
     * @author yancheng11334
     */
    class JsResourceOperator extends BaseOperator {

        public void writeJs(long modifiedSign, HttpServletResponse response) throws IOException {
            if (lastModified == 0 || lastModified != modifiedSign || !storage.exist()) {
                //执行生成js逻辑,并更新验证时间戳
                byte[] b = createJs();
                storage.store(new String(b, "UTF-8"));
                lastModified = modifiedSign;
                response.getOutputStream().write(b);
            } else {
                //从存储中读取js
                String resource = storage.read();
                response.getOutputStream().write(resource.getBytes("UTF-8"));
            }
        }

        private byte[] createJs() throws IOException {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                for (UIComponent component : uiComponentManager.getHealthUiComponents()) {
                    String[] paths = uiComponentManager.getComponentJsArray(component);
                    if (paths != null) {
                        for (String path : paths) {
                            logger.logMessage(LogLevel.DEBUG, "正在处理js文件:<{}>", path);
                            outputStream.write("try{\n".getBytes());
                            FileObject fileObject = fullContextFileRepository.getFileObject(path);
                            InputStream stream = new BufferedInputStream(fileObject.getInputStream());
                            StreamUtil.io(stream, outputStream, true, false);
                            outputStream.write("\n;}catch(e){}\n".getBytes());
                            logger.logMessage(LogLevel.DEBUG, "js文件:<{}>处理完毕", path);
                        }
                    }
                    if (component.getJsCodelet() != null) {
                        outputStream.write(component.getJsCodelet().getBytes("UTF-8"));
                    }
                }
                return outputStream.toByteArray();
            } finally {
                outputStream.close();
            }
        }

        public long getModifiedSign() {
            long time = 0;
            for (UIComponent component : uiComponentManager.getHealthUiComponents()) {
                String[] paths = uiComponentManager.getComponentJsArray(component);
                if (paths != null) {
                    for (String path : paths) {
                        FileObject fileObject = fullContextFileRepository.getFileObject(path);
                        if (fileObject != null && fileObject.isExist()) {
                            time += fileObject.getLastModifiedTime();
                            time += path.hashCode();
                        } else {
                            throw new RuntimeException("不能找到资源文件：" + component.getName() + "-" + path);
                        }
                    }
                }
            }
            return time;
        }
    }

    /**
     * CSS资源操作
     *
     * @author yancheng11334
     */
    class CssResourceOperator extends BaseOperator {

        public void writeCss(int cssno, HttpServletResponse response, String contextPath, String servletPath)
                throws IOException {
            UiCssInfo info = uiCssManager.getUiCssInfo(cssno);
            if (info != null) {
                response.getOutputStream().write(info.getBytes());
            }
        }

        public void writeCss(long modifiedSign, HttpServletResponse response, String contextPath, String servletPath)
                throws IOException {
            if (lastModified == 0 || lastModified != modifiedSign || !storage.exist()) {
                //执行生成css逻辑,并更新验证时间戳
                byte[] b = createCss(contextPath, servletPath);
                storage.store(new String(b, "UTF-8"));
                lastModified = modifiedSign;
                response.getOutputStream().write(b);
            } else {
                //从存储中读取css
                String resource = storage.read();
                response.getOutputStream().write(resource.getBytes("UTF-8"));
            }
        }

        private byte[] createCss(String contextPath, String servletPath) throws IOException {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                outputStream.write("@charset \"utf-8\";\n".getBytes());
                for (UIComponent component : uiComponentManager.getHealthUiComponents()) {
                    String[] paths = uiComponentManager.getComponentCssArray(component);
                    if (paths != null) {
                        for (String path : paths) {
                            logger.logMessage(LogLevel.DEBUG, "正在处理css文件:<{}>", path);
                            FileObject fileObject = fullContextFileRepository.getFileObject(path);
                            UiEngineUtil.writeCss(fileObject, outputStream, contextPath, servletPath);
                            outputStream.write('\n');
                            logger.logMessage(LogLevel.DEBUG, "css文件:<{}>处理完毕", path);
                        }
                    }
                    if (component.getCssCodelet() != null) {
                        UiEngineUtil.replaceCss(outputStream, contextPath, component.getCssCodelet(), servletPath);
                    }
                }
                return outputStream.toByteArray();
            } finally {
                outputStream.close();
            }
        }


        public long getModifiedSign() {
            long time = 0;
            for (UIComponent component : uiComponentManager.getHealthUiComponents()) {
                if (component != null) {
                    String[] paths = uiComponentManager.getComponentCssArray(component);
                    if (paths != null) {
                        for (String path : paths) {
                            FileObject fileObject = fullContextFileRepository.getFileObject(path);
                            if (fileObject != null && fileObject.isExist()) {
                                time += fileObject.getLastModifiedTime();
                                time += path.hashCode();
                            } else {
                                throw new RuntimeException("不能找到资源文件：" + component.getName() + "-" + path);
                            }
                        }
                    }
                }
            }
            return time;
        }
    }
}
