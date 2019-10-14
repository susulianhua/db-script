package org.tinygroup.flowbasiccomponent.exception.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flow.FlowExecutor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 异常抛出组件
 *
 * @author qiucn
 */
public class ThrowExceptionComponent implements ComponentInterface {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ThrowExceptionComponent.class);

    private Object exceptionObject;

    public Object getExceptionObject() {
        return exceptionObject;
    }

    public void setExceptionObject(Object exceptionObject) {
        this.exceptionObject = exceptionObject;
    }

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "异常抛出组件开始执行");
        if (exceptionObject == null) {
            exceptionObject = context.get(FlowExecutor.EXCEPTION_KEY);
        }
        LOGGER.logMessage(LogLevel.INFO, "异常抛出组件执行结束");
        if (exceptionObject instanceof RuntimeException) {
            throw (RuntimeException) exceptionObject;
        } else if (exceptionObject instanceof Throwable) {
            throw new RuntimeException((Throwable) exceptionObject);
        } else {
            throw new RuntimeException(exceptionObject.toString());
        }
    }

}
