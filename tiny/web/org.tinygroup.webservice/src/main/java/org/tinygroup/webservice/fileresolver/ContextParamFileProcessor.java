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
package org.tinygroup.webservice.fileresolver;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.webservice.config.ContextParams;
import org.tinygroup.webservice.manager.ContextParamManager;
import org.tinygroup.xstream.XStreamFactory;

import java.io.InputStream;

public class ContextParamFileProcessor extends AbstractFileProcessor {

    private static final String CONTEXT_PARAM_EXT_FILENAME = ".contextparam.xml";

    private ContextParamManager contextParamManager;


    public ContextParamManager getContextParamManager() {
        return contextParamManager;
    }

    public void setContextParamManager(ContextParamManager contextParamManager) {
        this.contextParamManager = contextParamManager;
    }

    public void process() {
        XStream stream = XStreamFactory
                .getXStream(ContextParamManager.XSTEAM_PACKAGE_NAME);
        for (FileObject fileObject : deleteList) {
            LOGGER.logMessage(LogLevel.INFO, "正在移除webservice参数配置文件[{0}]",
                    fileObject.getAbsolutePath());
            ContextParams contextParams = (ContextParams) caches.get(fileObject.getAbsolutePath());
            if (contextParams != null) {
                contextParamManager.removeContextParams(contextParams);
                caches.remove(fileObject.getAbsolutePath());
            }
            LOGGER.logMessage(LogLevel.INFO, "移除webservice参数配置文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
        for (FileObject fileObject : changeList) {
            LOGGER.logMessage(LogLevel.INFO, "正在加载webservice参数配置文件[{0}]",
                    fileObject.getAbsolutePath());
            ContextParams oldMatchers = (ContextParams) caches.get(fileObject.getAbsolutePath());
            if (oldMatchers != null) {
                contextParamManager.removeContextParams(oldMatchers);
            }
            InputStream inputStream = fileObject.getInputStream();
            ContextParams matchers = (ContextParams) stream
                    .fromXML(inputStream);
            try {
                inputStream.close();
            } catch (Exception e) {
                LOGGER.errorMessage("关闭文件流时出错,文件路径:{}", e, fileObject.getAbsolutePath());
            }
            contextParamManager.addContextParams(matchers);
            caches.put(fileObject.getAbsolutePath(), matchers);
            LOGGER.logMessage(LogLevel.INFO, "加载webservice参数配置文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
    }

    @Override
    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(CONTEXT_PARAM_EXT_FILENAME);
    }

}
