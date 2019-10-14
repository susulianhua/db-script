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
package org.tinygroup.cepcoregovernance;

import org.tinygroup.cepcoregovernance.container.SpecialExecuteInfoContainer;
import org.tinygroup.event.Event;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SpecialServiceExecuteContainer {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(SpecialServiceExecuteContainer.class);
    private static SpecialExecuteInfoContainer localContainer = new SpecialExecuteInfoContainer();
    private static SpecialExecuteInfoContainer remoteContainer = new SpecialExecuteInfoContainer();
    private static ExecutorService executor = Executors.newCachedThreadPool();

    protected static void addLocalExecuteBefore(Event e) {
        executor.execute(new SynchronousEventDeal(e, EventType.LOCALBEFORE));
    }

    protected static void addLocalExecuteAfter(Event e) {
        executor.execute(new SynchronousEventDeal(e, EventType.LOCALAFTER));
    }

    protected static void addRemoteExecuteBefore(Event e) {
        executor.execute(new SynchronousEventDeal(e, EventType.REMOTEBEFORE));
    }

    protected static void addRemoteExecuteAfter(Event e) {
        executor.execute(new SynchronousEventDeal(e, EventType.REMOTEAFTER));
    }

    protected static void addExecuteException(Event e, Exception t) {
        executor.execute(new SynchronousEventDeal(e, EventType.EXCEPTION));
    }

    public Long getLocalTotalTimes() {
        return localContainer.getTotalTimes();
    }

    public Long getLocalSucessTimes() {
        return localContainer.getSucessTimes();
    }

    public Long getLocalExceptionTimes() {
        return localContainer.getExceptionTimes();
    }

    public Long getRemoteTotalTimes() {
        return remoteContainer.getTotalTimes();
    }

    public Long getRemoteSucessTimes() {
        return remoteContainer.getSucessTimes();
    }

    public Long getRemoteExceptionTimes() {
        return remoteContainer.getExceptionTimes();
    }

    enum EventType {
        LOCALBEFORE, LOCALAFTER, REMOTEBEFORE, REMOTEAFTER, EXCEPTION
    }

    static class SynchronousEventDeal implements Runnable {
        Event e;
        EventType caseValue;
        Exception t;

        public SynchronousEventDeal(Event event, EventType caseValue) {
            this.e = event;
            this.caseValue = caseValue;
        }

        public SynchronousEventDeal(Event event, Exception t,
                                    EventType caseValue) {
            this.e = event;
            this.caseValue = caseValue;
            this.t = t;
        }

        public void run() {
            switch (caseValue) {
                case LOCALBEFORE:
                    localContainer.addExecuteBefore(e);
                    break;
                case LOCALAFTER:
                    localContainer.addExecuteAfter(e);
                    break;
                case REMOTEBEFORE:
                    remoteContainer.addExecuteBefore(e);
                    break;
                case REMOTEAFTER:
                    remoteContainer.addExecuteAfter(e);
                    break;
                case EXCEPTION:
                    if (localContainer.contain(e.getEventId())) {
                        localContainer.addExecuteException(e, t);
                    } else if (remoteContainer.contain(e.getEventId())) {
                        remoteContainer.addExecuteException(e, t);
                    } else {
                        LOGGER.errorMessage("事件:" + e.getEventId() + ",服务:"
                                + e.getServiceRequest().getServiceId()
                                + ",在本地/远程列表中不存在");
                    }
                    break;
            }
        }
    }
}
