package org.tinygroup.flowbasiccomponent.util.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flowbasiccomponent.util.DateTurnUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 根据参数生成新的日期
 *
 * @author qiucn
 */
public class DateCalculateComponent implements ComponentInterface {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DateCalculateComponent.class);
    /**
     * 源日期
     */
    private long dateTime;
    /**
     * 偏移天数，正数加负数减
     */
    private int num;

    /**
     * 计算字段，（年，月，日）
     */
    private int calendarField;

    /**
     * 结果集在上下文存放的key
     */
    private String resultKey;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getResultKey() {
        return resultKey;
    }

    public void setResultKey(String resultKey) {
        this.resultKey = resultKey;
    }

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "日期偏移组件开始执行");
        context.put(resultKey,
                DateTurnUtil.dateTurn(dateTime, num, calendarField).getTime());
        LOGGER.logMessage(LogLevel.INFO, "日期偏移组件执行结束");
    }

    public int getCalendarField() {
        return calendarField;
    }

    public void setCalendarField(int calendarField) {
        this.calendarField = calendarField;
    }
}
