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
package org.tinygroup.cepcoreimpl.test.newcase.testcase;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.cepcoreimpl.CEPCoreImpl;
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;

public abstract class BaseNewCase extends BaseDataCreate {
    protected static Logger LOGGER = LoggerFactory.getLogger(BaseNewCase.class);

    protected ServiceInfo getRemoteMethod(CEPCore cepcore, String serviceId) {
        try {
            Method method = CEPCoreImpl.class.getDeclaredMethod(
                    "getRemoteServiceInfo", String.class);
            method.setAccessible(true);
            return (ServiceInfo) method.invoke(cepcore,
                    new Object[]{serviceId});
        } catch (Exception e) {
            return null;
        }
    }


    protected void assertServiceExist(CEPCore cepcore, String allExistService,
                                      String notExistService) {
        LOGGER.logMessage(LogLevel.INFO, "allExistService:{}", allExistService);
        LOGGER.logMessage(LogLevel.INFO, "notExistService:{}", notExistService);
        List<String> allExistServiceList = trans(allExistService);
        List<String> notExistServiceList = trans(notExistService);
        // 所有能查到的服务
        for (String serviceId : allExistServiceList) {
            try {
                cepcore.getServiceInfo(serviceId);
            } catch (Exception e) {
                fail();
            }

        }
        for (String serviceId : notExistServiceList) {
            try {
                cepcore.getServiceInfo(serviceId);
                fail();
            } catch (Exception e) {

            }

        }
    }

    protected void assertLocalService(CEPCore cepcore, String localServiceIds,
                                      String unRegLocalServiceIds) {
        LOGGER.logMessage(LogLevel.INFO, "localServiceIds:{}", localServiceIds);
        LOGGER.logMessage(LogLevel.INFO, "unRegLocalServiceIds:{}", unRegLocalServiceIds);
        // 本地服务列表
        List<ServiceInfo> services = cepcore.getServiceInfos();
        List<String> localServiceIdsList = trans(localServiceIds);
        List<String> unRegLocalServiceIdsList = trans(unRegLocalServiceIds);
        // 已经注册的本地服务校验
        for (String serviceId : localServiceIdsList) {
            boolean contains = false;
            for (ServiceInfo info : services) {
                if (serviceId.equals(info.getServiceId())) {
                    contains = true;
                }
            }
            if (!contains) {
                // assertTrue(false);
                fail();
            }
        }
        // 已经注销的本地服务校验
        for (String serviceId : unRegLocalServiceIdsList) {
            boolean contains = true;
            for (ServiceInfo info : services) {
                if (serviceId.equals(info.getServiceId())) {
                    contains = false;
                }
            }
            if (!contains) {
                // assertTrue(false);
                fail();
            }
        }
        for (ServiceInfo service : services) {
            localServiceIdsList.remove(service.getServiceId());
        }
        assertEquals(0, localServiceIdsList.size());

    }

    protected void assertRemoteService(CEPCore cepcore, String remoteServiceIds,
                                       String unRegRemoteServiceIds) {
        List<String> remoteServiceIdsList = trans(remoteServiceIds);
        List<String> unregRemoteServiceIdsList = trans(unRegRemoteServiceIds);
        // 如果已经注册的远程服务获取不到，则说明注册失败
        for (String serviceId : remoteServiceIdsList) {
            ServiceInfo info = getRemoteMethod(cepcore, serviceId);
            if (info == null) {
                // assertTrue(false);
                fail();
            }
        }

        // 如果已经注销的远程服务还能获取到，则说明注销失败
        for (String serviceId : unregRemoteServiceIdsList) {
            ServiceInfo info = getRemoteMethod(cepcore, serviceId);
            if (info != null) {
                // assertTrue(false);
                fail();
            }
        }

    }

    /**
     * @description：
     * @localServiceIds：已经注册的本地服务id集
     * @unRegLocalServiceIds:已经注销的本地服务id集
     * @remoteServiceIds：已经注册的远程服务id集
     * @unregRemoteServiceIds：已经注销的远程服务id集
     * @eventProcessors：处理器集
     * @author: qiucn
     * @version: 2016年8月1日上午9:24:08
     */
    protected void assertEventProcessor(CEPCore cepcore, String eventProcessors) {
        LOGGER.logMessage(LogLevel.INFO, "eventProcessors:{}", eventProcessors);
        List<String> eventProcessorsList = trans(eventProcessors);
        List<EventProcessor> list = cepcore.getEventProcessors();
        assertEquals(eventProcessorsList.size(), list.size());
        for (EventProcessor processor : list) {
            eventProcessorsList.remove(processor.getId());
        }
        assertEquals(0, eventProcessorsList.size());
    }

}
