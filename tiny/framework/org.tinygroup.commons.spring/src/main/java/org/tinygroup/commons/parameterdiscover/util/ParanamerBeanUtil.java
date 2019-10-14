package org.tinygroup.commons.parameterdiscover.util;

import org.tinygroup.commons.namediscover.LocalVariableTableParameterNameDiscoverer;
import org.tinygroup.commons.namediscover.ParameterNameDiscoverer;
import org.tinygroup.commons.parameterdiscover.ParanamerLocalVariableTableParameterNameDiscoverer;
import org.tinygroup.commons.tools.ArrayUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ParanamerBeanUtil {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ParanamerBeanUtil.class);

    private static List<ParameterNameDiscoverer> parameterNameDiscovererList = new ArrayList<ParameterNameDiscoverer>();

    static {
        parameterNameDiscovererList.add(new LocalVariableTableParameterNameDiscoverer());
        parameterNameDiscovererList.add(new ParanamerLocalVariableTableParameterNameDiscoverer());
    }

    public static String[] getMethodParameterName(Method method) {
        for (ParameterNameDiscoverer parameterNameDiscoverer : parameterNameDiscovererList) {
            try {
                String[] parameterNames = parameterNameDiscoverer
                        .getParameterNames(method);
                LOGGER.logMessage(
                        LogLevel.DEBUG,
                        "method:{0},ParameterNameDiscoverer:{1},parameterNames:{2}",
                        method, parameterNameDiscoverer, parameterNames);
                if (!ArrayUtil.isEmptyArray(parameterNames)) {
                    return parameterNames;
                }
            } catch (Exception e) {
                LOGGER.logMessage(LogLevel.WARN,
                        "method:{0},ParameterNameDiscoverer:{1},解析方法参数出错", e,
                        method, parameterNameDiscoverer);
            }
        }
        return null;
    }

    public static String[] getMethodParameterName(Constructor ctor) {
        for (ParameterNameDiscoverer parameterNameDiscoverer : parameterNameDiscovererList) {
            try {
                String[] parameterNames = parameterNameDiscoverer
                        .getParameterNames(ctor);
                if (!ArrayUtil.isEmptyArray(parameterNames)) {
                    return parameterNames;
                }
            } catch (Exception e) {
                LOGGER.logMessage(LogLevel.WARN,
                        "Constructor:{0},ParameterNameDiscoverer:{1},解析构造函数出错",
                        e, ctor, parameterNameDiscoverer);
            }
        }
        return null;
    }

}

