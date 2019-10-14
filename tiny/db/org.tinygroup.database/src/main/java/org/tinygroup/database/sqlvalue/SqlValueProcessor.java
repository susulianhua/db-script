package org.tinygroup.database.sqlvalue;

/**
 * sql值处理接口,比如默认值，初始化数据的值
 */
public interface SqlValueProcessor {

    /**
     * 处理日期类型
     *
     * @param value
     * @return
     */
    String handleDateType(String value);
}
