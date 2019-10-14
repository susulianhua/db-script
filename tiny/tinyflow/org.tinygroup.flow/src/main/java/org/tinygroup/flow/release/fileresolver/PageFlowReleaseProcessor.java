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
/**
 *
 */
package org.tinygroup.flow.release.fileresolver;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.fileresolver.FileProcessor;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.flow.release.PageFlowReleaseManager;
import org.tinygroup.flow.release.config.PageFlowRelease;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import java.io.InputStream;

/**
 * @author yanwj
 */
public class PageFlowReleaseProcessor extends AbstractFileProcessor implements FileProcessor {

    private static final String PAGEFLOW_RELEASE_EXTENSION = ".pageflowrelease.xml";

    private static final String PAGEFLOW_XSTREAM_PACKAGENAME = "flow";

    public void process() {
        PageFlowReleaseManager.clear();
        XStream stream = XStreamFactory
                .getXStream(PAGEFLOW_XSTREAM_PACKAGENAME);
        for (FileObject fileObject : changeList) {
            LOGGER.logMessage(LogLevel.INFO, "正在读取flowrelease文件[{0}]",
                    fileObject.getAbsolutePath());
            try {
                InputStream inputStream = fileObject.getInputStream();
                PageFlowRelease filter = (PageFlowRelease) stream
                        .fromXML(inputStream);
                try {
                    inputStream.close();
                } catch (Exception e) {
                    LOGGER.errorMessage("关闭文件流时出错,文件路径:{}", e,
                            fileObject.getAbsolutePath());
                }
                try {
                    LOGGER.logMessage(LogLevel.INFO, "正在加载flowrelease");
                    PageFlowReleaseManager.add(filter);
                    LOGGER.logMessage(LogLevel.INFO, "加载flowrelease结束");
                } catch (Exception e) {
                    LOGGER.errorMessage("加载flowrelease时出现异常", e);
                }

            } catch (Exception e) {
                LOGGER.errorMessage("加载flowrelease文件[{0}]出现异常", e,
                        fileObject.getAbsolutePath());
            }

            LOGGER.logMessage(LogLevel.INFO, "读取flowrelease文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
    }

    @Override
    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(PAGEFLOW_RELEASE_EXTENSION);
    }
}
