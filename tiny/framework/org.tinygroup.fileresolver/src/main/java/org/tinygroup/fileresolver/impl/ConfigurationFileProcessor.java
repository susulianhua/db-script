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
package org.tinygroup.fileresolver.impl;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.config.Configuration;
import org.tinygroup.config.ConfigurationManager;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xmlparser.node.XmlNode;

import java.io.IOException;

public class ConfigurationFileProcessor extends AbstractFileProcessor {

    private static final String CONFIG_EXT_FILENAME = ".config.xml";

    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().toLowerCase()
                .endsWith(CONFIG_EXT_FILENAME);
    }

    public void process() {
        ConfigurationManager configurationManager = ConfigurationUtil
                .getConfigurationManager();
        if (!CollectionUtil.isEmpty(deleteList)
                || !CollectionUtil.isEmpty(changeList)) {
            for (FileObject fileObject : deleteList) {
                LOGGER.logMessage(LogLevel.INFO, "正在移除组件配置文件[{0}]",
                        fileObject.getAbsolutePath());
                configurationManager.getComponentConfigurationMap().remove(
                        fileObject.getPath());
                LOGGER.logMessage(LogLevel.INFO, "移除组件配置文件[{0}]结束",
                        fileObject.getAbsolutePath());
            }
            for (FileObject fileObject : changeList) {
                LOGGER.logMessage(LogLevel.INFO, "开始读取组件配置文件:{0}",
                        fileObject.getFileName());
                try {
                    XmlNode xmlNode = ConfigurationUtil
                            .parseXmlFromFileObject(fileObject);
                    configurationManager.setComponentConfiguration(
                            fileObject.getPath(), xmlNode);
                    LOGGER.logMessage(LogLevel.INFO, "读取组件配置文件:{0}完成",
                            fileObject.getFileName());
                } catch (IOException e) {
                    LOGGER.errorMessage("读取组件配置文件:{0}时出现异常！", e,
                            fileObject.getFileName());
                }
            }
            configurationManager.setConfigurationList(BeanContainerFactory
                    .getBeanContainer(this.getClass().getClassLoader())
                    .getBeans(Configuration.class));
            configurationManager.distributeConfiguration();// 重新分发配置信息
        }

    }

    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

}
