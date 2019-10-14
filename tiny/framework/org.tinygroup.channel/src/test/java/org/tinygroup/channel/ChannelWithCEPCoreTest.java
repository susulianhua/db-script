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
package org.tinygroup.channel;

import junit.framework.TestCase;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceRequest;

public class ChannelWithCEPCoreTest extends TestCase {
    ChannelInterface channelSample;

    protected void setUp() throws Exception {
        super.setUp();
        // BeanContainerFactory.setBeanContainer(SpringBeanContainer.class.getName());
//		CEPCore cepCore = BeanContainerFactory.getBeanContainer(
//				this.getClass().getClassLoader())
//				.getBean(CEPCore.CEP_CORE_BEAN);
        CEPCore cepCore = new CEPCoreForTest();
        channelSample = new ChannelSample();
        channelSample.setCepCore(cepCore
        );
        EventFilter eventFilter = new EventFilter() {
            public Event filter(Event event) {
                Event e = event;
                return e;
            }
        };
        channelSample.addSendEventFilter(eventFilter);
        channelSample.addReceiveEventFilter(eventFilter);
        EventListener eventListener = new EventListener() {
            public void process(Event event) {
                System.out.println(String.format("Log:%s", event
                        .getServiceRequest().getServiceId()));
            }
        };
        channelSample.addSendEventListener(eventListener);
        channelSample.addReceiveEventListener(eventListener);
        cepCore.registerEventProcessor(channelSample);
        cepCore.registerEventProcessor(new EventProcessor1());
        cepCore.registerEventProcessor(new EventProcessor2());
    }

    public void testSendEvent() {
        Event event = getEvent("aabbcc", "", "", "");
        channelSample.sendEvent(event);
        assertEquals("aa", event.getServiceRequest().getContext().get("result"));
        event = getEvent("111111", "aabbcc1", "a", "a");
        channelSample.sendEvent(event);
        assertEquals("bb", event.getServiceRequest().getContext().get("result"));
    }

    public void testSendEvent1() {
        Event event = getEvent("111111", "111111", "a", "a");
        channelSample.sendEvent(event);
        assertEquals("bb", event.getServiceRequest().getContext().get("result"));
    }

    private Event getEvent(String id, String name, String artifactId,
                           String groupId) {
        Event event = new Event();
        event.setEventId("123");
        ServiceRequest serviceRequest = new ServiceRequest();
        event.setServiceRequest(serviceRequest);
        serviceRequest.setServiceId(id);
        serviceRequest.setContext(new ContextImpl());
        return event;
    }
}
