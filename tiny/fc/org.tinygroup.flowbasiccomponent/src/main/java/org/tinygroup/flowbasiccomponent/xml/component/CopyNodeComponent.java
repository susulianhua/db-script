package org.tinygroup.flowbasiccomponent.xml.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flowbasiccomponent.util.XMLOperatorUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 将节点内容复制到另外一个节点
 *
 * @author qiucn
 */
public class CopyNodeComponent implements ComponentInterface {
    private static Logger LOGGER = LoggerFactory
            .getLogger(CopyNodeComponent.class);
    /**
     * xml树
     */
    private XmlNode xml;

    /**
     * 复制的节点内容对应路径
     */
    private String fromNodePath;
    /**
     * 复制目的地节点
     */
    private String toNodePath;

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "将xml树：{0}的节点：{1}复制到节点：{2}下", xml,
                fromNodePath, toNodePath);
        XMLOperatorUtil.copyNode(xml, fromNodePath, toNodePath);
        LOGGER.logMessage(LogLevel.INFO, "成功将xml树：{0}的节点：{1}复制到节点：{2}下", xml,
                fromNodePath, toNodePath);
    }

    public XmlNode getXml() {
        return xml;
    }

    public void setXml(XmlNode xml) {
        this.xml = xml;
    }

    public String getFromNodePath() {
        return fromNodePath;
    }

    public void setFromNodePath(String fromNodePath) {
        this.fromNodePath = fromNodePath;
    }

    public String getToNodePath() {
        return toNodePath;
    }

    public void setToNodePath(String toNodePath) {
        this.toNodePath = toNodePath;
    }
}
