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
package org.tinygroup.uienginestore.impl;

import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.fileresolver.FullContextFileRepository;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.uiengine.config.UIComponent;
import org.tinygroup.uiengine.manager.UIComponentManager;
import org.tinygroup.uienginestore.MergeCssManager;
import org.tinygroup.uienginestore.StoreConfig;
import org.tinygroup.uienginestore.UIOperator;
import org.tinygroup.vfs.FileObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultUIOperator implements UIOperator {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultUIOperator.class);
    static Pattern urlPattern = Pattern.compile("(url[(][\"\']?)(.*?)([\"\']?[)])");
    private UIComponentManager uiComponentManager;
    private FullContextFileRepository fullContextFileRepository;
    private MergeCssManager mergeCssManager;

    public UIComponentManager getUiComponentManager() {
        return uiComponentManager;
    }

    public void setUiComponentManager(UIComponentManager uiComponentManager) {
        this.uiComponentManager = uiComponentManager;
    }

    public FullContextFileRepository getFullContextFileRepository() {
        return fullContextFileRepository;
    }

    public void setFullContextFileRepository(
            FullContextFileRepository fullContextFileRepository) {
        this.fullContextFileRepository = fullContextFileRepository;
    }

    public MergeCssManager getMergeCssManager() {
        return mergeCssManager;
    }

    public void setMergeCssManager(MergeCssManager mergeCssManager) {
        this.mergeCssManager = mergeCssManager;
    }

    public List<FileObject> createJS(StoreConfig config) throws Exception {
        List<FileObject> list = new ArrayList<FileObject>();
        if (config.isMergeTag()) {
            //合并输出
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                for (UIComponent component : uiComponentManager.getHealthUiComponents()) {
                    writeMergeJs(component, outputStream);
                }
                list.add(new SourceFileObject(config.getMergeJsName(), outputStream.toByteArray()));
            } finally {
                outputStream.close();
            }

        } else {
            //单独输出
            for (UIComponent component : uiComponentManager.getHealthUiComponents()) {
                String[] paths = uiComponentManager.getComponentJsArray(component);
                if (paths != null) {
                    for (String path : paths) {
                        LOGGER.logMessage(LogLevel.DEBUG, "正在处理js文件:<{}>", path);
                        //不直接返回fullContextFileRepository的FileObject避免最后统一清理了。
                        FileObject fileObject = fullContextFileRepository.getFileObject(path);
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        try {
                            InputStream stream = new BufferedInputStream(fileObject.getInputStream());
                            StreamUtil.io(stream, outputStream, true, false);
                            list.add(new SourceFileObject(fileObject.getPath(), outputStream.toByteArray()));
                        } finally {
                            outputStream.close();
                        }
                        LOGGER.logMessage(LogLevel.DEBUG, "js文件:<{}>处理完毕", path);
                    }
                }
            }
        }
        return list;
    }

    private void writeMergeJs(UIComponent component, ByteArrayOutputStream outputStream) throws Exception {
        String[] paths = uiComponentManager.getComponentJsArray(component);
        if (paths != null) {
            for (String path : paths) {
                LOGGER.logMessage(LogLevel.DEBUG, "正在处理js文件:<{}>", path);
                outputStream.write("try{\n".getBytes());
                FileObject fileObject = fullContextFileRepository.getFileObject(path);
                InputStream stream = new BufferedInputStream(fileObject.getInputStream());
                StreamUtil.io(stream, outputStream, true, false);
                outputStream.write("\n;}catch(e){}\n".getBytes());
                LOGGER.logMessage(LogLevel.DEBUG, "js文件:<{}>处理完毕", path);
            }
        }
        if (component.getJsCodelet() != null) {
            outputStream.write(component.getJsCodelet().getBytes("UTF-8"));
        }
    }

    private void writeMergeCss(UIComponent component, ByteArrayOutputStream outputStream, String contextPath, String servletPath) throws Exception {
        String[] paths = uiComponentManager.getComponentCssArray(component);
        if (paths != null) {
            for (String path : paths) {
                LOGGER.logMessage(LogLevel.DEBUG, "正在处理css文件:<{}>", path);
                FileObject fileObject = fullContextFileRepository.getFileObject(path);
                writeCss(fileObject, outputStream, contextPath, servletPath);
                outputStream.write('\n');
                LOGGER.logMessage(LogLevel.DEBUG, "css文件:<{}>处理完毕", path);
            }
        }
        if (component.getCssCodelet() != null) {
            replaceCss(outputStream, contextPath, component.getCssCodelet(), servletPath);
        }
    }

    private void writeCss(FileObject fileObject, ByteArrayOutputStream outputStream, String contextPath, String servletPath) throws Exception {
        InputStream stream = new BufferedInputStream(fileObject.getInputStream());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        StreamUtil.io(stream, byteArrayOutputStream, true, false);
        replaceCss(outputStream, contextPath, new String(byteArrayOutputStream.toByteArray(), "UTF-8"), fileObject.getPath());
    }

    private List<FileObject> writeMergeCss(List<String> cssPaths, String contextPath, String servletPath, StoreConfig config) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write("@charset \"utf-8\";\n".getBytes());
        List<FileObject> list = new ArrayList<FileObject>();
        try {
            for (int i = 0; i < cssPaths.size(); i++) {
                String path = cssPaths.get(i);
                FileObject fileObject = fullContextFileRepository.getFileObject(path);
                if (byteArrayOutputStream.size() + fileObject.getSize() >= config.getCssLimit()) {
                    //流中的css大小已经超过限制,需要写入新的css文件
                    list.add(new SourceFileObject(createNewCssName(list, config.getMergeCssName()), byteArrayOutputStream.toByteArray()));
                    byteArrayOutputStream.close();
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    byteArrayOutputStream.write("@charset \"utf-8\";\n".getBytes());
                }

                writeCss(fileObject, byteArrayOutputStream, contextPath, servletPath);
                byteArrayOutputStream.write('\n');

                if (i == cssPaths.size() - 1) {
                    //最后一个文件强制写入
                    list.add(new SourceFileObject(createNewCssName(list, config.getMergeCssName()), byteArrayOutputStream.toByteArray()));
                }
            }
        } finally {
            byteArrayOutputStream.close();
        }

        return list;
    }

    private String createNewCssName(List<FileObject> list, String cssName) {
        int p = cssName.lastIndexOf(".");
        return cssName.substring(0, p) + (list.size() + 1) + cssName.substring(p, cssName.length());
    }

    public List<FileObject> createCSS(String contextPath, String servletPath,
                                      StoreConfig config) throws Exception {
        List<FileObject> list = new ArrayList<FileObject>();
        if (config.isMergeTag()) {
            if (config.getCssLimit() <= 0) {
                //合并输出
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                try {
                    outputStream.write("@charset \"utf-8\";\n".getBytes());
                    for (UIComponent component : uiComponentManager.getHealthUiComponents()) {
                        writeMergeCss(component, outputStream, contextPath, servletPath);
                    }
                    list.add(new SourceFileObject(config.getMergeCssName(), outputStream.toByteArray()));
                } finally {
                    outputStream.close();
                }
            } else {
                //按大小限制合并为若干css文件
                List<String> cssPaths = new ArrayList<String>();
                for (UIComponent component : uiComponentManager.getHealthUiComponents()) {
                    String[] paths = uiComponentManager.getComponentCssArray(component);
                    if (paths != null) {
                        for (String path : paths) {
                            cssPaths.add(path);
                        }
                    }
                }
                mergeCssManager.clear();
                list = writeMergeCss(cssPaths, contextPath, servletPath, config);
                for (FileObject object : list) {
                    mergeCssManager.addCssResource(object.getPath());
                    LOGGER.logMessage(LogLevel.DEBUG, "正在处理css合并文件:<{}>,合并大小:{}B", object.getPath(), object.getSize());
                }
            }

        } else {
            //单独输出
            for (UIComponent component : uiComponentManager.getHealthUiComponents()) {
                String[] paths = uiComponentManager.getComponentCssArray(component);
                if (paths != null) {
                    for (String path : paths) {
                        LOGGER.logMessage(LogLevel.DEBUG, "正在处理css文件:<{}>", path);
                        //不直接返回fullContextFileRepository的FileObject避免最后统一清理了。
                        FileObject fileObject = fullContextFileRepository.getFileObject(path);
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        try {
                            InputStream stream = new BufferedInputStream(fileObject.getInputStream());
                            StreamUtil.io(stream, outputStream, true, false);
                            list.add(new SourceFileObject(fileObject.getPath(), outputStream.toByteArray()));
                        } finally {
                            outputStream.close();
                        }
                        LOGGER.logMessage(LogLevel.DEBUG, "css文件:<{}>处理完毕", path);
                    }
                }
            }
        }
        return list;
    }

    private String convertUrl(String contextPath, String url, String servletPath) {
        if (contextPath == null) {
            contextPath = "";
        }
        if (url.startsWith("/") || url.startsWith("\\")) {
            return contextPath + url;
        } else if (url.startsWith("../") || url.startsWith("..\\")) {
            String firstThree = url.substring(0, 3);
            int count = 0;
            while (url.startsWith(firstThree)) {
                count++;
                url = url.substring(3);
            }
            String[] paths = servletPath.split("/");
            StringBuffer sb = new StringBuffer(contextPath);
            for (int i = 0; i < paths.length - count - 1; i++) {
                sb.append(paths[i]).append("/");
            }
            sb.append(url);
            return sb.toString();
        }
        return contextPath + servletPath.substring(0, servletPath.lastIndexOf('/') + 1) + url;
    }


    private void replaceCss(OutputStream outputStream, String contextPath, String string, String servletPath)
            throws IOException {
        Matcher matcher = urlPattern.matcher(string);
        int curpos = 0;
        while (matcher.find()) {
            outputStream.write(string.substring(curpos, matcher.start()).getBytes("UTF-8"));
            if (matcher.group(2).trim().startsWith("data:")) {
                outputStream.write(matcher.group().getBytes("UTF-8"));
            } else {
                outputStream.write(matcher.group(1).getBytes("UTF-8"));
                outputStream.write(convertUrl(contextPath, matcher.group(2), servletPath).getBytes("UTF-8"));
                outputStream.write(matcher.group(3).getBytes("UTF-8"));
            }
            curpos = matcher.end();
            continue;
        }
        outputStream.write(string.substring(curpos).getBytes("UTF-8"));
    }

}
