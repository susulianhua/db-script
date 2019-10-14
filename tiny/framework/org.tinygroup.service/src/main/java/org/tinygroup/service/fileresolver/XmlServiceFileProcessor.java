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
package org.tinygroup.service.fileresolver;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.beancontainer.BeanContainer;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.fileresolver.FileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.service.Service;
import org.tinygroup.service.ServiceProviderInterface;
import org.tinygroup.service.config.ServiceComponent;
import org.tinygroup.service.config.ServiceComponents;
import org.tinygroup.service.config.XmlConfigClassServiceLoader;
import org.tinygroup.service.config.XmlConfigServiceLoader;
import org.tinygroup.service.exception.ServiceLoadException;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlServiceFileProcessor extends XmlConfigClassServiceLoader implements
        FileProcessor {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(XmlServiceFileProcessor.class);
    private static final String SERVICE_EXT_FILENAME = ".service.xml";
    private ServiceProviderInterface provider;
    private List<ServiceComponents> list = new ArrayList<ServiceComponents>();

    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(SERVICE_EXT_FILENAME);
    }

    public ServiceProviderInterface getProvider() {
        return provider;
    }

    public void setProvider(ServiceProviderInterface provider) {
        this.provider = provider;
    }

    public void process() {
        XStream stream = XStreamFactory
                .getXStream(Service.SERVICE_XSTREAM_PACKAGENAME);
        for (FileObject fileObject : deleteList) {
            LOGGER.logMessage(LogLevel.INFO, "正在移除Service文件[{0}]",
                    fileObject.getAbsolutePath());
            ServiceComponents components = (ServiceComponents) caches
                    .get(fileObject.getAbsolutePath());
            if (components != null) {
                // list.remove(components);
                caches.remove(fileObject.getAbsolutePath());
            }
            removeServiceComponents(provider.getServiceRegistory(), components);
            LOGGER.logMessage(LogLevel.INFO, "移除Service文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }

        for (FileObject fileObject : changeList) {
            LOGGER.logMessage(LogLevel.INFO, "正在读取Service文件[{0}]",
                    fileObject.getAbsolutePath());
            try {
                ServiceComponents oldComponents = (ServiceComponents) caches
                        .get(fileObject.getAbsolutePath());
                if (oldComponents != null) {
                    list.remove(oldComponents);
                }
                InputStream inputStream = fileObject.getInputStream();
                ServiceComponents components = (ServiceComponents) stream
                        .fromXML(inputStream);
                try {
                    inputStream.close();
                } catch (Exception e) {
                    LOGGER.errorMessage("关闭文件流时出错,文件路径:{}", e, fileObject.getAbsolutePath());
                }
                list.add(components);
                caches.put(fileObject.getAbsolutePath(), components);
            } catch (Exception e) {
                LOGGER.errorMessage("读取Service文件[{0}]出现异常", e,
                        fileObject.getAbsolutePath());
            }

            LOGGER.logMessage(LogLevel.INFO, "读取Service文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
        try {
            LOGGER.logMessage(LogLevel.INFO, "正在注册Service");
            this.loadService(provider.getServiceRegistory(), getFileResolver()
                    .getClassLoader());
            LOGGER.logMessage(LogLevel.INFO, "注册Service结束");
        } catch (ServiceLoadException e) {
            LOGGER.errorMessage("注册Service时出现异常", e);
        }
        list.clear();// 扫描结束后清空服务列表

    }

    public void setConfigPath(String path) {

    }

    protected List<ServiceComponents> getServiceComponents() {
        return list;
    }

    protected Object getServiceInstance(ServiceComponent component)
            throws Exception {
        BeanContainer<?> container = BeanContainerFactory
                .getBeanContainer(getFileResolver().getClassLoader());
        // 如果没有定义bean ID
        try {
            if (component.getBean() == null || "".equals(component.getBean())) {
                // 20141023 为bundle修改
                Class<?> clazz = getFileResolver().getClassLoader().loadClass(
                        component.getType());
                // Class<?> clazz = Class.forName(component.getType());
                return container.getBean(clazz);
            }
            return container.getBean(component.getBean());
        } catch (Exception e) {
            if (component.getBean() == null || "".equals(component.getBean())) {
                LOGGER.logMessage(LogLevel.WARN, "查找Bean：{}时发生异常", e, component.getType());
            } else {
                LOGGER.logMessage(LogLevel.WARN, "查找Bean：{}时发生异常", e, component.getBean());
            }

            Class<?> clazz = Class.forName(component.getType());
            if (!clazz.isInterface()) {
                return clazz.newInstance();
            } else {
                throw e;
            }
        }
    }
}
