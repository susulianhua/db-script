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
package org.tinygroup.service.registry.impl;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.service.Service;
import org.tinygroup.service.registry.ServiceRegistry;
import org.tinygroup.service.registry.ServiceRegistryItem;

import java.util.*;

public class ServiceRegistryImpl implements ServiceRegistry {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ServiceRegistryImpl.class);
    /**
     * 指定服务ID，默认服务MAP
     */
    private final Map<String, ServiceRegistryItem> serviceIdMap = new HashMap<String, ServiceRegistryItem>();
    private final Map<Service, ServiceRegistryItem> serviceToServiceRegistryItem = new HashMap<Service, ServiceRegistryItem>();
    private boolean change = true;

    public void registerService(ServiceRegistryItem serviceRegistryItem) {
        change = true;
        if (serviceIdMap.containsKey(serviceRegistryItem.getServiceId())) {
            LOGGER.logMessage(LogLevel.WARN, "服务号:[{0}]已经存在,之前的服务将被覆盖",
                    serviceRegistryItem.getServiceId());
            ServiceRegistryItem item = serviceIdMap.get(serviceRegistryItem.getServiceId());
            if (item != null) {
                serviceToServiceRegistryItem.remove(item.getService());
            }
        }
        LOGGER.logMessage(LogLevel.INFO, "添加服务[serviceId:{0}]",
                serviceRegistryItem.getServiceId());
        serviceIdMap.put(serviceRegistryItem.getServiceId(),
                serviceRegistryItem);
        serviceToServiceRegistryItem.put(serviceRegistryItem.getService(),
                serviceRegistryItem);

    }

    public void registerService(List<ServiceRegistryItem> serviceRegistryItems) {
        for (ServiceRegistryItem serviceRegistryItem : serviceRegistryItems) {
            registerService(serviceRegistryItem);
        }
    }

    public void registerService(ServiceRegistryItem[] serviceRegistryItems) {
        for (ServiceRegistryItem serviceRegistryItem : serviceRegistryItems) {
            registerService(serviceRegistryItem);
        }
    }

    public void registerService(Set<ServiceRegistryItem> serviceRegistryItems) {
        for (ServiceRegistryItem serviceRegistryItem : serviceRegistryItems) {
            registerService(serviceRegistryItem);
        }
    }

    public void removeService(String serviceId) {
        removeService(serviceIdMap.get(serviceId));

    }

    private void removeService(ServiceRegistryItem serviceRegistryItem) {
        if (serviceRegistryItem == null) {
            return;
        }
        change = true;
        LOGGER.logMessage(LogLevel.INFO, "删除服务[serviceId:{0}]",
                serviceRegistryItem.getServiceId());
        ServiceRegistryItem removedItem = serviceIdMap
                .remove(serviceRegistryItem.getServiceId());
        serviceToServiceRegistryItem.remove(removedItem.getService());
    }

    public int size() {
        return serviceToServiceRegistryItem.size();
    }

    public void clear() {
        serviceIdMap.clear();
        serviceToServiceRegistryItem.clear();
    }

    public ServiceRegistryItem getServiceRegistryItem(Service service) {
        return serviceToServiceRegistryItem.get(service);
    }

    public ServiceRegistryItem getServiceRegistryItem(String serviceId) {
        return serviceIdMap.get(serviceId);
    }

    public Collection<ServiceRegistryItem> getServiceRegistryItems() {

        return serviceIdMap.values();
    }

    public boolean isChange() {
        return change;
    }

    public void setChange(boolean change) {
        this.change = change;
    }


}
