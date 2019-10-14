package org.tinygroup.service.classpublisher;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.tinygroup.beancontainer.BeanContainer;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.beanutil.BeanUtil;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.service.ServiceProviderInterface;
import org.tinygroup.service.config.*;
import org.tinygroup.service.exception.ServiceLoadException;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by qiucn on 2018/3/19.
 */
public class ClassServiceFileProcessor extends XmlConfigServiceLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassServiceFileProcessor.class);
    private ServiceProviderInterface provider;
    private List<ServiceComponents> list = new ArrayList<ServiceComponents>();
    private static final String SERVICE_EXT_FILENAME = ".classservice.xml";
    private static final String SERVICE_XSTREAM_PACKAGENAME = "classservice";

    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(SERVICE_EXT_FILENAME);
    }

    public void process() {
        XStream stream = XStreamFactory.getXStream(SERVICE_XSTREAM_PACKAGENAME);
        for (FileObject fileObject : deleteList) {
            LOGGER.logMessage(LogLevel.INFO, "正在移除接口全路径配置文件[{0}]",
                    fileObject.getAbsolutePath());
            SerivceClassPath serivceClassPath = (SerivceClassPath) caches.get(fileObject.getAbsolutePath());
            if (serivceClassPath != null) {
                caches.remove(fileObject.getAbsolutePath());
                removeService(serivceClassPath.getClassPaths());
            }
            LOGGER.logMessage(LogLevel.INFO, "移除接口全路径配置文件[{0}]结束",fileObject.getAbsolutePath());
        }

        for (FileObject fileObject : changeList) {
            LOGGER.logMessage(LogLevel.INFO, "正在读取接口全路径配置文件[{0}]",
                    fileObject.getAbsolutePath());
            try {
                SerivceClassPath oldSerivceClassPath = (SerivceClassPath) caches
                        .get(fileObject.getAbsolutePath());
                if (oldSerivceClassPath != null) {
                    caches.remove(fileObject.getAbsolutePath());
                    removeService(oldSerivceClassPath.getClassPaths());
                }

                InputStream inputStream = fileObject.getInputStream();
                SerivceClassPath serivceClassPath = (SerivceClassPath) stream.fromXML(inputStream);
                try {
                    inputStream.close();
                } catch (Exception e) {
                    LOGGER.errorMessage("关闭文件流时出错,文件路径:{}", e, fileObject.getAbsolutePath());
                }
                if(serivceClassPath != null){
                    initService(serivceClassPath.getClassPaths());
                }
            } catch (Exception e) {
                LOGGER.errorMessage("读取接口全路径配置文件[{0}]出现异常", e,
                        fileObject.getAbsolutePath());
            }

            LOGGER.logMessage(LogLevel.INFO, "读取接口全路径配置文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
        try {
            LOGGER.logMessage(LogLevel.INFO, "正在注册Service");
            this.loadService(provider.getServiceRegistory(), getFileResolver()
                    .getClassLoader());
            LOGGER.logMessage(LogLevel.INFO, "注册Service结束");
        } catch (ServiceLoadException e) {
            LOGGER.errorMessage("注册Service时出现异常", e);
        }
        list.clear();// 扫描结束后清空服务列表

    }

    private void removeService(List<String> classPaths) {
        if (CollectionUtil.isEmpty(classPaths)) {
            return;
        }
        for (String classPath : classPaths) {
            try {
                Class<?> clazz = getClazz(classPath);
                Method[] methods = clazz.getMethods();
                Map<String, Object> methodNameMap = new HashedMap();
                for (Method method : methods) {
                    String methodName = getMethodName(method.getName(), methodNameMap);
                    String serviceId = classPath + "." + methodName;
                    provider.getServiceRegistory().removeService(serviceId);
                }
            } catch (ServiceLoadException e) {
                LOGGER.errorMessage("类[{0}]加载出现异常", e, classPath);
            }
        }
    }

    protected void initService(List<String> classPaths) throws ServiceLoadException {
        if (CollectionUtils.isEmpty(classPaths)) {
            return;
        }
        for (String classPath : classPaths) {
            initClassPath(classPath);
        }
    }

    private void initClassPath(String classPath) throws ServiceLoadException {
        ServiceComponents components = new ServiceComponents();

        Class<?> clazz = getClazz(classPath);
        Method[] methods = clazz.getMethods();

        ServiceComponent component = new ServiceComponent();
        component.setType(classPath);
        Object instance = getServiceInstance(component);

        List<ServiceMethod> serviceMethods = new ArrayList<ServiceMethod>();
        Map<String, Object> methodNameMap = new HashedMap();
        for (Method method : methods) {
            Method realMethod = getRealMethod(instance, method);
            String methodName = getMethodName(realMethod.getName(), methodNameMap);

            ServiceMethod serviceMethod = new ServiceMethod();
            serviceMethod.setServiceId(classPath + "." + methodName);
            serviceMethod.setLocalName(methodName);
            serviceMethod.setMethodName(realMethod.getName());

            initInputParameters(realMethod, serviceMethod);
            initOutputParameters(realMethod, serviceMethod);

            serviceMethods.add(serviceMethod);
        }
        component.setServiceMethods(serviceMethods);

        components.getServiceComponents().add(component);
        list.add(components);
        caches.put(classPath, components);
    }

    private Method getRealMethod(Object instance, Method method) throws ServiceLoadException {
        try {
            return instance.getClass().getMethod(method.getName(), method.getParameterTypes());
        } catch (NoSuchMethodException e) {
            LOGGER.errorMessage("接口：{} 方法：{} 找不到实现", e, instance.getClass().getName(), method.getName());
            throw new ServiceLoadException(e);
        }
    }

    private Class<?> getClazz(String classPath) throws ServiceLoadException {
        try {
            return getFileResolver().getClassLoader().loadClass(classPath);
        } catch (ClassNotFoundException e) {
            try {
                return Class.forName(classPath);
            } catch (ClassNotFoundException e1) {
            }
            LOGGER.errorMessage("服务发布接口不存在,接口路径:{}", e, classPath);
            throw new ServiceLoadException(e);
        }
    }

    private void initOutputParameters(Method method, ServiceMethod serviceMethod) {
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
                ParameterizedTypeImpl typeImpl = (ParameterizedTypeImpl) method.getGenericReturnType();
                Class typeClass = (Class) typeImpl.getActualTypeArguments()[0];
                serviceResult.setType(typeClass.getName());
            } else {
                serviceResult.setArray(false);
                serviceResult.setType(method.getReturnType().getName());
            }
        }
        serviceMethod.setServiceResult(serviceResult);
    }

    private void initInputParameters(Method method, ServiceMethod serviceMethod) {
        List<ServiceParameter> serviceParameters = new ArrayList<ServiceParameter>();
        Class[] types = method.getParameterTypes();
        Type[] types1 = method.getGenericParameterTypes();
        String[] parameterNames = BeanUtil.getMethodParameterName(method);
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
                ParameterizedTypeImpl typeImpl = (ParameterizedTypeImpl) types1[i];
                Class typeClass = (Class) typeImpl.getActualTypeArguments()[0];
                parameter.setType(typeClass.getName());
                parameter.setCollectionType(type.getName());
            } else {
                parameter.setType(type.getCanonicalName());
                parameter.setArray(false);
            }
            serviceParameters.add(parameter);
        }
        serviceMethod.setServiceParameters(serviceParameters);
    }

    private String getMethodName(String methodName, Map<String, Object> methodNameMap) {
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

    protected List<ServiceComponents> getServiceComponents() {
        return list;
    }

    protected Object getServiceInstance(ServiceComponent component) throws ServiceLoadException {
        BeanContainer<?> container = BeanContainerFactory
                .getBeanContainer(getFileResolver().getClassLoader());
        Class<?> clazz = getClazz(component.getType());
        return container.getBean(clazz);
    }

    public ServiceProviderInterface getProvider() {
        return provider;
    }

    public void setProvider(ServiceProviderInterface provider) {
        this.provider = provider;
    }

    @Override
    public void setConfigPath(String path) {

    }
}
