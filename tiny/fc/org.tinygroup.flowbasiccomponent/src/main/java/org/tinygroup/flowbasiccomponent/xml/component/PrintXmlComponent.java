package org.tinygroup.flowbasiccomponent.xml.component;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flowbasiccomponent.util.Hex2AscUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * xml树打印
 *
 * @author qiucn
 */
public class PrintXmlComponent implements ComponentInterface {
    private static Logger LOGGER = LoggerFactory
            .getLogger(PrintXmlComponent.class);
    /**
     * xml树内容
     */
    private XmlNode xml;

    /**
     * 日志级别
     */
    private String logLevel;

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "xml树打印组件开始执行");
        if (StringUtil.isBlank(logLevel)) {
            logLevel = LogLevel.INFO.toString();
        }
        LOGGER.logMessage(LogLevel.valueOf(logLevel.toUpperCase()), Hex2AscUtil.str2HexStr(xml.toString()));
        LOGGER.logMessage(LogLevel.INFO, "xml树打印组件执行结束");
    }

    public XmlNode getXml() {
        return xml;
    }

    public void setXml(XmlNode xml) {
        this.xml = xml;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

}
