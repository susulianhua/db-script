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
package org.tinygroup.xmlsignature;

import org.tinygroup.xmlsignature.config.XmlSignatureConfig;
import org.tinygroup.xmlsignature.config.XmlSignatureConfigs;

import java.security.KeyPair;
import java.util.List;

/**
 * XML数字签名管理器
 *
 * @author yancheng11334
 */
public interface XmlSignatureManager {

    public static final String DEFAULT_BAEN_NAME = "xmlSignatureManager";

    /**
     * 添加XML数字签名配置项
     *
     * @param config
     */
    void addXmlSignatureConfig(XmlSignatureConfig config);

    /**
     * 删除XML数字签名配置项
     *
     * @param userId
     */
    void removeXmlSignatureConfig(String userId);

    /**
     * 读取XML数字签名配置项
     *
     * @param userId
     * @return
     */
    XmlSignatureConfig getXmlSignatureConfig(String userId);

    /**
     * 获得XML数字签名配置项列表
     *
     * @return
     */
    List<XmlSignatureConfig> getXmlSignatureConfigList();

    /**
     * 根据配置项获得公钥私钥对
     *
     * @param userId
     * @return
     */
    KeyPair getKeyPair(String userId);

    /**
     * 添加XML数字签名配置组
     *
     * @param configs
     */
    void addXmlSignatureConfigs(XmlSignatureConfigs configs);

    /**
     * 删除XML数字签名配置组
     *
     * @param configs
     */
    void removeXmlSignatureConfigs(XmlSignatureConfigs configs);
}
