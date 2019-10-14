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
package org.tinygroup.flow.util;

import org.tinygroup.config.impl.AbstractConfiguration;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.parser.filter.NameFilter;
import org.tinygroup.xmlparser.node.XmlNode;

import java.util.List;

public class FlowUtilConfig extends AbstractConfiguration {

    private static final String FLOW_EL_UTIL_CONFIG_PATH = "/application/el-util-config";

    public String getApplicationNodePath() {
        return FLOW_EL_UTIL_CONFIG_PATH;
    }

    public String getComponentConfigPath() {
        return "";
    }

    public void config(XmlNode applicationConfig, XmlNode componentConfig) {
        super.config(applicationConfig, componentConfig);
        XmlNode combineNode = ConfigurationUtil.combineXmlNode(
                applicationConfig, componentConfig);
        init(combineNode);
    }

    public void init(XmlNode config) {
        if (config == null) {
            LOGGER.logMessage(LogLevel.INFO, "工具类配置信息为空");
            return;
        }
        LOGGER.logMessage(LogLevel.INFO, "开始初始化工具类配置");
        NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(config);
        List<XmlNode> aopList = nameFilter.findNodeList("el-util");
        for (XmlNode node : aopList) {
            String name = node.getAttribute("name");
            String classPath = node.getAttribute("class-path");
            Class<?> clazz;
            try {
                clazz = Class.forName(classPath);
            } catch (ClassNotFoundException e) {
                LOGGER.logMessage(LogLevel.ERROR, "初始化工具类失败：{0}", classPath);
                continue;
            }
            FlowElUtil.putUtilClass(name, clazz);

        }
        LOGGER.logMessage(LogLevel.INFO, "初始化工具类配置完成");

    }

}
