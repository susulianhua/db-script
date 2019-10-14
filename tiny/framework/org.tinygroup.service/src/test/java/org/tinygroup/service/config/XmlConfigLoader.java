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

import com.thoughtworks.xstream.XStream;
import org.tinygroup.beancontainer.BeanContainer;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.vfs.FileObject;

import java.util.ArrayList;
import java.util.List;

public class XmlConfigLoader extends XmlConfigServiceLoader {


    protected List<ServiceComponents> getServiceComponents() {
        ServiceComponents serviceComponents = new ServiceComponents();
        List<ServiceComponent> serviceComponentList = new ArrayList<ServiceComponent>();
        serviceComponents.setServiceComponents(serviceComponentList);
        ServiceComponent serviceComponent = new ServiceComponent();
        serviceComponentList.add(serviceComponent);
        serviceComponent.setType("org.tinygroup.service.config.Hello");
        List<ServiceMethod> serviceMethods = new ArrayList<ServiceMethod>();
        serviceComponent.setServiceMethods(serviceMethods);
        ServiceMethod serviceMethod = new ServiceMethod();
        serviceMethods.add(serviceMethod);
        serviceMethod.setLocalName("你好");
        serviceMethod.setServiceId("hello");
        serviceMethod.setMethodName("sayHello");
        ServiceParameter serviceResult = new ServiceParameter();
        serviceResult.setArray(false);
        serviceResult.setRequired(true);
        serviceResult.setType("java.lang.String");
        serviceResult.setName("helloResult");
        serviceMethod.setServiceResult(serviceResult);
        List<ServiceParameter> serviceParameters = new ArrayList<ServiceParameter>();
        serviceMethod.setServiceParameters(serviceParameters);
        ServiceParameter serviceParameter = new ServiceParameter();
        serviceParameters.add(serviceParameter);
        serviceParameter.setArray(false);
        serviceParameter.setRequired(true);
        serviceParameter.setType("java.lang.String");
        serviceParameter.setName("name");
        XStream xstream = new XStream();
        xstream.autodetectAnnotations(true);
        System.out.println(xstream.toXML(serviceComponents));
        List<ServiceComponents> list = new ArrayList<ServiceComponents>();
        list.add(serviceComponents);
        return list;
    }

    public void setConfigPath(String path) {

    }

    protected Object getServiceInstance(ServiceComponent component)
            throws Exception {
        BeanContainer container = BeanContainerFactory.getBeanContainer(this.getClass().getClassLoader());
        if (component.getBean() == null
                || "".equals(component.getBean().trim())) {
            Class<?> clazz = Class.forName(component.getType());
            return container.getBean(clazz);
        }
        return container.getBean(component.getBean());
    }

    @Override
    protected boolean checkMatch(FileObject fileObject) {
        return false;
    }

}
