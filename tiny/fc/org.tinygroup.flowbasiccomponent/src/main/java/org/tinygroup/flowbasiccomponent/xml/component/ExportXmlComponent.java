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
 * 导出xml树到指定文件
 *
 * @author qiucn
 */
public class ExportXmlComponent implements ComponentInterface {
    private static Logger LOGGER = LoggerFactory
            .getLogger(ExportXmlComponent.class);
    /**
     * xml树
     */
    private XmlNode xml;

    /**
     * 需要导出的节点(如果为空则默认导出整个xml文件)
     */
    private String nodePath;
    /**
     * 导出文件路径
     */
    private String exportFilePath;

    /**
     * 编码格式(如果为空则默认为UTF-8)
     */
    private String encoding;

    public void execute(Context context) {
        if (!StringUtil.isBlank(nodePath)) {
            LOGGER.logMessage(LogLevel.INFO,
                    "xml树:{0}内容导出开始，将节点：{1}导出到文件：{2}", xml, nodePath,
                    exportFilePath);
        } else {
            LOGGER.logMessage(LogLevel.INFO, "xml树:{0}导出开始，导出到文件：{1}",
                    xml, exportFilePath);
        }
        XMLOperatorUtil.export(xml, exportFilePath, nodePath, encoding);
        LOGGER.logMessage(LogLevel.INFO, "成功将值：{0}写入到xml文件：{1}的节点：{2}下",
                xml, exportFilePath);
        if (!StringUtil.isBlank(nodePath)) {
            LOGGER.logMessage(LogLevel.INFO,
                    "成功导出xml树:{0}的节点内容，将节点：{1}导出到文件：{2}", xml, nodePath,
                    exportFilePath);
        } else {
            LOGGER.logMessage(LogLevel.INFO, "成功导出xml数:{0}，导出到文件：{1}",
                    xml, exportFilePath);
        }
    }

    public String getNodePath() {
        return nodePath;
    }

    public void setNodePath(String nodePath) {
        this.nodePath = nodePath;
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

    public String getExportFilePath() {
        return exportFilePath;
    }

    public void setExportFilePath(String exportFilePath) {
        this.exportFilePath = exportFilePath;
    }
}
