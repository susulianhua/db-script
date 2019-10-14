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
 * @author qiucn
 */
public class EnumSimpleConvertComponent implements ComponentInterface {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(EnumSimpleConvertComponent.class);

    /**
     * 需要转换的值
     */
    private String value;
    /**
     * 转换结果
     */
    private String resultKey;

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "枚举转换组件开始执行");
        context.put(resultKey, CommEnumConverUntil.codeConvert(value));
        LOGGER.logMessage(LogLevel.INFO, "枚举转换组件执行结束");
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getResultKey() {
        return resultKey;
    }

    public void setResultKey(String resultKey) {
        this.resultKey = resultKey;
    }

}
