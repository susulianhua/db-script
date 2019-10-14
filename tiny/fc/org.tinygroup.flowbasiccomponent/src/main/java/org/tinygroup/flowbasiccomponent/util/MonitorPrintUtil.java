package org.tinygroup.flowbasiccomponent.util;

import org.tinygroup.flowbasiccomponent.errorcode.FlowComponentExceptionErrorCode;
import org.tinygroup.flowbasiccomponent.exception.FlowComponentException;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.vfs.VFS;

import java.io.FileWriter;
import java.io.IOException;

public class MonitorPrintUtil {

    private static Logger LOGGER = LoggerFactory
            .getLogger(MonitorPrintUtil.class);

    public static void printMonitor(String message, String filePath) {
        FileWriter writer = null;
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = new FileWriter(
                    VFS.resolveFile(filePath).getAbsolutePath(), true);
            writer.write(message + "\r\n");
        } catch (IOException e) {
            LOGGER.logMessage(LogLevel.ERROR, "写监控失败：{0},错误信息：{1}", message, e);
            throw new FlowComponentException(
                    FlowComponentExceptionErrorCode.PRINT_MONITOR_FAILED,
                    message, e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                LOGGER.logMessage(LogLevel.ERROR, "写文件器关闭失败", e);
            }
        }
    }
}
