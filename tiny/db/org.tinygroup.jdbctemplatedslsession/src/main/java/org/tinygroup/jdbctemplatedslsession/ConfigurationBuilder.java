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
package org.tinygroup.jdbctemplatedslsession;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.jdbctemplatedslsession.exception.DslRuntimeException;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.tinysqldsl.KeyGenerator;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * 配置信息对象的构造者
 *
 * @author renhui
 */
public class ConfigurationBuilder {

    private static final String MAX_LIMIT = "max_limit";

    private static final String MAX_START = "max_start";

    private static final String DEFAULT_AUTO_GENERATOR_KEY = "default-auto-generator-key";

    private static final String GENERATOR_BEAN = "generator-bean";

    private static final String KEY_NAME = "key-name";

    private static final String KEY_GENERATOR = "key-generator";

    private static final String KEY_GENERATORS = "key-generators";

    private static final String POJO_CLASS = "pojo-class";

    private static final String TABLE_NAME = "table-name";

    private static final String AUTO_GENERATOR_KEY = "auto-generator-key";

    private static final String TABLE_CONFIGS = "table-configs";

    private static final String TABLE_CONFIG = "table-config";
    private static final String COUNT_SIGN = "can-count";
    private final Configuration configuration;
    private boolean parsed;
    private XmlNode xmlNode;
    private Logger logger = LoggerFactory.getLogger(ConfigurationBuilder.class);


    public ConfigurationBuilder(InputStream inputStream) {
        try {
            parseFromStream(inputStream);
        } catch (IOException e) {
            logger.errorMessage("载入数据库配置信息时出现异常，错误原因：{}！", e, e.getMessage());
            throw new DslRuntimeException(e);
        }
        configuration = new Configuration();
    }

    public ConfigurationBuilder(Reader reader) {
        try {
            parseFromReader(reader);
        } catch (IOException e) {
            logger.errorMessage("载入数据库配置信息时出现异常，错误原因：{}！", e, e.getMessage());
            throw new DslRuntimeException(e);
        }
        configuration = new Configuration();
    }

    protected void parseFromStream(InputStream inputStream) throws IOException {
        XmlStringParser parser = new XmlStringParser();
        String configXml = StreamUtil.readText(inputStream, "UTF-8", true);
        xmlNode = parser.parse(configXml).getRoot();
    }

    protected void parseFromReader(Reader reader) throws IOException {
        XmlStringParser parser = new XmlStringParser();
        String configXml = StreamUtil.readText(reader, true);
        xmlNode = parser.parse(configXml).getRoot();
    }

    public Configuration parse() {
        if (parsed) {
            throw new DslRuntimeException(
                    "Each ConfigurationBuilder can only be used once.");
        }
        parsed = true;
        parseTableConfigs();
        return configuration;
    }

    private void parseTableConfigs() {
        String canCount = xmlNode.getAttribute(COUNT_SIGN);
        if (!StringUtil.isBlank(canCount)) {
            configuration.setCanCount(Boolean.parseBoolean(canCount));
        }
        String startMaxStr = xmlNode.getAttribute(MAX_START);
        if (!StringUtil.isBlank(startMaxStr)) {
            configuration.setStartMaxValue(Integer.parseInt(startMaxStr));
        }
        String limitMaxStr = xmlNode.getAttribute(MAX_LIMIT);
        if (!StringUtil.isBlank(limitMaxStr)) {
            configuration.setLimitMaxValue(Integer.parseInt(limitMaxStr));
        }
        XmlNode tableConfigsNode = xmlNode.getSubNode(TABLE_CONFIGS);
        if (tableConfigsNode == null) {
            return;
        }
        String defaultGeneratorBean = tableConfigsNode
                .getAttribute("default-generator-bean");
        KeyGenerator defaultKeyGenerator = BeanContainerFactory
                .getBeanContainer(this.getClass().getClassLoader()).getBean(
                        defaultGeneratorBean, KeyGenerator.class);
        configuration.setDefaultKeyGenerator(defaultKeyGenerator);
        String defaultAutoKeyGenerator = tableConfigsNode
                .getAttribute(DEFAULT_AUTO_GENERATOR_KEY);
        configuration.setDefaultAutoKeyGenerator(Boolean
                .parseBoolean(defaultAutoKeyGenerator));
        List<XmlNode> tableConfigNodes = tableConfigsNode
                .getSubNodes(TABLE_CONFIG);
        if (!CollectionUtil.isEmpty(tableConfigNodes)) {
            for (XmlNode tableConfigNode : tableConfigNodes) {
                parseTableConfig(tableConfigNode);
            }
        }
    }

    private void parseTableConfig(XmlNode tableConfigNode) {
        TableConfig tableConfig = new TableConfig();
        String autoGeneratorKey = tableConfigNode
                .getAttribute(AUTO_GENERATOR_KEY);
        tableConfig
                .setAutoGeneratedKeys(Boolean.parseBoolean(autoGeneratorKey));
        String tableName = tableConfigNode.getAttribute(TABLE_NAME);
        tableConfig.setTableName(tableName);
        String pojoClass = tableConfigNode.getAttribute(POJO_CLASS);
        tableConfig.setPojoClass(pojoClass);
        if (!tableConfig.isAutoGeneratedKeys()) {// 如果不是由数据库生成主键,则加载主键生成机制配置
            XmlNode keyGeneratorsNode = tableConfigNode
                    .getSubNode(KEY_GENERATORS);
            List<XmlNode> keyGeneratorNodes = keyGeneratorsNode
                    .getSubNodes(KEY_GENERATOR);
            if (!CollectionUtil.isEmpty(keyGeneratorNodes)) {
                for (XmlNode keyGeneratorNode : keyGeneratorNodes) {
                    parseKeyGenerator(keyGeneratorNode, tableConfig);
                }
            }
        }
        configuration.putTableConfig(tableConfig);
    }

    private void parseKeyGenerator(XmlNode keyGeneratorNode,
                                   TableConfig tableConfig) {
        String keyName = keyGeneratorNode.getAttribute(KEY_NAME);
        String generatorBean = keyGeneratorNode.getAttribute(GENERATOR_BEAN);
        tableConfig.addGeneratorConfig(new KeyGeneratorConfig(keyName,
                generatorBean));
    }

    protected class TableConfig {
        private boolean autoGeneratedKeys = true;
        private String pojoClass;
        private String tableName;
        private Class type;
        private List<KeyGeneratorConfig> generatorConfigs = new ArrayList<KeyGeneratorConfig>();

        public boolean isAutoGeneratedKeys() {
            return autoGeneratedKeys;
        }

        public void setAutoGeneratedKeys(boolean autoGeneratedKeys) {
            this.autoGeneratedKeys = autoGeneratedKeys;
        }

        public String getPojoClass() {
            return pojoClass;
        }

        public void setPojoClass(String pojoClass) {
            this.pojoClass = pojoClass;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public List<KeyGeneratorConfig> getGeneratorConfigs() {
            return generatorConfigs;
        }

        public void addGeneratorConfig(KeyGeneratorConfig config) {
            generatorConfigs.add(config);
        }

        public Class getType() {
            if (type != null) {
                return type;
            }
            try {
                type = Class.forName(pojoClass);
            } catch (ClassNotFoundException e) {
                throw new DslRuntimeException(e);
            }
            return type;
        }

        public KeyGenerator getKeyGenerator(String keyName) {
            for (KeyGeneratorConfig keyGeneratorConfig : generatorConfigs) {
                if (keyGeneratorConfig.getKeyName().equalsIgnoreCase(keyName)) {
                    return keyGeneratorConfig.getKeyGeneratorInstance();
                }
            }
            return null;
        }
    }

    protected class KeyGeneratorConfig {
        private String keyName;
        private String keyGeneratorBean;
        private KeyGenerator keyGenerator;

        public KeyGeneratorConfig(String keyName, String keyGeneratorBean) {
            super();
            this.keyName = keyName;
            this.keyGeneratorBean = keyGeneratorBean;
        }

        public String getKeyName() {
            return keyName;
        }

        public String getKeyGeneratorBean() {
            return keyGeneratorBean;
        }

        public KeyGenerator getKeyGeneratorInstance() {
            if (keyGenerator == null) {
                return BeanContainerFactory.getBeanContainer(
                        this.getClass().getClassLoader()).getBean(
                        keyGeneratorBean, KeyGenerator.class);
            }
            return keyGenerator;
        }

    }

}
