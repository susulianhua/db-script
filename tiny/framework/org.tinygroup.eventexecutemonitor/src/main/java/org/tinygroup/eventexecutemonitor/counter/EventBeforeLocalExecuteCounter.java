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
package org.tinygroup.eventexecutemonitor.counter;

import org.tinygroup.cepcore.aop.CEPCoreAopAdapter;
import org.tinygroup.event.Event;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 *
 * @author zhangliang08072
 * @version $Id: EventBeforeLocalExecuteCounter.java, v 0.1 2016年12月21日 上午1:38:29 zhangliang08072 Exp $
 */
public class EventBeforeLocalExecuteCounter extends EventExecuteCounter implements CEPCoreAopAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventBeforeLocalExecuteCounter.class);

    public void handle(Event event) {
        EventExecuteMonitor.addLocalExecuteBeforeSyn(event);
        if (!getExecutorService().isShutdown()) {
            AsynchronousDeal thread = new AsynchronousDeal(event);
            getExecutorService().execute(thread);
        } else {
            LOGGER.logMessage(LogLevel.ERROR, "请求的异步统计线程池已关闭，未能统计。eventId:{}", event.getEventId());
        }
    }

    class AsynchronousDeal implements Runnable {
        Event e;

        AsynchronousDeal(Event e) {
            this.e = e;
        }

        public void run() {
            EventExecuteMonitor.addLocalExecuteBeforeAsyn(e);
        }
    }
}
