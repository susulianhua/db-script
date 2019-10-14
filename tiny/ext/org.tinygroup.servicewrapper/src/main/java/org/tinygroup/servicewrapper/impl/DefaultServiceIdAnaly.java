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

import org.springframework.core.annotation.AnnotationUtils;
import org.tinygroup.commons.serviceid.ClassMethodServiceIdGenerateRule;
import org.tinygroup.commons.serviceid.DefaultServiceIdGenerateRule;
import org.tinygroup.commons.serviceid.ServiceIdGenerateRule;
import org.tinygroup.servicewrapper.ServiceIdAnaly;
import org.tinygroup.servicewrapper.ServiceWrapperConfigManager;
import org.tinygroup.servicewrapper.annotation.ServiceWrapper;
import org.tinygroup.servicewrapper.config.MethodConfig;
import org.tinygroup.servicewrapper.config.ParameterType;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DefaultServiceIdAnaly implements ServiceIdAnaly {

    private ServiceWrapperConfigManager manager;

    private ServiceIdGenerateRule serviceIdGenerateRule=new ClassMethodServiceIdGenerateRule();

    public ServiceWrapperConfigManager getManager() {
        return manager;
    }

    public void setManager(ServiceWrapperConfigManager manager) {
        this.manager = manager;
    }

    public ServiceIdGenerateRule getServiceIdGenerateRule() {
        return serviceIdGenerateRule;
    }

    public void setServiceIdGenerateRule(ServiceIdGenerateRule serviceIdGenerateRule) {
        this.serviceIdGenerateRule = serviceIdGenerateRule;
    }

    public String analyMethod(Method method) {
        String serviceId = manager.getServiceIdWithMethod(method);
        if (serviceId == null) {
            ServiceWrapper serviceWrapper = AnnotationUtils.findAnnotation(method,
                    ServiceWrapper.class);
            if (serviceWrapper == null) {
                serviceId = serviceIdGenerateRule.generateServiceId(method.getDeclaringClass(), method);
            } else {
                serviceId = serviceWrapper.serviceId();
            }
            if (serviceId != null) {
                MethodConfig methodConfig = createMethodConfig(method);
                methodConfig.setServiceId(serviceId);
                manager.putServiceWrapper(methodConfig);
            }
        }
        return serviceId;
    }

    private MethodConfig createMethodConfig(Method method) {
        MethodConfig serviceWrapperConfig = new MethodConfig();
        serviceWrapperConfig.setType(method.getDeclaringClass().getName());
        serviceWrapperConfig.setMethodName(method.getName());
        Class<?>[] types = method.getParameterTypes();
        List<ParameterType> parameterTypeList = new ArrayList<ParameterType>();
        for (int i = 0; i < types.length; i++) {
            Class<?> type = types[i];
            ParameterType ptype = new ParameterType();
            ptype.setType(type.getName());
            parameterTypeList.add(ptype);
        }
        serviceWrapperConfig.setParamTypes(parameterTypeList);
        return serviceWrapperConfig;
    }

}
