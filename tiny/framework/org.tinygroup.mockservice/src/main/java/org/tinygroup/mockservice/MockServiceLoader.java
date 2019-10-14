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
package org.tinygroup.mockservice;

import org.tinygroup.event.Parameter;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.service.config.ServiceComponent;
import org.tinygroup.service.config.ServiceComponents;
import org.tinygroup.service.config.ServiceMethod;
import org.tinygroup.service.config.ServiceParameter;
import org.tinygroup.service.exception.ServiceLoadException;
import org.tinygroup.service.util.ServiceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockServiceLoader {
    private final static Logger LOGGER = LoggerFactory
            .getLogger(MockServiceLoader.class);
    private static Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();

    static {
        classMap.put("int", int.class);
        classMap.put("short", short.class);
        classMap.put("byte", byte.class);
        classMap.put("char", char.class);
        classMap.put("long", long.class);
        classMap.put("double", double.class);
        classMap.put("float", float.class);
        classMap.put("boolean", boolean.class);
        classMap.put("void", void.class);
    }

    private MockServiceManager manager;

    public void addService(List<ServiceComponents> list) {
        for (ServiceComponents components : list) {
            addService(components);
        }
    }

    public void addService(ServiceComponents components) {
        for (ServiceComponent serviceComponent : components
                .getServiceComponents()) {
            addService(serviceComponent);
        }
    }

    public void addService(ServiceComponent serviceComponent) {
        String className = serviceComponent.getType();
        Class<?> classType = null;
        try {
            classType = Class.forName(className);
        } catch (ClassNotFoundException e) {
            LOGGER.errorMessage("解析服务时,获取服务对应class时出错", e);
            return;
        }
        for (ServiceMethod serviceMethod : serviceComponent.getServiceMethods()) {
            try {
                MockService item = new MockService();
                item.setServiceId(serviceMethod.getServiceId());
                item.setCategory(serviceMethod.getCategory());
                item.setMethodName(serviceMethod.getMethodName());
                item.setType(classType);
                getInputParameterNames(item, serviceMethod);
                getOutputParameterNames(item, serviceMethod);
                manager.addMockService(item);
            } catch (Exception e) {
                LOGGER.errorMessage("解析服务时出错,服务id:{}", e, serviceMethod.getServiceId());
            }

        }
    }

    private void getInputParameterNames(MockService item,
                                        ServiceMethod serviceMethod) throws ClassNotFoundException,
            ServiceLoadException {
        List<Parameter> inputParameterDescriptors = new ArrayList<Parameter>();
        // ==================入参处理 begin========================
        for (ServiceParameter serviceParameter : serviceMethod
                .getServiceParameters()) {
            String type = serviceParameter.getType();
            Class<?> parameterType = classMap.get(type);
            if (parameterType == null) {
                parameterType = Class.forName(type);
                classMap.put(type, parameterType);
            }
            Parameter descriptor = new Parameter();
            if (!ServiceUtil.assignFromSerializable(parameterType)) {
                throw new ServiceLoadException("服务返回值类型:<"
                        + parameterType.getName() + ">必须实现Serializable接口");
            }
            descriptor.setType(parameterType.getName());
            descriptor.setArray(serviceParameter.isArray());
            descriptor.setName(serviceParameter.getName());
            descriptor.setRequired(serviceParameter.isRequired());
            descriptor.setValidatorSence(serviceParameter.getValidatorScene());
            descriptor.setTitle(serviceParameter.getLocalName());
            descriptor.setCollectionType(serviceParameter.getCollectionType());
            descriptor.setDescription(serviceParameter.getDescription());
            inputParameterDescriptors.add(descriptor);
        }
        // ==================入参处理 end========================
        item.setParameters(inputParameterDescriptors);
    }

    private void getOutputParameterNames(MockService item,
                                         ServiceMethod serviceMethod) throws ClassNotFoundException,
            ServiceLoadException {
        // ==================出参处理 begin========================
        if (serviceMethod.getServiceResult() != null) {
            ServiceParameter serviceResult = serviceMethod.getServiceResult();
            String type = serviceResult.getType();
            Class<?> parameterType = classMap.get(type);
            if (parameterType == null) {
                parameterType = Class.forName(type);
                classMap.put(type, parameterType);
            }
            Parameter descriptor = new Parameter();
            if (!ServiceUtil.assignFromSerializable(parameterType)) {
                throw new ServiceLoadException("服务返回值类型:<"
                        + parameterType.getName() + ">必须实现Serializable接口");
            }
            descriptor.setType(parameterType.getName());
            descriptor.setArray(serviceResult.isArray());
            descriptor.setRequired(serviceResult.isRequired());
            descriptor.setName(serviceResult.getName());
            descriptor.setValidatorSence(serviceResult.getValidatorScene());
            descriptor.setTitle(serviceResult.getLocalName());
            descriptor.setDescription(serviceResult.getDescription());
            descriptor.setCollectionType(serviceResult.getCollectionType());
            List<Parameter> outputParameterDescriptors = new ArrayList<Parameter>();
            outputParameterDescriptors.add(descriptor);
            item.setResults(outputParameterDescriptors);
        }
        // ==================出参处理 end========================
    }

    public MockServiceManager getManager() {
        return manager;
    }

    public void setManager(MockServiceManager manager) {
        this.manager = manager;
    }

}
