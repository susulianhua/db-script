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
package org.tinygroup.servicewrapper.fileresolver;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.servicewrapper.ServiceWrapperConfigManager;
import org.tinygroup.servicewrapper.config.MethodConfigs;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;


public class ServiceWrapperFileProcessor extends AbstractFileProcessor {

    private static final String WRAPPER_CACHE_EXT_FILENAME = ".servicewrapper.xml";

    private ServiceWrapperConfigManager manager;

    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(WRAPPER_CACHE_EXT_FILENAME);
    }

    public void process() {
        XStream stream = XStreamFactory
                .getXStream(ServiceWrapperConfigManager.XSTEAM_PACKAGE_NAME);
        for (FileObject fileObject : deleteList) {
            LOGGER.logMessage(LogLevel.INFO, "正在移除aop服务包装配置文件[{0}]",
                    fileObject.getAbsolutePath());
            MethodConfigs aopWrappers = (MethodConfigs) caches.get(fileObject
                    .getAbsolutePath());
            if (aopWrappers != null) {
                manager.removeServiceWrappers(aopWrappers);
                caches.remove(fileObject.getAbsolutePath());
            }
            LOGGER.logMessage(LogLevel.INFO, "移除aop服务包装配置文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
        for (FileObject fileObject : changeList) {
            LOGGER.logMessage(LogLevel.INFO, "正在加载aop服务包装配置文件[{0}]",
                    fileObject.getAbsolutePath());
            MethodConfigs oldConfigs = (MethodConfigs) caches.get(fileObject
                    .getAbsolutePath());
            if (oldConfigs != null) {
                manager.removeServiceWrappers(oldConfigs);
            }
            MethodConfigs configs = convertFromXml(stream, fileObject);
            manager.addServiceWrappers(configs);
            caches.put(fileObject.getAbsolutePath(), configs);
            LOGGER.logMessage(LogLevel.INFO, "加载aop服务包装配置文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
    }

    public ServiceWrapperConfigManager getManager() {
        return manager;
    }

    public void setManager(ServiceWrapperConfigManager manager) {
        this.manager = manager;
    }

}