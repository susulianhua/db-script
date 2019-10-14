package org.tinygroup.flowbasiccomponent.xml.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flowbasiccomponent.util.XMLOperatorUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 获取xml指定路径的节点 指定节点名的 子节点列表，指定的名称可能有多个同名节点，这里返回所有该名称节点列表
 *
 * @author qiucn
 */
public class QueryByNodeNameComponent implements ComponentInterface {
    private static Logger LOGGER = LoggerFactory
            .getLogger(QueryByNodeNameComponent.class);
    /**
     * xml树
     */
    private XmlNode xml;
    /**
     * 节点路径
     */
    private String nodePath;
    /**
     * 指定获取的节点名称
     */
    private String nodeName;
    /**
     * 查询结果上下文存放key
     */
    private String resultKey;

    public XmlNode getXml() {
        return xml;
    }

    public void setXml(XmlNode xml) {
        this.xml = xml;
    }

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO,
                "查询xml树：{0}的节点：{1}下子节点列表，出现的子节点名称为：{2}", xml, nodePath,
                nodeName);
        context.put(resultKey,
                XMLOperatorUtil.queryByNodeName(xml, nodePath, nodeName));
        LOGGER.logMessage(LogLevel.INFO,
                "成功出现xml树：{0}的节点：{1}下子节点列表，出现的子节点名称为：{2}", xml, nodePath,
                nodeName);
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

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

}
