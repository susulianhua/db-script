package org.tinygroup.flowbasiccomponent.xml.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flowbasiccomponent.util.XMLOperatorUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 将节点从一个位置移动到另外一个位置
 *
 * @author qiucn
 */
public class MoveNodeComponent implements ComponentInterface {
    private static Logger LOGGER = LoggerFactory
            .getLogger(MoveNodeComponent.class);
    /**
     * xml树
     */
    private XmlNode xml;

    /**
     * 移动的节点对应路径
     */
    private String fromNodePath;
    /**
     * 移动目的地节点
     */
    private String toNodePath;

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "将xml树：{0}的节点：{1}移动到节点：{2}下", xml,
                fromNodePath, toNodePath);
        XMLOperatorUtil.moveNode(xml, fromNodePath, toNodePath);
        LOGGER.logMessage(LogLevel.INFO, "成功将xml树：{0}的节点：{1}移动到节点：{2}下", xml,
                fromNodePath, toNodePath);
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

    public XmlNode getXml() {
        return xml;
    }

    public void setXml(XmlNode xml) {
        this.xml = xml;
    }
}
