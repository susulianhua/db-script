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

import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class NoMonitorTestCase extends AbstractCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(NoMonitorTestCase.class);

    private long SERVICE_A_COUNT = 20;
    private long SERVICE_EXCEPTION_COUNT = 20;
    private int COUNT = 100;
    private CountDownLatch latch = new CountDownLatch(COUNT);

    public String getConfig() {
        return "applicationnomonitor.xml";
    }

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

    }

    private void execute() {
        for (int i = 0; i < SERVICE_A_COUNT; i++) {
            try {
                getCore().process(getEvent("a"));
            } catch (Exception e) {
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
