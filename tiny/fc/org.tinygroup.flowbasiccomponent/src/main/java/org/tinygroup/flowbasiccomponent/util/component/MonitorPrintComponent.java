package org.tinygroup.flowbasiccomponent.util.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flowbasiccomponent.util.MonitorPrintUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 写监控组件
 *
 * @author qiucn
 */
public class MonitorPrintComponent implements ComponentInterface {

    private Logger LOGGER = LoggerFactory
            .getLogger(MonitorPrintComponent.class);

    /**
     * 监控内容
     */
    private String message;

    /**
     * 文件全路径
     */
    private String filePath;

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "写监控开始执行");
        MonitorPrintUtil.printMonitor(message, filePath);
        LOGGER.logMessage(LogLevel.INFO, "写监控执行结束");
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
