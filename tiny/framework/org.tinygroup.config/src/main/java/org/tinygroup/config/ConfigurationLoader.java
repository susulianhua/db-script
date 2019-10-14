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
package org.tinygroup.config;

import org.tinygroup.xmlparser.node.XmlNode;

import java.util.Map;

/**
 * 用于载入配置
 * Created by luoguo on 2014/5/14.
 */
public interface ConfigurationLoader {
    /**
     * 用于载入应用配置
     *
     * @return 返回应用配置节点
     */
    XmlNode loadApplicationConfiguration();

    /**
     * 用于载入组件配置
     *
     * @return 返回多个组件配置组成的map
     */
    Map<String, XmlNode> loadComponentConfiguration();

}
