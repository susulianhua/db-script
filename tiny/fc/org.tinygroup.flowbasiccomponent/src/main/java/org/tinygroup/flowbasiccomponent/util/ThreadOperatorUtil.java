package org.tinygroup.flowbasiccomponent.util;

import org.tinygroup.flowbasiccomponent.FlowComponentConstants;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 线程操作工具类
 *
 * @author qiucn
 */
public class ThreadOperatorUtil {

    private static Logger LOGGER = LoggerFactory
            .getLogger(ThreadOperatorUtil.class);

    public static void threadSleep(long sleepTime, Integer timeUnit) {
        if (timeUnit == null) {
            timeUnit = FlowComponentConstants.TIME_UNIT_MILLISECOND;
        }
        switch (timeUnit) {
            case FlowComponentConstants.TIME_UNIT_SECOND:
                sleepTime = sleepTime * 1000;
                break;
            case FlowComponentConstants.TIME_UNIT_MINUTE:
                sleepTime = sleepTime * 1000 * 60;
                break;
            case FlowComponentConstants.TIME_UNIT_HOUR:
                sleepTime = sleepTime * 1000 * 60 * 60;
                break;
            case FlowComponentConstants.TIME_UNIT_DAY:
                sleepTime = sleepTime * 1000 * 60 * 60 * 24;
                break;
            default:
                break;
        }
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            LOGGER.logMessage(LogLevel.ERROR, "线程休眠被中断", e);
        }
        LOGGER.logMessage(LogLevel.INFO, "线程休眠结束，休眠时间：{0}毫秒", sleepTime);
    }

}
