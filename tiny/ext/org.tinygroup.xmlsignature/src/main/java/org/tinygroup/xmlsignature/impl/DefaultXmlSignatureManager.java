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
package org.tinygroup.xmlsignature.impl;


import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;
import org.tinygroup.xmlsignature.XmlSignatureManager;
import org.tinygroup.xmlsignature.config.XmlSignatureConfig;
import org.tinygroup.xmlsignature.config.XmlSignatureConfigs;

import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 默认的XML数字签名配置管理器
 *
 * @author yancheng11334
 */
public class DefaultXmlSignatureManager implements XmlSignatureManager {

    private Map<String, XmlSignatureConfig> configMaps = new HashMap<String, XmlSignatureConfig>();
    private Map<String, KeyPair> keyPairMaps = new HashMap<String, KeyPair>();

    private Logger LOGGER = LoggerFactory.getLogger(DefaultXmlSignatureManager.class);

    public void addXmlSignatureConfig(XmlSignatureConfig config) {
        if (config != null) {
            configMaps.put(config.getUserId(), config);
        }
    }

    public void removeXmlSignatureConfig(String userId) {
        configMaps.remove(userId);
        keyPairMaps.remove(userId);
    }

    public XmlSignatureConfig getXmlSignatureConfig(String userId) {
        return configMaps.get(userId);
    }

    public void addXmlSignatureConfigs(XmlSignatureConfigs configs) {
        if (configs != null && configs.getXmlSignatureConfigList() != null) {
            for (XmlSignatureConfig config : configs.getXmlSignatureConfigList()) {
                addXmlSignatureConfig(config);
            }
        }
    }

    public void removeXmlSignatureConfigs(XmlSignatureConfigs configs) {
        if (configs != null && configs.getXmlSignatureConfigList() != null) {
            for (XmlSignatureConfig config : configs.getXmlSignatureConfigList()) {
                removeXmlSignatureConfig(config.getUserId());
            }
        }
    }

    public KeyPair getKeyPair(String userId) {
        try {
            //如果存在KeyPair,直接返回
            KeyPair keyPair = keyPairMaps.get(userId);
            if (keyPair != null) {
                return keyPair;
            }

            XmlSignatureConfig config = configMaps.get(userId);
            if (config != null) {
                //创建keyPair
                keyPair = loadKeyPair(config);
                keyPairMaps.put(userId, keyPair);
                return keyPair;
            }
        } catch (Exception e) {
            LOGGER.errorMessage(String.format("根据[%s]初始化密钥对失败", userId), e);
        }

        return null;
    }

    //根据PublicKey和PrivateKey创建密钥对
    private KeyPair loadKeyPair(XmlSignatureConfig config) throws Exception {
        try {
            LOGGER.logMessage(LogLevel.DEBUG, "开始加载PrivateKey信息...");
            PrivateKey privateKey = loadPrivateKey(config);
            LOGGER.logMessage(LogLevel.DEBUG, "加载PrivateKey信息成功");
            LOGGER.logMessage(LogLevel.DEBUG, "开始加载PublicKey信息...");
            PublicKey publicKey = loadPublicKey(config);
            LOGGER.logMessage(LogLevel.DEBUG, "加载PublicKey信息成功");
            return new KeyPair(publicKey, privateKey);
        } catch (Exception e) {
            throw new Exception(String.format("根据配置项[%s]生成KeyPair失败", config.toString()), e);
        }
    }

    //装载公钥
    private PublicKey loadPublicKey(XmlSignatureConfig config) throws Exception {
        String storeType = StringUtil.isEmpty(config.getPublicStoreType()) ? "X.509" : config.getPublicStoreType();
        CertificateFactory cf = CertificateFactory.getInstance(storeType);
        FileObject file = VFS.resolveFile(config.getPublicKeyPath());
        if (!file.isExist()) {
            throw new Exception(String.format("[%s]没有找到匹配的公钥，请检查配置", config.getPublicKeyPath()));
        }
        Certificate c = cf.generateCertificate(file.getInputStream());
        return c.getPublicKey();
    }

    //装载私钥
    private PrivateKey loadPrivateKey(XmlSignatureConfig config) throws Exception {
        String storeType = StringUtil.isEmpty(config.getPrivateStoreType()) ? KeyStore.getDefaultType() : config.getPrivateStoreType();
        KeyStore keyStore = KeyStore.getInstance(storeType);
        FileObject file = VFS.resolveFile(config.getPrivateKeyPath());
        if (!file.isExist()) {
            throw new Exception(String.format("[%s]没有找到匹配的私钥，请检查配置", config.getPrivateKeyPath()));
        }
        char[] password = config.getPassword().toCharArray();
        keyStore.load(file.getInputStream(), password);
        return (PrivateKey) keyStore.getKey(config.getAlias(), password);
    }

    public List<XmlSignatureConfig> getXmlSignatureConfigList() {
        return new ArrayList<XmlSignatureConfig>(configMaps.values());
    }

}
