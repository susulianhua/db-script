package org.tinygroup.flowbasiccomponent.data.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flow.util.FlowElUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * @description：断言组件
 * @author: qiuqn
 * @version: 2016年4月12日 下午7:50:03
 */
public class AssertComponent implements ComponentInterface {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(AssertComponent.class);

    /**
     * el表达式
     */
    private String el;
    /**
     * 断言结果存放上下文key
     */
    private String resultKey;

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "单值断言组件执行开始");
        Object o = FlowElUtil.executeCondition(el, context,
                AssertComponent.class.getClassLoader());
        context.put(resultKey, o);
        LOGGER.logMessage(LogLevel.INFO, "单值断言组件执行结束");
    }

    public String getEl() {
        return el;
    }

    public void setEl(String el) {
        this.el = el;
    }

    public String getResultKey() {
        return resultKey;
    }

    public void setResultKey(String resultKey) {
        this.resultKey = resultKey;
    }
}
