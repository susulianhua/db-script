/**
 * Copyright (c) 2012-2017, www.tinygroup.org (luo_guo@icloud.com).
 * <p>
 * Licensed under the GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/gpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tinygroup.service;

import org.apache.commons.beanutils.MethodUtils;
import org.tinygroup.commons.tools.ValueUtil;
import org.tinygroup.context.Context;
import org.tinygroup.context2object.util.Context2ObjectUtil;
import org.tinygroup.event.Parameter;
import org.tinygroup.event.exception.ParamIsNullException;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.service.exception.ServiceExecuteException;
import org.tinygroup.service.exception.ServiceRunException;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.List;

public class ServiceProxy implements Service {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceProxy.class);
    private Object objectInstance;
    private Method method;
    private String serviceId;
    private List<Parameter> inputParameters;
    private Parameter outputParameter;
    private String methodName;
    private ClassLoader loader = this.getClass().getClassLoader();

    public ClassLoader getLoader() {
        return loader;
    }

    public void setLoader(ClassLoader loader) {
        this.loader = loader;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object getObjectInstance() {
        return objectInstance;
    }

    public void setObjectInstance(Object objectInstance) {
        this.objectInstance = objectInstance;
    }

    public Parameter getOutputParameter() {
        return outputParameter;
    }

    public void setOutputParameter(Parameter outputParameter) {
        this.outputParameter = outputParameter;
    }

    public List<Parameter> getInputParameters() {
        return inputParameters;
    }

    public void setInputParameters(List<Parameter> inputParameters) {
        this.inputParameters = inputParameters;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
        this.methodName = method.getName();
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * 根据参数配置的type获取真正的class
     *
     * @param param 参数配置的type
     * @return
     */
    private Class<?> getClassByName(Parameter param) {
        String name = param.getType();// 参数类型名默认取参数type
        if (param.getCollectionType() != null) { // 如果配置了参数集合类型，则参数类型取集合类型的值
            name = param.getCollectionType();
        }
        Class<?> clazz = getClassByName(name);
        if (param.isArray()) {
            return Array.newInstance(clazz, 1).getClass();
        } else {
            return clazz;
        }

    }

    private Class<?> getClassByName(String name) {
        try {
            if ("int".equals(name)) {
                return int.class;
            } else if ("byte".equals(name)) {
                return byte.class;
            } else if ("long".equals(name)) {
                return long.class;
            } else if ("short".equals(name)) {
                return short.class;
            } else if ("char".equals(name)) {
                return char.class;
            } else if ("double".equals(name)) {
                return double.class;
            } else if ("float".equals(name)) {
                return float.class;
            } else if ("boolean".equals(name)) {
                return boolean.class;
            }

            return loader.loadClass(name);
        } catch (ClassNotFoundException e) {
            throw new ServiceExecuteException(e);
        }
    }

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.DEBUG, "开始执行serviceProxy,对应方法名:{}", methodName);
        if (method == null) {
            method = findMethod();
        }
        // 获取所有参数的值
        LOGGER.logMessage(LogLevel.DEBUG, "开始获取方法参数");
        Object[] args = getArguments(context);
        LOGGER.logMessage(LogLevel.DEBUG, "取得方法参数");
        try {
            // 20130109调整，添加无返回值的服务的处理
            if (outputParameter != null
                    && !outputParameter.getType().equals("void")
                    && !outputParameter.getType().equals("")) {
                Object result = MethodUtils.invokeMethod(objectInstance, methodName, args, method.getParameterTypes());
                context.put(outputParameter.getName(), result);
            } else {
                MethodUtils.invokeMethod(objectInstance, methodName, args, method.getParameterTypes());
            }
            // } catch (InvocationTargetException e) {
            // InvocationTargetException realException =
            // (InvocationTargetException) e;
            // Object targetExeception = realException.getTargetException();
            // if (targetExeception != null
            // && targetExeception instanceof Exception) {
            // dealException((Exception) targetExeception);
            // }
            //
        } catch (Exception e) {
            // dealException(e);
            throw new ServiceRunException(serviceId, e);
        }
        LOGGER.logMessage(LogLevel.DEBUG, "执行serviceProxy完毕,对应方法名:{}", methodName);
    }

    // private void dealException(Exception e) {
    // if (e instanceof BaseRuntimeException) {
    // throw (BaseRuntimeException) e;
    // } else {
    // if (!ExceptionUtil.handle(e)) {
    // throw new ServiceRunException(e);
    // }
    // }
    // }

    private Method findMethod() {
        Class<?>[] argsType = null;// 参数类型列表
        if (inputParameters != null) {
            argsType = new Class<?>[inputParameters.size()];
            for (int i = 0; i < argsType.length; i++) {
                argsType[i] = getClassByName(inputParameters.get(i));
            }
        }
        try {
            return objectInstance.getClass().getMethod(methodName, argsType);
        } catch (Exception e) {
            LOGGER.errorMessage("获取方法时出现异常,方法名:{methodName}", e, methodName);
            throw new RuntimeException("获取方法时出现异常,方法名:{" + methodName + "}", e);
        }

    }

    /**
     * 返回参数值列表
     *
     * @param context
     * @return
     */
    private Object[] getArguments(Context context) {
        Object args[] = null;
        if (inputParameters != null && inputParameters.size() > 0) {
            args = new Object[inputParameters.size()];
            for (int i = 0; i < inputParameters.size(); i++) {
                args[i] = getArgument(context, i);
            }
        }
        return args;
    }

    private Object getArgument(Context context, int i) {
        Parameter des = inputParameters.get(i);
        String paramName = des.getName();
        // =============20130619修改bengin================
        Object obj = Context2ObjectUtil.getObject(des, context, loader);
        // =============20130619修改end================
        if (obj == null) {
            if (des.isRequired()) { // 如果输入参数是必须的,则抛出异常
                LOGGER.logMessage(LogLevel.ERROR, "参数{paramName}未传递", paramName);
                throw new ParamIsNullException(paramName);
            } else { // 如果输出参数非必须，直接返回null
                return null;
            }
        }
        if (!(obj instanceof String)) {
            return obj;
        }
        return ValueUtil.getValue((String) obj, des.getType());
    }

}
