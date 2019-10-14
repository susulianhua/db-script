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
package org.tinygroup.fileresolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.config.ConfigurationManager;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.fileresolver.impl.LocalPropertiesFileProcessor;
import org.tinygroup.fileresolver.impl.MergePropertiesFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;

public class TestApplicationProperties {

    private final static Logger LOGGER = LoggerFactory.getLogger(TestApplicationProperties.class);
    private static String DEFAULT_CONFIG = "application.xml";

    public static void main(String[] args) {

        InputStream inputStream = TestApplicationProperties.class
                .getClassLoader().getResourceAsStream(DEFAULT_CONFIG);
        if (inputStream == null) {
            inputStream = TestApplicationProperties.class
                    .getResourceAsStream(DEFAULT_CONFIG);
        }
        String applicationConfig = "";
        try {
            applicationConfig = StreamUtil
                    .readText(inputStream, "UTF-8", false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (applicationConfig != null) {
            ConfigurationManager c = ConfigurationUtil
                    .getConfigurationManager();
            c.setApplicationConfiguration(new XmlStringParser().parse(
                    applicationConfig).getRoot());

        }

        FileResolver fileResolver = FileResolverFactory.getFileResolver();
        FileResolverUtil.addClassPathPattern(fileResolver);
        fileResolver
                .addResolvePath(FileResolverUtil.getClassPath(fileResolver));
        fileResolver.addResolvePath(FileResolverUtil.getWebClasses());
        LocalPropertiesFileProcessor localProcessor = new LocalPropertiesFileProcessor(
                applicationConfig);
        localProcessor.start();

        MergePropertiesFileProcessor mergeProcessor = new MergePropertiesFileProcessor();
        mergeProcessor.start();

        fileResolver.resolve();
        Map<String, String> map = ConfigurationUtil.getConfigurationManager()
                .getConfiguration();
        for (String key : map.keySet()) {
            LOGGER.logMessage(LogLevel.INFO, "key:{},value:{}", key, map.get(key));
        }

        Assert.assertEquals("127.0.0.1", map.get("ip"));
        Assert.assertEquals("127.0.0.1", map.get("ipVar"));
        XmlNode node = ConfigurationUtil.getConfigurationManager().getApplicationConfiguration();
        LOGGER.logMessage(LogLevel.INFO, "{}", node);
        List<XmlNode> nodes1 = node.getSubNodes("value1");
        Assert.assertEquals("127.0.0.1", nodes1.get(0).getAttribute("value"));
        List<XmlNode> nodes2 = node.getSubNodes("value2");
        Assert.assertEquals("127.0.0.1", nodes2.get(0).getAttribute("value"));

    }
}
