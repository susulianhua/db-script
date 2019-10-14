package org.tinygroup.flowbasiccomponent.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 数据格式化工具类
 *
 * @author qiucn
 */
public class DataFormatUtil {

    /**
     * 数值格式化，精度截取
     *
     * @param num
     * @param precision
     * @return
     */
    public static double numPrecisionFormat(double num, int precision) {
        BigDecimal big = new BigDecimal(num);
        return big.setScale(precision, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 日期格式化
     *
     * @param date
     * @param pattern 输出格式，例如 yyyy-MM-dd
     * @return
     */
    public static String dateFormat(Date date, String pattern) {
        SimpleDateFormat sFormat = new SimpleDateFormat(pattern);
        return sFormat.format(date);
    }

    /**
     * 日期格式化
     *
     * @param dateTime
     * @param pattern  输出格式，例如 yyyy-MM-dd
     * @return
     */
    public static String dateFormat(long dateTime, String pattern) {
        SimpleDateFormat sFormat = new SimpleDateFormat(pattern);
        return sFormat.format(dateTime);
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getSystemDate() {
        SimpleDateFormat sFormat = new SimpleDateFormat("yyyyMMdd");
        return sFormat.format(new Date());
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getSystemTime() {
        SimpleDateFormat sFormat = new SimpleDateFormat("hhmmss");
        return sFormat.format(new Date());
    }
}
