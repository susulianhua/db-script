package org.tinygroup.flowbasiccomponent.util.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flowbasiccomponent.util.ThreadOperatorUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 线程休眠组件
 *
 * @author qiucn
 */
public class ThreadSleepComponent implements ComponentInterface {

    private Logger LOGGER = LoggerFactory.getLogger(ThreadSleepComponent.class);

    /**
     * 休眠时间，毫秒
     */
    private long sleepTime;
    /**
     * 时间单位
     */
    private Integer timeUnit = 1;

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "线程休眠组件开始执行");
        ThreadOperatorUtil.threadSleep(sleepTime, timeUnit);
        LOGGER.logMessage(LogLevel.INFO, "线程休眠组件执行结束");
    }

    public long getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }

    public Integer getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(Integer timeUnit) {
        this.timeUnit = timeUnit;
    }
}
