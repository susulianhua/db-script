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
package org.tinygroup.springwithtemplate3.impl;

import org.springframework.web.servlet.view.AbstractTemplateView;
import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.fileresolver.FullContextFileRepository;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.uiengine.config.UIComponent;
import org.tinygroup.uiengine.manager.UIComponentManager;
import org.tinygroup.vfs.FileObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * 合并输出的JS视图
 * @author yancheng11334
 *
 */
public class TinyMergeJsView extends AbstractTemplateView {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(TinyMergeJsView.class);

    private UIComponentManager uiComponentManager;
    private FullContextFileRepository fullContextFileRepository;

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

    protected void renderMergedTemplateModel(Map<String, Object> model,
                                             HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        for (UIComponent component : uiComponentManager.getHealthUiComponents()) {
            writeMergeJs(component, response.getOutputStream());
        }
    }

    private void writeMergeJs(UIComponent component, OutputStream outputStream) throws Exception {
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

}
