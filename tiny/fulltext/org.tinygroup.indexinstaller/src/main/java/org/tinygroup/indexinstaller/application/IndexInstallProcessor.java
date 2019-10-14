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
package org.tinygroup.indexinstaller.application;

import org.tinygroup.application.AbstractApplicationProcessor;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.fulltext.FullText;
import org.tinygroup.fulltext.FullTextHelper;
import org.tinygroup.fulltext.exception.FullTextException;
import org.tinygroup.indexinstaller.IndexDataSource;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.xmlparser.node.XmlNode;

import java.util.List;

/**
 * 执行安装索引的操作
 *
 * @author yancheng11334
 */
public class IndexInstallProcessor extends AbstractApplicationProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexInstallProcessor.class);
    private XmlNode applicationConfig;
    private XmlNode componentConfig;
    /**
     * 索引数据源
     */
    private List<IndexDataSource<XmlNode>> dataSources;

    public List<IndexDataSource<XmlNode>> getDataSources() {
        return dataSources;
    }

    public void setDataSources(List<IndexDataSource<XmlNode>> dataSources) {
        this.dataSources = dataSources;
    }

    public void start() {
        LOGGER.logMessage(LogLevel.INFO, "开始安装索引数据源...");
        XmlNode totalNode = ConfigurationUtil.combineXmlNode(applicationConfig, componentConfig);
        List<XmlNode> sourceNodes = totalNode.getSubNodes("index-source");

        //实例化FullText
        FullText fullText = FullTextHelper.getFullText();

        //是否允许单节点异常
        boolean allowError = Boolean.parseBoolean(StringUtil.defaultIfEmpty(totalNode.getAttribute("allowError"), "false"));

        for (XmlNode node : sourceNodes) {
            try {
                IndexDataSource<XmlNode> dataSource = getIndexDataSource(node.getAttribute("type"));
                dataSource.setFullText(fullText);
                dataSource.install(node);
            } catch (FullTextException e) {
                if (allowError) {
                    //记录错误日志继续
                    LOGGER.logMessage(LogLevel.WARN, "安装索引节点发生异常:" + e.getMessage());
                    continue;
                } else {
                    //抛出异常
                    throw e;
                }
            } catch (Exception e) {
                if (allowError) {
                    //记录错误日志继续
                    LOGGER.logMessage(LogLevel.WARN, "安装索引节点发生异常:" + e.getMessage());
                    continue;
                } else {
                    //抛出异常
                    throw new FullTextException(e);
                }
            }

        }
        LOGGER.logMessage(LogLevel.INFO, "安装索引数据源结束!");
    }

    public void stop() {

    }

    private IndexDataSource<XmlNode> getIndexDataSource(String type) {
        for (IndexDataSource<XmlNode> indexDataSource : dataSources) {
            if (indexDataSource.getType().equals(type)) {
                return indexDataSource;
            }
        }
        throw new FullTextException(String.format("类型[%s]的索引数据源配置没有找到对应的IndexDataSource", type));
    }

    public void config(XmlNode applicationConfig, XmlNode componentConfig) {
        this.applicationConfig = applicationConfig;
        this.componentConfig = componentConfig;
    }

    public XmlNode getApplicationConfig() {
        return applicationConfig;
    }

    public String getApplicationNodePath() {
        return "/application/index-installer";
    }

    public XmlNode getComponentConfig() {
        return componentConfig;
    }

    public String getComponentConfigPath() {
        return "indexinstaller.config.xml";
    }

    public int getOrder() {
        return 0;
    }

}
