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
package com.xquant.configuration;


import com.xquant.xmlparser.node.XmlNode;

/**
 * 所有需要进行应用配置统一管理的类，都推荐实现此接口。
 * 通过此接口，可以由框架自动注入配置信息，且在配置进行刷新的时候，自动推送参数到应用，以便及时做出更新。
 *
 * @author luoguo
 */
public interface Configuration {
    /**
     * 获取在application.xml中配置对象的相对路径
     *
     * @return
     */
    String getApplicationNodePath();

    /**
     * 返回该配置对象组件配置的相对路径，相对于classpath的路径。
     *
     * @return 组件配置的相对路径
     */
    String getComponentConfigPath();

    /**
     * 设置配置信息,应用配置和组件配置内容合并
     *
     * @param applicationConfig 应用配置节点
     * @param componentConfig   组件配置节点
     */
    void config(XmlNode applicationConfig, XmlNode componentConfig);

    /**
     * 获取组件配置信息
     *
     * @return
     */
    XmlNode getComponentConfig();

    /**
     * 获取应用配置信息
     *
     * @return
     */
    XmlNode getApplicationConfig();


}
