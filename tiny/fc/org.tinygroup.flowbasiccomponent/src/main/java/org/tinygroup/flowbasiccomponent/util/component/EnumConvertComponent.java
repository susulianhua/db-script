package org.tinygroup.flowbasiccomponent.util.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flowbasiccomponent.util.CommEnumConverUntil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 枚举转换组件
 *
 * @param <B>
 * @author qiucn
 */
public class EnumConvertComponent<B> implements ComponentInterface {

    private Logger LOGGER = LoggerFactory.getLogger(EnumConvertComponent.class);

    /**
     * 枚举类路径
     */
    private String enumClassPath;

    /**
     * 需要转换的值
     */
    private String value;

    /**
     * 枚举转换器类型
     */
    private String type;
    /**
     * 转换结果
     */
    private String resultKey;

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "枚举转换组件开始执行");
        context.put(resultKey,
                CommEnumConverUntil.getEnum(enumClassPath, value, type));
        LOGGER.logMessage(LogLevel.INFO, "枚举转换组件执行结束");
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEnumClassPath() {
        return enumClassPath;
    }

    public void setEnumClassPath(String enumClassPath) {
        this.enumClassPath = enumClassPath;
    }

    public String getResultKey() {
        return resultKey;
    }

    public void setResultKey(String resultKey) {
        this.resultKey = resultKey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
