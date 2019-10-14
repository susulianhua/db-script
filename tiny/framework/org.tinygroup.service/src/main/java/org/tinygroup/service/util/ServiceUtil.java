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
package org.tinygroup.service.util;

import org.apache.commons.collections.map.HashedMap;
import org.tinygroup.commons.parameterdiscover.util.ParanamerBeanUtil;
import org.tinygroup.commons.serviceid.ServiceIdGenerateRule;
import org.tinygroup.commons.spring.util.AopTargetUtil;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.service.config.ServiceComponent;
import org.tinygroup.service.config.ServiceMethod;
import org.tinygroup.service.config.ServiceParameter;
import org.tinygroup.service.exception.ServiceLoadException;
import org.tinygroup.service.registry.ServiceRegistryItem;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServiceUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceUtil.class);
    public static boolean assignFromSerializable(Class<?> clazz) {
        boolean isPrimitive = clazz.isPrimitive();
        if (isPrimitive) {
            return true;
        }
        boolean isInterface = clazz.isInterface();
        if (isInterface) {
            return true;
        }
        boolean isMapTypes = Map.class.isAssignableFrom(clazz);
        if (isMapTypes) {
            return true;
        }
        if (clazz == Object.class) {
            return true;
        }
        boolean seriaType = Serializable.class.isAssignableFrom(clazz);
        return seriaType;
    }

    public static ServiceRegistryItem copyServiceItem(ServiceRegistryItem serviceiItem) {
        ServiceRegistryItem item = new ServiceRegistryItem();
        item.setCategory(serviceiItem.getCategory());
        item.setDescription(serviceiItem.getDescription());
        item.setLocalName(serviceiItem.getLocalName());
        item.setParameters(serviceiItem.getParameters());
        item.setResults(serviceiItem.getResults());
        item.setService(serviceiItem.getService());
        item.setRequestTimeout(serviceiItem.getRequestTimeout());
        return item;
    }

    /**
     *  若Component对象下属没有ServiceMethod对象，则将Component对应的类的所有方法都生成ServiceMethod
     * @param instance 对象实例
     * @param component 服务描述对象
     * @param serviceIdGenerateRule 服务id生成规则
     * @throws ServiceLoadException
     */
    public static void supplementServiceComponent(Object instance,
                                                  ServiceComponent component,ServiceIdGenerateRule serviceIdGenerateRule)throws ServiceLoadException {

        if(CollectionUtil.isEmpty( component.getServiceMethods())) {
            generateServiceMethod(instance, component,serviceIdGenerateRule);
        }
    }

    public static void generateServiceMethod(Class clazz,ServiceComponent component,ServiceIdGenerateRule serviceIdGenerateRule) throws ServiceLoadException {
        List<ServiceMethod> serviceMethods = new ArrayList<ServiceMethod>();
        Map<String, Object> methodNameMap = new HashedMap();
        Method[] methods = getClassMethods(clazz);
        for (Method method : methods) {
            Method realMethod = getRealMethod(clazz, method);
            String methodName = getMethodName(realMethod.getName(), methodNameMap);

            ServiceMethod serviceMethod = new ServiceMethod();
            serviceMethod.setServiceId(serviceIdGenerateRule.generateServiceId(clazz,method));
            serviceMethod.setLocalName(methodName);
            serviceMethod.setMethodName(realMethod.getName());

            initInputParameters(realMethod, serviceMethod);
            initOutputParameters(realMethod, serviceMethod);

            serviceMethods.add(serviceMethod);
        }
        component.setServiceMethods(serviceMethods);

    }

    private static void generateServiceMethod(Object instance, ServiceComponent component,ServiceIdGenerateRule serviceIdGenerateRule) throws ServiceLoadException {
        List<ServiceMethod> serviceMethods = new ArrayList<ServiceMethod>();
        Map<String, Object> methodNameMap = new HashedMap();
        Class<?>  clazz = getClazz(component.getType(),instance);
        Method[] methods = getClassMethods(clazz);
        for (Method method : methods) {
            Method realMethod = getRealMethod(instance, method);
            String methodName = getMethodName(realMethod.getName(), methodNameMap);

            ServiceMethod serviceMethod = new ServiceMethod();
            serviceMethod.setServiceId(serviceIdGenerateRule.generateServiceId(clazz,method));
            serviceMethod.setLocalName(methodName);
            serviceMethod.setMethodName(realMethod.getName());

            initInputParameters(realMethod, serviceMethod);
            initOutputParameters(realMethod, serviceMethod);

            serviceMethods.add(serviceMethod);
        }
        component.setServiceMethods(serviceMethods);

    }
    private static Method getRealMethod(Object instance, Method method) throws ServiceLoadException {
        return getRealMethod(AopTargetUtil.getMixProxyTarget(instance).getClass(),method);
    }

    private static Method getRealMethod(Class clazz, Method method) throws ServiceLoadException {
        try {
            return clazz.getMethod(method.getName(), method.getParameterTypes());
        } catch (NoSuchMethodException e) {
            LOGGER.errorMessage("接口：{} 方法：{} 找不到实现", e, clazz.getName(), method.getName());
            throw new ServiceLoadException(e);
        }
    }

    private static String getMethodName(String methodName, Map<String, Object> methodNameMap) {
        String name = methodName;
        int index = 1;
        do {
            if (methodNameMap.containsKey(name)) {
                name += index;
                index++;
            } else {
                methodNameMap.put(name, null);
            }
        } while (!methodNameMap.containsKey(name));

        return name;
    }
    private static void initInputParameters(Method method, ServiceMethod serviceMethod) {
        List<ServiceParameter> serviceParameters = new ArrayList<ServiceParameter>();
        serviceMethod.setServiceParameters(serviceParameters);
        Class[] types = method.getParameterTypes();
        Type[] types1 = method.getGenericParameterTypes();
        String[] parameterNames = ParanamerBeanUtil.getMethodParameterName(method);
        if(parameterNames==null){
            return;
        }
        for (int i = 0; i < types.length; i++) {
            Class<?> type = types[i];
            ServiceParameter parameter = new ServiceParameter();
            parameter.setRequired(false);
            parameter.setName(parameterNames[i]);
            if (type.isArray()) {//数组
                parameter.setType(type.getCanonicalName().replace("[", "").replace("]", "").trim());
                parameter.setArray(true);
            } else if (type.isInterface() && type.isAssignableFrom(List.class)) {//集合
                parameter.setArray(false);
                if(types1[i] instanceof ParameterizedTypeImpl){
                    parameter.setType(parseClassType((ParameterizedTypeImpl) types1[i]).getName());
                }else{
                    parameter.setType(Object.class.getName());
                }
                parameter.setCollectionType(type.getName());
            } else {
                parameter.setType(type.getCanonicalName());
                parameter.setArray(false);
            }
            serviceParameters.add(parameter);
        }

    }
    private static void initOutputParameters(Method method, ServiceMethod serviceMethod) {
        ServiceParameter serviceResult = new ServiceParameter();
        serviceResult.setRequired(false);
        serviceResult.setName("result");
        if (void.class.equals(method.getReturnType())) {
            serviceResult.setType("void");
            serviceResult.setArray(false);
        } else {
            if (method.getReturnType().isArray()) {
                serviceResult.setType(method.getReturnType().getCanonicalName().replace("[", "").replace("]", "").trim());
                serviceResult.setArray(true);
            } else if (method.getReturnType().isInterface() && method.getReturnType().isAssignableFrom(List.class)) {
                serviceResult.setArray(false);
                serviceResult.setCollectionType(method.getReturnType().getName());
                if( method.getGenericReturnType() instanceof  ParameterizedTypeImpl){
                    serviceResult.setType(parseClassType((ParameterizedTypeImpl) method.getGenericReturnType()).getName());
                }else{
                    serviceResult.setType(Object.class.getName());
                }
            } else {
                serviceResult.setArray(false);
                serviceResult.setType(method.getReturnType().getName());
            }
        }
        serviceMethod.setServiceResult(serviceResult);
    }

    private static Class parseClassType(ParameterizedTypeImpl typeImpl){
       if(typeImpl.getActualTypeArguments()[0] instanceof  ParameterizedTypeImpl) {
           ParameterizedTypeImpl typeObject = (ParameterizedTypeImpl)(typeImpl.getActualTypeArguments()[0]);
           return (Class)typeObject.getActualTypeArguments()[0];
       }else{
           return (Class) typeImpl.getActualTypeArguments()[0];
       }
    }


    private static Method[] getClassMethods(Class<?> clazz) {
        if(clazz.isInterface()) {
            return clazz.getMethods();
        }
        List<Method> list = new ArrayList<Method>();
        Method[] methods = clazz.getMethods();
        for(Method m:methods) {
            if (Object.class.getName().equals(m.getDeclaringClass().getName())) {
                continue;
            }
            list.add(m);
        }
        return list.toArray(new Method[list.size()]);
    }
    private static Class<?> getClazz(String classPath,Object instance) throws ServiceLoadException {
        if(StringUtil.isBlank(classPath)){
            return instance.getClass();
        }
        try {
            return Class.forName(classPath);
        } catch (ClassNotFoundException e) {
            LOGGER.errorMessage("服务发布接口不存在,接口路径:{}", e, classPath);
            throw new ServiceLoadException(e);
        }


    }

}
