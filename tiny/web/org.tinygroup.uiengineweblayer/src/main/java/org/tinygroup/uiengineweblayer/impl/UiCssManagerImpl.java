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
package org.tinygroup.uiengineweblayer.impl;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.fileresolver.FullContextFileRepository;
import org.tinygroup.uiengine.config.UIComponent;
import org.tinygroup.uiengine.manager.UIComponentManager;
import org.tinygroup.uiengineweblayer.UiCssInfo;
import org.tinygroup.uiengineweblayer.UiCssManager;
import org.tinygroup.uiengineweblayer.UiEngineUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.weblayer.listener.ServletContextHolder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UiCssManagerImpl implements UiCssManager {

    private Map<String, UiCssInfo> maps = new LinkedHashMap<String, UiCssInfo>();
    private String pathPrefix;
    private UIComponentManager uiComponentManager;
    private FullContextFileRepository fullContextFileRepository;
    private String paramterName;
    private String cssName;
    private long cssLimit;

    public List<String> getCssPaths() {
        return new ArrayList<String>(maps.keySet());
    }

    public UiCssInfo getUiCssInfo(String path) {
        return maps.get(path);
    }

    protected String getPath(int no) {
        return pathPrefix + no;
    }

    public UiCssInfo getUiCssInfo(int no) {
        return getUiCssInfo(getPath(no));
    }

    public void addUiCssInfo(UiCssInfo info) {
        maps.put(info.getPath(), info);
    }

    public void removeUiCssInfo(String path) {
        maps.remove(path);
    }

    public void removeUiCssInfo(int no) {
        maps.remove(getPath(no));
    }

    public void clear() {
        maps.clear();
    }

    public String getParamterName() {
        if (paramterName == null) {
            return "cssno";
        }
        return paramterName;
    }

    public void setParamterName(String paramterName) {
        this.paramterName = paramterName;
    }

    public long getCssLimit() {
        if (cssLimit <= 0) {
            return 50000;
        }
        return cssLimit;
    }


    public void setCssLimit(long cssLimit) {
        this.cssLimit = cssLimit;
    }

    public String getCssName() {
        if (cssName == null) {
            return "uiengine.uicss";
        }
        return cssName;
    }

    public void setCssName(String cssName) {
        this.cssName = cssName;
    }

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

    public void createDynamicCss() throws IOException {
        createDynamicCss(ServletContextHolder.getServletContext().getContextPath(), "/" + getCssName());
    }

    public void createDynamicCss(String contextPath, String servletPath)
            throws IOException {
        pathPrefix = createPathPrefix(contextPath, servletPath);

        List<String> cssPaths = new ArrayList<String>();
        for (UIComponent component : uiComponentManager.getHealthUiComponents()) {
            String[] paths = uiComponentManager.getComponentCssArray(component);
            if (paths != null) {
                for (String path : paths) {
                    cssPaths.add(path);
                }
            }
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write("@charset \"utf-8\";\n".getBytes());
        int count = 0;
        try {
            for (int i = 0; i < cssPaths.size(); i++) {
                String path = cssPaths.get(i);
                FileObject fileObject = fullContextFileRepository
                        .getFileObject(path);
                if (byteArrayOutputStream.size() + fileObject.getSize() >= cssLimit) {
                    // 流中的css大小已经超过限制,需要写入新的css文件
                    count++;
                    UiCssInfo info = new UiCssInfo(createNewCssName(count),
                            byteArrayOutputStream.toByteArray());
                    addUiCssInfo(info);

                    byteArrayOutputStream.close();
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    byteArrayOutputStream.write("@charset \"utf-8\";\n"
                            .getBytes());
                }

                UiEngineUtil.writeCss(fileObject, byteArrayOutputStream,
                        contextPath, servletPath);
                byteArrayOutputStream.write('\n');

                if (i == cssPaths.size() - 1) {
                    // 最后一个文件强制写入
                    count++;
                    UiCssInfo info = new UiCssInfo(createNewCssName(count),
                            byteArrayOutputStream.toByteArray());
                    addUiCssInfo(info);
                }
            }
        } finally {
            byteArrayOutputStream.close();
        }
    }

    private String createNewCssName(int count) {
        return pathPrefix + count;
    }

    private String createPathPrefix(String contextPath, String servletPath) {
        pathPrefix = "";
        if (!StringUtil.isBlank(contextPath)) {
            if (!contextPath.startsWith("/")) {
                pathPrefix = "/" + contextPath;
            } else {
                pathPrefix = contextPath;
            }
        }
        if (!StringUtil.isBlank(servletPath)) {
            if (!servletPath.startsWith("/")) {
                pathPrefix = pathPrefix + "/" + servletPath;
            } else {
                pathPrefix = pathPrefix + servletPath;
            }
        }
        pathPrefix = pathPrefix + "?" + getParamterName() + "=";
        return pathPrefix;
    }


}
