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
package org.tinygroup.flowservicecomponent;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.context.Context;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceRequest;
import org.tinygroup.flow.ComponentInterface;

import java.util.UUID;

public class CallFlow implements ComponentInterface {
    String flowId;
    String version;
    CEPCore cepCore;


    public CEPCore getCepCore() {
        return cepCore;
    }

    public void setCepCore(CEPCore cepCore) {
        this.cepCore = cepCore;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void execute(Context context) {
        // = (CEPCore)SpringBeanContainer.getBean("cepcore");
        Event event = getEvent(context);
        cepCore.process(event);
    }

    private Event getEvent(Context context) {
        Event event = new Event();
        event.setEventId(UUID.randomUUID().toString());
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setContext(context);
        if (notNull(flowId)) {
            serviceRequest.setServiceId(flowId);
        }
        event.setServiceRequest(serviceRequest);
        return event;
    }

    private boolean notNull(String str) {
        return str != null && !"".equals(str);
    }

}
