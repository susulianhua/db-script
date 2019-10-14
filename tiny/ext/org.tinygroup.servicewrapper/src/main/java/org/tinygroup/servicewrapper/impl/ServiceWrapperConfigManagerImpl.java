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
package org.tinygroup.servicewrapper.impl;

import org.springframework.util.ReflectionUtils;
import org.tinygroup.commons.tools.Assert;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.servicewrapper.ServiceWrapperConfigManager;
import org.tinygroup.servicewrapper.config.MethodConfig;
import org.tinygroup.servicewrapper.config.MethodConfigs;
import org.tinygroup.servicewrapper.config.MethodDescription;
import org.tinygroup.servicewrapper.config.ParameterType;
import org.tinygroup.servicewrapper.exception.ServiceWrapperException;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceWrapperConfigManagerImpl implements
        ServiceWrapperConfigManager {
    private Map<MethodDescription, String> methodActionMap = new HashMap<MethodDescription, String>();

    public void addServiceWrappers(MethodConfigs serviceWrappers) {
        List<MethodConfig> methodConfigList = serviceWrappers
                .getMethodConfigs();
        for (MethodConfig methodConfig : methodConfigList) {
            putServiceWrapper(methodConfig);
        }
    }

    public void removeServiceWrappers(MethodConfigs serviceWrappers) {
        List<MethodConfig> methodConfigList = serviceWrappers
                .getMethodConfigs();
        for (MethodConfig methodConfig : methodConfigList) {
            MethodDescription description = new MethodDescription();
            description.setClassName(methodConfig.getType());
            description.setMethodName(methodConfig.getMethodName());
            List<ParameterType> paramTypes = methodConfig.getParamTypes();
            StringBuilder typeBuilder = new StringBuilder();
            for (int i = 0; i < paramTypes.size(); i++) {
                typeBuilder.append(paramTypes.get(i).getType());
                if (i < paramTypes.size() - 1) {
                    typeBuilder.append(";");
                }
            }
            description.setParameterTypes(typeBuilder.toString());
            methodActionMap.remove(description);
        }
    }

    public String getServiceIdWithMethod(Method method) {
        MethodDescription description = MethodDescription
                .createMethodDescription(method);
        return methodActionMap.get(description);
    }

    public void putServiceWrapper(final MethodConfig serviceWrapper) {
        final String type = serviceWrapper.getType();
        Assert.assertNotNull(type, "type must not be null");
        final String methodName = serviceWrapper.getMethodName();
        List<ParameterType> paramTypes = serviceWrapper.getParamTypes();
        if (StringUtil.isBlank(methodName) && paramTypes.size() == 0) {
            //TODO 反射类所有公共方法
            Class<?> clazz = null;
            try {
                clazz = Class.forName(type);
            } catch (ClassNotFoundException e) {
                throw new ServiceWrapperException(e);
            }
            ReflectionUtils.doWithLocalMethods(clazz, new ReflectionUtils.MethodCallback() {
                @Override
                public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                    MethodDescription description = new MethodDescription();
                    description.setClassName(type);
                    description.setMethodName(method.getName());
                    StringBuilder typeBuilder = new StringBuilder();
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    for (int i = 0; i < parameterTypes.length; i++) {
                        Class<?> parameterType = parameterTypes[i];
                        typeBuilder.append(parameterType.getName());
                        if (i < parameterTypes.length - 1) {
                            typeBuilder.append(";");
                        }
                    }
                    description.setParameterTypes(typeBuilder.toString());
                    methodActionMap.put(description, type + "." + method.getName());
                }
            });
        } else {
            MethodDescription description = new MethodDescription();
            description.setClassName(serviceWrapper.getType());
            description.setMethodName(serviceWrapper.getMethodName());
            StringBuilder typeBuilder = new StringBuilder();
            for (int i = 0; i < paramTypes.size(); i++) {
                typeBuilder.append(paramTypes.get(i).getType());
                if (i < paramTypes.size() - 1) {
                    typeBuilder.append(";");
                }
            }
            description.setParameterTypes(typeBuilder.toString());
            methodActionMap.put(description, serviceWrapper.getServiceId());
        }
    }

}
