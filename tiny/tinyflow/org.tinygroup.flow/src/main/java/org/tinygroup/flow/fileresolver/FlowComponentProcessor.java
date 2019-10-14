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
package org.tinygroup.flow.fileresolver;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.fileresolver.FileResolver;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.flow.FlowExecutor;
import org.tinygroup.flow.config.ComponentDefines;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import java.io.InputStream;

/**
 * 扫描组件的文件处理器
 *
 * @author renhui
 */
public class FlowComponentProcessor extends AbstractFileProcessor {

    /**
     * 扫描的文件后缀
     */
    private static final String FLOW_COMPONENT_EXT_FILENAME = ".fc.xml";
    private static final String FLOW_COMPONENT_EXT_FILENAME2 = ".fc";
    private FlowExecutor flowExecutor;

    public FlowExecutor getFlowExecutor() {
        return flowExecutor;
    }

    public void setFlowExecutor(FlowExecutor flowExecutor) {
        this.flowExecutor = flowExecutor;
    }

    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(FLOW_COMPONENT_EXT_FILENAME) || fileObject.getFileName().endsWith(FLOW_COMPONENT_EXT_FILENAME2);
    }

    public void process() {
        XStream stream = XStreamFactory
                .getXStream(FlowExecutor.FLOW_XSTREAM_PACKAGENAME);
        for (FileObject fileObject : deleteList) {
            LOGGER.logMessage(LogLevel.INFO, "正在删除逻辑组件fc文件[{0}]",
                    fileObject.getAbsolutePath());
            ComponentDefines components = (ComponentDefines) caches
                    .get(fileObject.getAbsolutePath());
            if (components != null) {
                flowExecutor.removeComponents(components);
                caches.remove(fileObject.getAbsolutePath());
            }
            LOGGER.logMessage(LogLevel.INFO, "删除逻辑组件fc文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
        for (FileObject fileObject : changeList) {
            LOGGER.logMessage(LogLevel.INFO, "正在读取逻辑组件fc文件[{0}]",
                    fileObject.getAbsolutePath());
            ComponentDefines oldDefines = (ComponentDefines) caches.get(fileObject.getAbsolutePath());
            if (oldDefines != null) {
                flowExecutor.removeComponents(oldDefines);
            }
            InputStream inputStream = fileObject.getInputStream();
            ComponentDefines components = (ComponentDefines) stream
                    .fromXML(inputStream);
            try {
                inputStream.close();
            } catch (Exception e) {
                LOGGER.errorMessage("关闭文件流时出错,文件路径:{}", e, fileObject.getAbsolutePath());
            }
            flowExecutor.addComponents(components);
            caches.put(fileObject.getAbsolutePath(), components);
            LOGGER.logMessage(LogLevel.INFO, "读取逻辑组件fc文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }

    }

    public void setFileResolver(FileResolver fileResolver) {

    }

}
