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
package org.tinygroup.event;

import org.tinygroup.context.Context;
import org.tinygroup.context.util.ContextFactory;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

/**
 * 事件
 * <p/>
 * <br>
 * 一个事件，可以是一个服务请求，也可以是多个
 *
 * @author luoguo
 */
public final class Event implements Serializable {
    // 请求
    public static final int EVENT_TYPE_REQUEST = 1;
    // 响应
    public static final int EVENT_TYPE_RESPONSE = 2;
    // 同步
    public static final int EVENT_MODE_SYNCHRONOUS = 1;
    // 异步
    public static final int EVENT_MODE_ASYNCHRONOUS = 2;
    private static final long serialVersionUID = -1789684514322963288L;
    // 类型，用于标示是请求还是返回事件
    private int type = EVENT_TYPE_REQUEST;
    // EventID唯一标识
    private String eventId;
    // 服务请求信息描述
    private ServiceRequest serviceRequest;
    // 如果服务发生异常，此处存放异常信息
    private Throwable throwable;
    // 优先级
    private int priority;
    // 调用模式(同步/异步)
    private int mode = EVENT_MODE_SYNCHRONOUS;
    // 是否分组方式，如果是分组模式，则所有服务提供者都将被调用，默认是非分组方式
    private boolean groupMode = false;

    public Event() {
        this.eventId = UUID.randomUUID().toString();
    }

    public Event(String eventId) {
        this.eventId = eventId;
    }

    public static Event createEvent(String serviceId, String nodeName, Context context) {
        Event event = createEvent(serviceId, context);
        event.getServiceRequest().setNodeName(nodeName);
        return event;
    }

    public static Event createEvent(String serviceId, Context context) {
        Event event = new Event();
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setServiceId(serviceId);
        serviceRequest.setContext(context);
        event.setServiceRequest(serviceRequest);
        return event;
    }

    public static Event copy(Event event) {
        String serviceId = event.getServiceRequest().getServiceId();
        Context context = ContextFactory.getContext();
        Event asyEvent = Event.createEvent(serviceId, context);
        Context oldContext = event.getServiceRequest().getContext();
        context.putAll(oldContext.getItemMap());
        if (!oldContext.getSubContextMap().isEmpty()) {
            Set<String> subKeys = oldContext.getSubContextMap().keySet();
            for (String subKey : subKeys) {
                context.putSubContext(subKey, oldContext.getSubContextMap().get(subKey));
            }
        }
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setServiceId(serviceId);
        serviceRequest.setContext(context);
        serviceRequest.setNodeName(event.getServiceRequest().getNodeName());
        asyEvent.setServiceRequest(serviceRequest);
        asyEvent.setEventId(event.getEventId());
        asyEvent.setGroupMode(event.getGroupMode());
        asyEvent.setMode(event.getMode());
        asyEvent.setPriority(event.getPriority());
        return asyEvent;
    }

    public boolean getGroupMode() {
        return groupMode;
    }

    public void setGroupMode(boolean groupMode) {
        this.groupMode = groupMode;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public ServiceRequest getServiceRequest() {
        return serviceRequest;
    }

    public void setServiceRequest(ServiceRequest serviceRequest) {
        this.serviceRequest = serviceRequest;
    }
}
