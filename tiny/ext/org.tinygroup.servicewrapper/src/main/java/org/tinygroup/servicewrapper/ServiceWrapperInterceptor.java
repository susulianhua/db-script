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
package org.tinygroup.servicewrapper;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.ParameterNameDiscoverer;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.commons.tools.Assert;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.event.Event;
import org.tinygroup.event.Parameter;
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.event.ServiceRequest;

import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

/**
 * 拦截器
 */
public class ServiceWrapperInterceptor implements MethodInterceptor {
    private CEPCore core;

    private ServiceIdAnaly serviceIdAnaly;

    private ParameterNameDiscoverer parameterNameDiscoverer;

    private String nodeName;

    public CEPCore getCore() {
        return core;
    }

    public void setCore(CEPCore core) {
        this.core = core;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public ParameterNameDiscoverer getParameterNameDiscoverer() {
        return parameterNameDiscoverer;
    }

    public void setParameterNameDiscoverer(
            ParameterNameDiscoverer parameterNameDiscoverer) {
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }

    public ServiceIdAnaly getServiceIdAnaly() {
        return serviceIdAnaly;
    }

    public void setServiceIdAnaly(ServiceIdAnaly serviceIdAnaly) {
        this.serviceIdAnaly = serviceIdAnaly;
    }

    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object[] args = invocation.getArguments();
        Method method = invocation.getMethod();
        Object target = invocation.getThis();
        if (AopUtils.isToStringMethod(method)) {
            // Allow for differentiating between the proxy and the raw Connection.
            StringBuffer buf = new StringBuffer("proxy for service");
            if (target != null) {
                buf.append("[").append(target.toString()).append("]");
            }
            return buf.toString();
        }

        String serviceId = serviceIdAnaly.analyMethod(method);
        if (StringUtil.isBlank(serviceId)) {
            throw new RuntimeException(String.format("方法:%s,未发布成服务,不能进行访问", method));
        }
        Context context = new ContextImpl();
        String[] paramNames = parameterNameDiscoverer.getParameterNames(method);
        int size = paramNames != null ? paramNames.length : 0;
        int argSize = args != null ? args.length : 0;
        if (paramNames != null && args != null) {
            Assert.assertTrue(size == argSize, "服务配置描述的参数个数与实际方法的参数个数不一致");// 参数个数必须一致
        }
        for (int i = 0; i < size; i++) {
            context.put(paramNames[i], args[i]);
        }
        return callServiceAndCallBack(serviceId, context);

    }

    private Event getEvent(String serviceId, Context context) throws Exception {
        Event event = new Event();
        event.setEventId(UUID.randomUUID().toString());
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setContext(context);
        serviceRequest.setServiceId(serviceId);
        try {
            String nodeName = InvokeNodeSetter.getNodeName();
            int eventMode = EventModeSetter.getEventMode();
            if (!StringUtil.isBlank(nodeName)) {
                serviceRequest.setNodeName(nodeName);
            }
            event.setMode(eventMode);
        } finally {
            InvokeNodeSetter.removeNodeName();
            EventModeSetter.removeEventMode();
        }
        event.setServiceRequest(serviceRequest);
        return event;
    }

    private <T> T callServiceAndCallBack(String serviceId, Context context)
            throws Exception {
        Event event = getEvent(serviceId, context);
        core.process(event);
        ServiceInfo info = core.getServiceInfo(serviceId);
        List<Parameter> resultsParam = info.getResults();
        if (resultsParam == null || resultsParam.size() == 0) {
            return null;
        }
        return event.getServiceRequest().getContext()
                .get(resultsParam.get(0).getName());
    }

}
