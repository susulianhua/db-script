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
package org.tinygroup.service.mbean;

import org.tinygroup.service.registry.ServiceRegistry;

public class ServiceMonitor implements ServiceMonitorMBean {

    ServiceRegistry serviceRegistry;

    public ServiceRegistry getServiceRegistry() {
        return serviceRegistry;
    }

    public void setServiceRegistry(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    public Integer getServiceTotal() {
        Integer total = 0;
        if (!serviceRegistry.getServiceRegistryItems().isEmpty()) {
            total = serviceRegistry.getServiceRegistryItems().size();
        }
        return total;
    }

    public boolean isExistLocalService(String serviceId) {
        boolean exist = false;
        if (!serviceRegistry.getServiceRegistryItems().isEmpty()) {
            if (serviceRegistry.getServiceRegistryItem(serviceId) != null) {
                exist = true;
            }
        }
        return exist;
    }
}

	