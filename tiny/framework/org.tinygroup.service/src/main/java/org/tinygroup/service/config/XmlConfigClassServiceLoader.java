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
package org.tinygroup.service.config;

import org.tinygroup.commons.serviceid.ClassMethodServiceIdGenerateRule;
import org.tinygroup.commons.serviceid.ServiceIdGenerateRule;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.service.exception.ServiceLoadException;
import org.tinygroup.service.registry.ServiceRegistry;
import org.tinygroup.service.util.ServiceUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class XmlConfigClassServiceLoader extends XmlConfigServiceLoader{
//       private static final Logger LOGGER = LoggerFactory.getLogger(XmlConfigClassServiceLoader.class);
       private Map<String,ServiceComponent> tempComponents = new HashMap<String, ServiceComponent>();
       private ServiceIdGenerateRule serviceIdGenerateRule;
       public  ServiceIdGenerateRule getServiceIdGenerateRule() {
            if(serviceIdGenerateRule==null){
                serviceIdGenerateRule = new ClassMethodServiceIdGenerateRule();
            }
            return serviceIdGenerateRule;
       }

       public  void setServiceIdGenerateRule(ServiceIdGenerateRule serviceIdGenerateRule) {
            this.serviceIdGenerateRule = serviceIdGenerateRule;
       }
       protected void registerServices(Object instance,
                                        ServiceComponent component, ServiceRegistry serviceRegistry, ClassLoader classLoader)
                throws ClassNotFoundException, ServiceLoadException {
            if(CollectionUtil.isEmpty( component.getServiceMethods())) {
                ServiceUtil.supplementServiceComponent(instance, component,getServiceIdGenerateRule());
                tempComponents.put(component.getType(), component);
            }
            super.registerServices(instance,component,serviceRegistry,classLoader);
       }

       public void removeServiceComponents(ServiceRegistry serviceRegistry,
                                            ServiceComponents serviceComponents) {
            for (ServiceComponent component : serviceComponents
                    .getServiceComponents()) {
                String type = component.getType();
                List<ServiceMethod> list = component.getServiceMethods();
                if(tempComponents.containsKey(type)){
                    list = tempComponents.remove(type).getServiceMethods();
                }
                for (ServiceMethod method : list) {
                    serviceRegistry.removeService(method.getServiceId());
                }

            }
        }

}
