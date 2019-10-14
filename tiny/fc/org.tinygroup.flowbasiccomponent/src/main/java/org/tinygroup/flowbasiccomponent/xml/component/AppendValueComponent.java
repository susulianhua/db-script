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
public class AppendValueComponent implements ComponentInterface {
    private static Logger LOGGER = LoggerFactory
            .getLogger(AppendValueComponent.class);
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
    private String nodeValue;

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "将值：{0}写入到xml树：{1}的节点：{2}下",
                nodeValue, xml, nodePath);
        XMLOperatorUtil.appendValue(xml, nodePath, nodeValue);
        LOGGER.logMessage(LogLevel.INFO, "成功将值：{0}写入到xml树：{1}的节点：{2}下",
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
