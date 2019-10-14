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
package org.tinygroup.service.release.fileresolver;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.fileresolver.FileProcessor;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.service.Service;
import org.tinygroup.service.release.ServiceReleaseManager;
import org.tinygroup.service.release.config.ServiceRelease;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import java.io.InputStream;

/**
 * @author yanwj
 */
public class ServiceReleaseProcessor extends AbstractFileProcessor implements FileProcessor {

    private static final String SERVICE_RELEASE_EXTENSION = ".servicerelease.xml";

    public void process() {
        XStream stream = XStreamFactory
                .getXStream(Service.SERVICE_XSTREAM_PACKAGENAME);
        for (FileObject fileObject : changeList) {
            LOGGER.logMessage(LogLevel.INFO, "正在读取servicerelease文件[{0}]",
                    fileObject.getAbsolutePath());
            try {
                InputStream inputStream = fileObject.getInputStream();
                ServiceRelease filter = (ServiceRelease) stream
                        .fromXML(inputStream);
                try {
                    inputStream.close();
                } catch (Exception e) {
                    LOGGER.errorMessage("关闭文件流时出错,文件路径:{}", e,
                            fileObject.getAbsolutePath());
                }
                try {
                    LOGGER.logMessage(LogLevel.INFO, "正在加载servicerelease");
                    ServiceReleaseManager.add(filter);
                    LOGGER.logMessage(LogLevel.INFO, "加载servicerelease结束");
                } catch (Exception e) {
                    LOGGER.errorMessage("加载servicerelease时出现异常", e);
                }

            } catch (Exception e) {
                LOGGER.errorMessage("加载servicerelease文件[{0}]出现异常", e,
                        fileObject.getAbsolutePath());
            }

            LOGGER.logMessage(LogLevel.INFO, "读取servicerelease文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
    }

    @Override
    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(SERVICE_RELEASE_EXTENSION);
    }
}
