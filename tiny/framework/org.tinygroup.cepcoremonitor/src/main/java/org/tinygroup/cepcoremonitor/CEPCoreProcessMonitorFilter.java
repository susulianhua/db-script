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
package org.tinygroup.cepcoremonitor;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.CEPCoreProcessDealer;
import org.tinygroup.cepcore.CEPCoreProcessFilter;
import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.event.Event;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class CEPCoreProcessMonitorFilter implements CEPCoreProcessFilter {
    private static final ConcurrentMap<String, AtomicInteger> currentInfos = new ConcurrentHashMap<String, AtomicInteger>();
    private static final ConcurrentMap<String, AtomicReference<long[]>> dataInfoMap = new ConcurrentHashMap<String, AtomicReference<long[]>>();

    public static ConcurrentMap<String, AtomicReference<long[]>> getDataInfoMap() {
        return dataInfoMap;
    }

    private AtomicInteger getTimes(String serviceId, ConcurrentMap<String, AtomicInteger> map) {
        AtomicInteger times = map.get(serviceId);
        if (times == null) {
            map.putIfAbsent(serviceId, new AtomicInteger());
            times = map.get(serviceId);
        }
        return times;
    }

    public void process(CEPCoreProcessDealer dealer, Event event, CEPCore core, EventProcessor processor) {

        String serviceId = event.getServiceRequest().getServiceId();
        getTimes(serviceId, currentInfos).incrementAndGet();
        long start = System.currentTimeMillis();
        boolean success = false;
        try {
            dealer.process(event, core, processor);
            success = true;
        } finally {
            compute(event, start, success);
            getTimes(serviceId, currentInfos).decrementAndGet();
        }

    }

    private void compute(Event event, long start, boolean successTag) {
        String serviceId = event.getServiceRequest().getServiceId();
        long executeTime = System.currentTimeMillis() - start;
        AtomicReference<long[]> reference = dataInfoMap.get(serviceId);
        if (reference == null) {
            dataInfoMap.putIfAbsent(serviceId, new AtomicReference<long[]>());
            reference = dataInfoMap.get(serviceId);
        }
        long success = successTag ? 1 : 0;
        long failure = successTag ? 0 : 1;
        int concurrent = getTimes(serviceId, currentInfos).get();

        long[] current;
        long[] update = new long[6];
        do {
            current = reference.get();
            if (current == null) {
                update[0] = success; // 成功次数
                update[1] = failure; // 失败次数
                update[2] = executeTime; // 执行时长
                update[3] = concurrent; // 最大并发数
                update[4] = executeTime; // 最短执行时间
                update[5] = executeTime; // 最长执行时间
            } else {
                update[0] = current[0] + success;
                update[1] = current[1] + failure;
                update[2] = current[2] + executeTime;
                update[3] = current[3] > concurrent ? current[3] : concurrent;
                update[4] = current[4] > executeTime ? executeTime : current[4];
                update[5] = current[5] > executeTime ? current[5] : executeTime;
            }
        } while (!reference.compareAndSet(current, update));
    }


}
