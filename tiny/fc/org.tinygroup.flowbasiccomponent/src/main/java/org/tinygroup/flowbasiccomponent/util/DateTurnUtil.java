package org.tinygroup.flowbasiccomponent.util;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期偏移工具，输入一个日期和偏移量，输出计算后的日期
 *
 * @author qiucn
 */
public class DateTurnUtil {

    public static Date dateTurn(Date date, int num, int calendarField) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendarField, calendar.get(calendarField) + num);
        return calendar.getTime();
    }

    public static Date dateTurn(long dateTime, int num, int calendarField) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(dateTime));
        calendar.set(calendarField, calendar.get(calendarField) + num);
        return calendar.getTime();
    }
}
