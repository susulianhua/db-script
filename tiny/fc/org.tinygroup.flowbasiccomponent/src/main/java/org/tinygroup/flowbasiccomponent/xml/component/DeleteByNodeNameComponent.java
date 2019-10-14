package org.tinygroup.flowbasiccomponent.xml.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flowbasiccomponent.util.XMLOperatorUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * xml删除节点 删除xml指定路径的 指定节点名的 子节点，指定的名称可能有多个同名节点，这里删除所有该名称节点
 *
 * @author qiucn
 */
public class DeleteByNodeNameComponent implements ComponentInterface {
    private static Logger LOGGER = LoggerFactory
            .getLogger(DeleteByNodeNameComponent.class);
    /**
     * xml树
     */
    private XmlNode xml;
    /**
     * 节点路径
     */
    private String nodePath;
    /**
     * 指定删除的节点名称
     */
    private String nodeName;
    /**
     * 将删除掉的内容返回到指定的key下面
     */
    private String resultKey;

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "删除xml树：{0}的节点：{1}下，子节点名称为：{2}的节点",
                xml, nodePath, nodeName);
        context.put(resultKey,
                XMLOperatorUtil.deleteByNodeName(xml, nodePath, nodeName));
        LOGGER.logMessage(LogLevel.INFO,
                "成功删除xml树：{0}的节点：{1}下，子节点名称为：{2}的节点", xml, nodePath,
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

    public XmlNode getXml() {
        return xml;
    }

    public void setXml(XmlNode xml) {
        this.xml = xml;
    }

}
