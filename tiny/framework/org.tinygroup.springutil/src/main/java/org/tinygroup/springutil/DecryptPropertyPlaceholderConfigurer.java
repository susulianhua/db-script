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
package org.tinygroup.springutil;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.tinygroup.commons.cryptor.Cryptor;
import org.tinygroup.commons.cryptor.DefaultCryptor;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.exception.BaseRuntimeException;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * 有解密功能的属性占位符
 *
 * @author renhui
 */
public class DecryptPropertyPlaceholderConfigurer extends
        PropertyPlaceholderConfigurer implements InitializingBean {

    private static final String SPLIT = ",";
    private static final String PASSWORD = "tiny.password";
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DecryptPropertyPlaceholderConfigurer.class);
    /**
     * 需要进行解密的属性名，多个以逗号进行分隔
     */
    private String decryptPropertyNames;
    private List<String> decrptList = new ArrayList<String>();
    private Cryptor cryptor;
    private String seed;

    public String getDecryptPropertyNames() {
        return decryptPropertyNames;
    }

    public void setDecryptPropertyNames(String decryptPropertyNames) {
        this.decryptPropertyNames = decryptPropertyNames;
    }

    public Cryptor getCryptor() {
        return cryptor;
    }

    public void setCryptor(Cryptor cryptor) {
        this.cryptor = cryptor;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    @Override
    protected Properties mergeProperties() throws IOException {
        Properties properties = new Properties();
        Map<String, String> configMap = ConfigurationUtil
                .getConfigurationManager().getConfiguration();
        if (configMap != null) {
            for (String key : configMap.keySet()) {
                String value = configMap.get(key);
                properties.put(key, value);
            }
        }
        Properties mergeProperties = super.mergeProperties();
        if (mergeProperties != null) {
            properties.putAll(mergeProperties);
            for (Object key : mergeProperties.keySet()) {
                configMap.put(key.toString(), mergeProperties.get(key).toString());
            }
        }
        return properties;
    }

    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        if (decrptList.contains(propertyName)) {
            LOGGER.logMessage(LogLevel.DEBUG, "propertyName:[{0}],需要进行解密操作",
                    propertyName);
            try {
                if (StringUtil.isBlank(seed)) {
                    return cryptor.decrypt(propertyValue);
                } else {
                    return cryptor.decrypt(propertyValue, seed);
                }
            } catch (Exception e) {
                LOGGER.logMessage(LogLevel.ERROR,
                        "propertyName:[{0}],解密操作出现异常", e, propertyName);
                throw new BaseRuntimeException(e);
            } finally {
                LOGGER.logMessage(LogLevel.DEBUG, "propertyName:[{0}],解密操作完成",
                        propertyName);
            }
        }
        return super.convertProperty(propertyName, propertyValue);
    }

    public void afterPropertiesSet() throws Exception {
        if (StringUtil.isBlank(decryptPropertyNames)) {
            decryptPropertyNames = PASSWORD;
        }
        Collections.addAll(decrptList, decryptPropertyNames.split(SPLIT));
        if (cryptor == null) {
            cryptor = new DefaultCryptor();
        }
    }

}
