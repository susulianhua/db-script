package org.tinygroup.flowbasiccomponent.util.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flowbasiccomponent.util.NewInstanceUntil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 根据类路径创建相对应的实例
 *
 * @author qiucn
 */
public class NewInstanceComponent implements ComponentInterface {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(NewInstanceComponent.class);
    /**
     * 类的beanId
     */
    private String beanId;
    /**
     * 类路径
     */
    private String classPath;

    /**
     * 结果集在上下文存放的key
     */
    private String resultKey;

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.INFO, "类实例创建组件开始执行");
        context.put(resultKey, NewInstanceUntil.getInstance(beanId, classPath));
        LOGGER.logMessage(LogLevel.INFO, "类实例创建组件执行结束");
    }

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public String getResultKey() {
        return resultKey;
    }

    public void setResultKey(String resultKey) {
        this.resultKey = resultKey;
    }

    public String getBeanId() {
        return beanId;
    }

    public void setBeanId(String beanId) {
        this.beanId = beanId;
    }
}
