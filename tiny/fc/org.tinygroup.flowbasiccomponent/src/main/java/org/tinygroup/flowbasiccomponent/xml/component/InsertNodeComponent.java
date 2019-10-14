package org.tinygroup.flowbasiccomponent.xml.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flowbasiccomponent.util.XMLOperatorUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * xml追加内容组件 在指定路径下面加入追加传入的值
 *
 * @author qiucn
 */
public class InsertNodeComponent implements ComponentInterface {
    private static Logger LOGGER = LoggerFactory
            .getLogger(InsertNodeComponent.class);
    /**
     * xml树
     */
    private XmlNode xml;
    /**
     * 节点路径
     */
    private String nodePath;
    /**
     * 插入节点
     */
    private String xmlNode;
    /**
     * xml树上下文存放key
     */
    private String resultKey;

    public XmlNode getXml() {
        return xml;
    }

    public void setXml(XmlNode xml) {
        this.xml = xml;
    }

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "将xml格式字符串：{0}写入到xml树：{1}的节点：{2}下",
                xmlNode, xml, nodePath);
        XMLOperatorUtil.insertNode(xml, nodePath, xmlNode);
        LOGGER.logMessage(LogLevel.INFO, "成功将xml格式字符串：{0}写入到xml树：{1}的节点：{2}下",
                xmlNode, xml, nodePath);
    }

    public String getNodePath() {
        return nodePath;
    }

    public void setNodePath(String nodePath) {
        this.nodePath = nodePath;
    }

    public String getXmlNode() {
        return xmlNode;
    }

    public void setXmlNode(String xmlNode) {
        this.xmlNode = xmlNode;
    }

    public String getResultKey() {
        return resultKey;
    }

    public void setResultKey(String resultKey) {
        this.resultKey = resultKey;
    }

}
