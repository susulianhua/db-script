package org.tinygroup.flowbasiccomponent.util;

import org.tinygroup.beancontainer.BeanContainer;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.flowbasiccomponent.errorcode.FlowComponentExceptionErrorCode;
import org.tinygroup.flowbasiccomponent.exception.FlowComponentException;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class NewInstanceUntil {
    private static Logger LOGGER = LoggerFactory
            .getLogger(NewInstanceUntil.class);

    public static Object getInstance(String beanId, String classPath) {
        BeanContainer<?> container = BeanContainerFactory
                .getBeanContainer(NewInstanceUntil.class.getClassLoader());
        try {
            if (StringUtil.isBlank(beanId)) {
                Class<?> clazz = Class.forName(classPath);
                return clazz.newInstance();
            }
            return container.getBean(beanId);
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "找不到指定的类：{0}", classPath);
            throw new FlowComponentException(
                    FlowComponentExceptionErrorCode.CLASS_NOT_FOUND, classPath);
        }
    }
}
