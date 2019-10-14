package org.tinygroup.flowbasiccomponent.xml.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flowbasiccomponent.util.XMLOperatorUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * xml节点内容修改
 *
 * @author qiucn
 */
public class ModifyValueComponent implements ComponentInterface {
    private static Logger LOGGER = LoggerFactory
            .getLogger(ModifyValueComponent.class);
    /**
     * xml树
     */
    private XmlNode xml;

    /**
     * 节点路径
     */
    private String nodePath;
    /**
     * 修改后的值
     */
    private String nodeValue;

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "将值：{0}写入到xml树：{1}的节点：{2}下，覆盖原内容",
                nodeValue, xml, nodePath);
        XMLOperatorUtil.updateValue(xml, nodePath, nodeValue);
        LOGGER.logMessage(LogLevel.INFO, "成功将值：{0}写入到xml树：{1}的节点：{2}下，覆盖原内容",
                nodeValue, xml, nodePath);
    }

    public String getNodePath() {
        return nodePath;
    }

    public void setNodePath(String nodePath) {
        this.nodePath = nodePath;
    }

    public String getNodeValue() {
        return nodeValue;
    }

    public void setNodeValue(String nodeValue) {
        this.nodeValue = nodeValue;
    }

    public XmlNode getXml() {
        return xml;
    }

    public void setXml(XmlNode xml) {
        this.xml = xml;
    }
}
