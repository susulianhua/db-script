package org.tinygroup.servicewrapper;

import org.springframework.core.NamedThreadLocal;
import org.tinygroup.event.Event;

public class EventModeSetter {

    private static final ThreadLocal<Integer> EVENT_MODE = new NamedThreadLocal<Integer>(
            "event call mode");

    /**
     * 如果没设置mode，则使用默认的同步模式
     *
     * @return
     */
    public static int getEventMode() {
        Integer mode = EVENT_MODE.get();
        if (mode == null) {
            mode = Event.EVENT_MODE_SYNCHRONOUS;
        }
        return mode;
    }

    /**
     * 如果参数值不是异步模式，则认为是默认的同步模式。
     *
     * @param eventMode
     */
    public static void setEventMode(int eventMode) {
        if (eventMode != Event.EVENT_MODE_ASYNCHRONOUS) {
            EVENT_MODE.set(Event.EVENT_MODE_SYNCHRONOUS);
        } else {
            EVENT_MODE.set(Event.EVENT_MODE_ASYNCHRONOUS);
        }
    }

    public static void removeEventMode() {
        EVENT_MODE.remove();
    }

}
