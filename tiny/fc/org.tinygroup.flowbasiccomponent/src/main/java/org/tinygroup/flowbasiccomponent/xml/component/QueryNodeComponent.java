package org.tinygroup.flowbasiccomponent.xml.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flowbasiccomponent.util.XMLOperatorUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 获取xml指定路径的节点 获取该节点及其子节点
 *
 * @author qiucn
 */
public class QueryNodeComponent implements ComponentInterface {
    private static Logger LOGGER = LoggerFactory
            .getLogger(QueryNodeComponent.class);
    /**
     * xml树
     */
    private XmlNode xml;
    /**
     * 节点路径
     */
    private String nodePath;
    /**
     * 值
     */
    private String resultKey;

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "查询xml树：{0}的节点：{1}", xml,
                nodePath);
        context.put(resultKey, XMLOperatorUtil.queryNode(xml, nodePath));
        LOGGER.logMessage(LogLevel.INFO, "成功查询xml树：{0}的节点：{1}", xml,
                nodePath);
    }

    public String getNodePath() {
        return nodePath;
    }

    public void setNodePath(String nodePath) {
        this.nodePath = nodePath;
    }

    public String getResultKey() {
        return resultKey;
    }

    public void setResultKey(String resultKey) {
        this.resultKey = resultKey;
    }

    public XmlNode getXml() {
        return xml;
    }

    public void setXml(XmlNode xml) {
        this.xml = xml;
    }
}
