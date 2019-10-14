package org.tinygroup.flowbasiccomponent.util.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 输出日志组件
 *
 * @author qiucn
 */
public class LoggerPrintComponent implements ComponentInterface {

    private Logger LOGGER = LoggerFactory.getLogger(LoggerPrintComponent.class);

    /**
     * 日志级别
     */
    private String logLevel;

    /**
     * 日志内容
     */
    private String message;

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "日志输出组件开始执行");
        LOGGER.logMessage(LogLevel.valueOf(logLevel.toUpperCase()), message);
        LOGGER.logMessage(LogLevel.INFO, "日志输出组件执行结束");
    }
}
