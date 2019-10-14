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
package org.tinygroup.channel.impl;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.channel.ChannelInterface;
import org.tinygroup.channel.EventFilter;
import org.tinygroup.channel.EventListener;
import org.tinygroup.event.Event;
import org.tinygroup.event.central.Node;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象通道，实现了事件相关，日志
 *
 * @author luoguo
 */
public abstract class AbstractChannel implements ChannelInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractChannel.class);
    private List<EventFilter> sendEventFilters = new ArrayList<EventFilter>();
    private List<EventFilter> receiveEventFilters = new ArrayList<EventFilter>();
    private List<EventListener> sendEventListeners = new ArrayList<EventListener>();
    private List<EventListener> receiveEventListeners = new ArrayList<EventListener>();
    private String id;
    private CEPCore cepCore;

    public CEPCore getCepCore() {
        return cepCore;
    }

    public void setCepCore(CEPCore cepCore) {
        this.cepCore = cepCore;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean canProcess(String nodeString){
        throw new RuntimeException("not support");
    }
    public List<EventFilter> getSendEventFilters() {
        return sendEventFilters;
    }

    public List<EventFilter> getReceiveEventFilters() {
        return receiveEventFilters;
    }

    public List<EventListener> getSendEventListeners() {
        return sendEventListeners;
    }

    public List<EventListener> getReceiveEventListeners() {
        return receiveEventListeners;
    }

    public void addSendEventListener(EventListener eventListener) {
        sendEventListeners.add(eventListener);

    }

    public void addReceiveEventListener(EventListener eventListener) {
        receiveEventListeners.add(eventListener);

    }

    public void addSendEventFilter(EventFilter eventFilter) {
        sendEventFilters.add(eventFilter);
    }

    public void addReceiveEventFilter(EventFilter eventFilter) {
        receiveEventFilters.add(eventFilter);
    }

    public void sendEvent(Event event) {
//		event.setEventReceiver(getId());
        processEventListener(event, sendEventListeners);
        try {
            cepCore.process(filterEvent(event, sendEventFilters));
        } catch (RuntimeException e) {
            error(e);
        }

    }

    private Event filterEvent(Event event, List<EventFilter> eventFilters) {
        Event e = event;
        for (EventFilter filter : eventFilters) {
            e = filter.filter(e);
        }
        return e;
    }

    /**
     * 通道去处理一个事件
     */
    public void process(Event event) {
        processEventListener(event, receiveEventListeners);
        try {
            receive(filterEvent(event, receiveEventFilters));
        } catch (RuntimeException e) {
            error(e);
        }
    }

    private void error(RuntimeException e) {
        LOGGER.errorMessage("通道处理错误", e);
        throw e;
    }

    protected abstract void receive(Event event);

    /**
     * 处理事件不会导致事件本身的传递，也不会影响其它事件处理，也能修改事件
     *
     * @param event
     * @param eventListeners
     */
    private void processEventListener(final Event event,
                                      List<EventListener> eventListeners) {
        if (!eventListeners.isEmpty()) {
            for (EventListener eventListener : eventListeners) {
                try {
                    eventListener.process(event);
                } catch (RuntimeException e) {
                    error(e);
                }
            }
        }
    }

    public void start() {

    }

    public void stop() {

    }
}
