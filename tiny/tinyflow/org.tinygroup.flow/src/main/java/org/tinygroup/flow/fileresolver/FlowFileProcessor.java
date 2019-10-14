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
import org.tinygroup.flow.config.Flow;
import org.tinygroup.flow.release.FlowReleaseManager;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import java.io.InputStream;
import java.util.List;

public class FlowFileProcessor extends AbstractFileProcessor {
    private static final String FLOW_EXT_FILENAME = ".flow";
    private FlowExecutor flowExecutor;


    public FlowExecutor getFlowExecutor() {
        return flowExecutor;
    }

    public void setFlowExecutor(FlowExecutor flowExecutor) {
        this.flowExecutor = flowExecutor;
    }

    public List<FileObject> getFlowFiles() {
        return fileObjects;
    }

    protected boolean checkMatch(FileObject fileObject) {
        return !fileObject.isFolder() && fileObject.getFileName().endsWith(FLOW_EXT_FILENAME);
    }

    public void process() {
        XStream stream = XStreamFactory
                .getXStream(FlowExecutor.FLOW_XSTREAM_PACKAGENAME);
        for (FileObject fileObject : deleteList) {
            LOGGER.logMessage(LogLevel.INFO, "正在删除逻辑流程flow文件[{0}]",
                    fileObject.getAbsolutePath());
            Flow flow = (Flow) caches.get(fileObject.getAbsolutePath());
            if (flow != null) {
                flowExecutor.removeFlow(flow);
                caches.remove(fileObject.getAbsolutePath());
            }
            LOGGER.logMessage(LogLevel.INFO, "删除逻辑流程flow文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
        for (FileObject fileObject : changeList) {
            LOGGER.logMessage(LogLevel.INFO, "正在读取逻辑流程flow文件[{0}]",
                    fileObject.getAbsolutePath());
            Flow oldFlow = (Flow) caches.get(fileObject.getAbsolutePath());
            if (oldFlow != null) {
                flowExecutor.removeFlow(oldFlow);
            }
            InputStream inputStream = fileObject.getInputStream();
            Flow flow = (Flow) stream.fromXML(inputStream);
            try {
                inputStream.close();
            } catch (Exception e) {
                LOGGER.errorMessage("关闭文件流时出错,文件路径:{}", e, fileObject.getAbsolutePath());
            }
            if (!FlowReleaseManager.isAccept(flow.getId())) {
                LOGGER.logMessage(LogLevel.INFO, "过滤flow: {0}", flow.getId());
                continue;
            }
            flowExecutor.addFlow(flow);
            caches.put(fileObject.getAbsolutePath(), flow);
            LOGGER.logMessage(LogLevel.INFO, "读取逻辑流程flow文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
        flowExecutor.assemble();
    }

    public void setFileResolver(FileResolver fileResolver) {

    }

}
