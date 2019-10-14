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
package org.tinygroup.cepcoremonitor.test;

import org.tinygroup.cepcoremonitor.CEPCoreProcessMonitorFilter;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class MonitorTestCase extends AbstractCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorTestCase.class);

    private long SERVICE_A_COUNT = 20;
    private long SERVICE_EXCEPTION_COUNT = 20;
    private int COUNT = 100;
    private CountDownLatch latch = new CountDownLatch(COUNT);

    public void testCount() {
        List<DealThread> list = new ArrayList<DealThread>();
        for (int i = 0; i < COUNT; i++) {
            DealThread t = new DealThread();
            t.start();
            list.add(t);
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
        }
        LOGGER.errorMessage("线程数:{}", COUNT);
        ConcurrentMap<String, AtomicReference<long[]>> map = CEPCoreProcessMonitorFilter.getDataInfoMap();
        for (String service : map.keySet()) {
            long[] data = map.get(service).get();
            LOGGER.errorMessage("==================");
            LOGGER.errorMessage("服务ID:{}", service);
            LOGGER.errorMessage("执行成功次数：{}", data[0]);
            LOGGER.errorMessage("执行失败次数：{}", data[1]);
            if ("a".equals(service)) {
                assertEquals(COUNT * SERVICE_A_COUNT, data[0]);
                assertEquals(0, data[1]);
                LOGGER.errorMessage("执行时长：{}", data[2]);
                LOGGER.errorMessage("最大并发数：{}", data[3]);
                LOGGER.errorMessage("最短执行时间：{}", data[4]);
                LOGGER.errorMessage("最长执行时间：{}", data[5]);
            } else {
                assertEquals(0, data[0]);
                assertEquals(COUNT * SERVICE_EXCEPTION_COUNT, data[1]);
                LOGGER.errorMessage("执行时长：{}", data[2]);
                LOGGER.errorMessage("最大并发数：{}", data[3]);
                LOGGER.errorMessage("最短执行时间：{}", data[4]);
                LOGGER.errorMessage("最长执行时间：{}", data[5]);
            }
        }

        for (DealThread t : list) {
            LOGGER.errorMessage("==================");
            LOGGER.errorMessage("执行时间：{}", t.getTime());
        }

    }

    private void execute() {
        for (int i = 0; i < SERVICE_A_COUNT; i++) {
            try {
                getCore().process(getEvent("a"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < SERVICE_EXCEPTION_COUNT; i++) {
            try {
                getCore().process(getEvent("exception"));
            } catch (Exception e) {
            }

        }
    }

    class DealThread extends Thread {
        long time = 0;

        public void run() {
            long start = System.currentTimeMillis();
            execute();
            time = System.currentTimeMillis() - start;
            latch.countDown();
        }

        public long getTime() {
            return time;
        }

    }

}
