package org.tinygroup.flowbasiccomponent.util.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flowbasiccomponent.util.ResponseCodeConvertUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 工具类实现001转换成002模式
 *
 * @author qiucn
 */
public class ResponseCodeSimpleConvertComponent implements ComponentInterface {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ResponseCodeSimpleConvertComponent.class);
    /**
     * 需要转换的值
     */
    private String value;
    /**
     * 转换结果
     */
    private String resultKey;

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "响应码转换组件开始执行");
        context.put(resultKey, ResponseCodeConvertUtil.codeConvert(value));
        LOGGER.logMessage(LogLevel.INFO, "响应码转换组件执行结束");
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
