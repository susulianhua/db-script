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
package org.tinygroup.servicehttpchannel.fileprocessor;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.service.Service;
import org.tinygroup.service.config.ServiceComponents;
import org.tinygroup.servicehttpchannel.ServiceHttpChannelLoader;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ServiceHttpChannelFileProcessor extends AbstractFileProcessor {
    private static final String MOCK_SERVICE_EXT_FILENAME = ".service.xml";
    private List<ServiceComponents> list = new ArrayList<ServiceComponents>();
    private ServiceHttpChannelLoader loader;


    public ServiceHttpChannelLoader getLoader() {
        return loader;
    }

    public void setLoader(ServiceHttpChannelLoader loader) {
        this.loader = loader;
    }

    public void process() {
        XStream stream = XStreamFactory
                .getXStream(Service.SERVICE_XSTREAM_PACKAGENAME);

        for (FileObject fileObject : fileObjects) {
            LOGGER.logMessage(LogLevel.INFO, "正在读取Service文件[{0}]",
                    fileObject.getAbsolutePath());
            try {
                InputStream inputStream = fileObject.getInputStream();
                ServiceComponents components = (ServiceComponents) stream
                        .fromXML(inputStream);
                try {
                    inputStream.close();
                } catch (Exception e) {
                    LOGGER.errorMessage("关闭文件流时出错,文件路径:{}", e,
                            fileObject.getAbsolutePath());
                }
                list.add(components);
                caches.put(fileObject.getAbsolutePath(), components);
            } catch (Exception e) {
                LOGGER.errorMessage("读取Service文件[{0}]出现异常", e,
                        fileObject.getAbsolutePath());
            }
            LOGGER.logMessage(LogLevel.INFO, "读取Service文件[{0}]结束",
                    fileObject.getAbsolutePath());
            loader.addService(list);
        }

        list.clear();// 扫描结束后清空服务列表

    }

    @Override
    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(MOCK_SERVICE_EXT_FILENAME);
    }

}
