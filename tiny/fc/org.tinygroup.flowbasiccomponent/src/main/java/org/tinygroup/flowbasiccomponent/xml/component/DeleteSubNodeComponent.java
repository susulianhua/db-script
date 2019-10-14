package org.tinygroup.flowbasiccomponent.xml.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flowbasiccomponent.util.XMLOperatorUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * xml删除节点 删除指定节点下面的子节点，但不删除该节点
 * 如<users><name>hundsun</name></users>,这次删除的是users下面的子节点，但是users节点仍然保留
 *
 * @author qiucn
 */
public class DeleteSubNodeComponent implements ComponentInterface {
    private static Logger LOGGER = LoggerFactory
            .getLogger(DeleteSubNodeComponent.class);
    /**
     * xml树
     */
    private XmlNode xml;
    /**
     * 节点路径
     */
    private String nodePath;
    /**
     * 将删除掉的内容返回到指定的key下面
     */
    private String resultKey;

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "删除xml树：{0}的节点：{1}下的子节点", xml,
                nodePath);
        context.put(resultKey,
                XMLOperatorUtil.deleteSubNode(xml, nodePath));
        LOGGER.logMessage(LogLevel.INFO, "成功删除xml树：{0}的节点：{1}下的子节点",
                xml, nodePath);
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
