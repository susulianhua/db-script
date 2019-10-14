package org.tinygroup.flowbasiccomponent.xml.component;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flowbasiccomponent.util.XMLOperatorUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 导入文件到xml树
 *
 * @author qiucn
 */
public class ImportXmlComponent implements ComponentInterface {
    private static Logger LOGGER = LoggerFactory
            .getLogger(ImportXmlComponent.class);
    /**
     * xml树
     */
    private XmlNode xml;

    /**
     * 导入节点位置(如果为空则默认导入到xml文件的最后一个节点后面)
     */
    private String nodePath;
    /**
     * 导入文件路径
     */
    private String importFilePath;

    /**
     * 数据编码格式
     */
    private String encoding;

    public void execute(Context context) {
        if (!StringUtil.isBlank(nodePath)) {
            LOGGER.logMessage(LogLevel.INFO,
                    "文件:{0}导入开始，导入文件到xml树：{1}的节点：{2}下", importFilePath, xml,
                    nodePath);
        } else {
            LOGGER.logMessage(LogLevel.INFO,
                    "文件:{0}导入开始，导入文件到xml树：{1}最后一个节点后方", importFilePath, xml);
        }
        XMLOperatorUtil.importFile(xml, importFilePath, nodePath, encoding);
        if (!StringUtil.isBlank(nodePath)) {
            LOGGER.logMessage(LogLevel.INFO,
                    "成功导入文件:{0}，导入文件到xml树：{1}的节点：{2}下成功", importFilePath, xml,
                    nodePath);
        } else {
            LOGGER.logMessage(LogLevel.INFO,
                    "成功导入文件:{0}，导入文件到xml树：{1}最后一个节点后方", importFilePath, xml);
        }
    }

    public String getNodePath() {
        return nodePath;
    }

    public void setNodePath(String nodePath) {
        this.nodePath = nodePath;
    }

    public String getImportFilePath() {
        return importFilePath;
    }

    public void setImportFilePath(String importFilePath) {
        this.importFilePath = importFilePath;
    }

    public XmlNode getXml() {
        return xml;
    }

    public void setXml(XmlNode xml) {
        this.xml = xml;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
