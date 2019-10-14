package org.tinygroup.flowbasiccomponent.util.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flowbasiccomponent.util.ResponseCodeConvertUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 工具类实现001转换成002模式，规则可以动态添加
 *
 * @author qiucn
 */
public class ResponseCodeConvertComponent implements ComponentInterface {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ResponseCodeConvertComponent.class);

    /**
     * 数据类型
     */
    private String type;
    /**
     * 转换数据
     */
    private String input;
    /**
     * 转换结果存放上下文key
     */
    private String resultKey;

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getResultKey() {
        return resultKey;
    }

    public void setResultKey(String resultKey) {
        this.resultKey = resultKey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "响应码转换组件开始执行");
        context.put(resultKey, ResponseCodeConvertUtil.covert(type, input));
        LOGGER.logMessage(LogLevel.INFO, "响应码转换组件执行结束");
    }

}
