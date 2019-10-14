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

import org.tinygroup.beancontainer.BeanContainer;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.aop.CEPCoreAopManager;
import org.tinygroup.context.Context;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceRequest;
import org.tinygroup.service.ServiceProviderInterface;
import org.tinygroup.tinytestutil.AbstractTestUtil;

public class ServiceTestUtil {
    private static boolean init = false;

    public static void setInit(boolean init) {
        ServiceTestUtil.init = init;
    }

    public static void init() {
        if (!init) {
            AbstractTestUtil.init("application.xml", true);
            initAopManager();
            init = true;
        }

    }

    private static void initAopManager() {
        BeanContainer contaner = BeanContainerFactory.getBeanContainer(ServiceTestUtil.class.getClassLoader());
        CEPCoreAopManager aopManager = (CEPCoreAopManager) contaner.getBean(CEPCoreAopManager.CEPCORE_AOP_BEAN);
        aopManager.addAopAdapter(CEPCoreAopManager.BEFORE_LOCAL, "requestParamValidate", null);
        CEPCore core = (CEPCore) contaner.getBean(CEPCore.CEP_CORE_BEAN);
        EventProcessorForValidate ep = new EventProcessorForValidate();

        ServiceProviderInterface provider = (ServiceProviderInterface) contaner.getBean("service");
        ep.setProvider(provider);
        ep.getServiceInfos().addAll(provider.getServiceRegistory().getServiceRegistryItems());
        core.registerEventProcessor(ep);
    }

    public static void execute(String serviceId, Context context) {
//        init();
//        BeanContainer contaner = BeanContainerFactory.getBeanContainer(ServiceTestUtil.class.getClassLoader());
        ServiceProviderInterface provider = getProvider();
        provider.execute(serviceId, context);
    }

    public static ServiceProviderInterface  getProvider() {
        init();
        BeanContainer contaner = BeanContainerFactory.getBeanContainer(ServiceTestUtil.class.getClassLoader());
        ServiceProviderInterface provider = (ServiceProviderInterface) contaner.getBean("service");
        return provider;
    }

    public static void executeForValidate(String serviceId, Context context) {
        init();
        BeanContainer contaner = BeanContainerFactory.getBeanContainer(ServiceTestUtil.class.getClassLoader());

        Event e = new Event();
        ServiceRequest sr = new ServiceRequest();
        e.setServiceRequest(sr);
        sr.setServiceId(serviceId);
        sr.setContext(context);


        CEPCore core = (CEPCore) contaner.getBean(CEPCore.CEP_CORE_BEAN);
        core.process(e);

    }

}
