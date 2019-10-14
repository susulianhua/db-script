package org.tinygroup.flowbasiccomponent.data.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flow.util.FlowElUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * EL表达式执行组件
 *
 * @author qiucn
 */
public class ElExcutorComponent implements ComponentInterface {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ElExcutorComponent.class);

    private String el;

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "el表达式开始执行");
        FlowElUtil.executeNotCatchException(el, context, this.getClass().getClassLoader());
        LOGGER.logMessage(LogLevel.INFO, "el表达式执行结束");
    }

    public String getEl() {
        return el;
    }

    public void setEl(String el) {
        this.el = el;
    }

}
