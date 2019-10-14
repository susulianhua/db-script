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
package org.tinygroup.tinypc.helloforeach.rmi;

import org.tinygroup.tinypc.JobCenter;
import org.tinygroup.tinypc.TestUtil;
import org.tinygroup.tinypc.Warehouse;
import org.tinygroup.tinypc.Work;
import org.tinygroup.tinypc.hellosingle.WorkerHello;
import org.tinygroup.tinypc.impl.JobCenterRemote;
import org.tinygroup.tinypc.impl.WarehouseDefault;
import org.tinygroup.tinypc.impl.WorkDefault;

import java.util.List;

public class TestWorkClient {
    public static void main(String[] args) {

        TestWorkClient c = new TestWorkClient();
        c.run();

    }

    public void run() {
        MyThread t = new MyThread();
        t.run();
    }

    private void work() {
        try {
            JobCenter jobCenter = new JobCenterRemote(TestUtil.CIP, TestUtil.CP, TestUtil.SIP, TestUtil.SP);
            jobCenter.registerWorker(new WorkerHello());
            Warehouse inputWarehouse = new WarehouseDefault();
            inputWarehouse.put("name", "world");
            Work work = new WorkDefault("hello", inputWarehouse);
            Warehouse outputWarehouse = jobCenter.doWork(work);
            List<String> result = outputWarehouse.get("helloInfo");
            System.out.println(result.size());
            jobCenter.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class MyThread extends Thread {
        boolean end = false;

        public void run() {
            while (!end) {
                work();
                try {
                    sleep(5000);
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }

        }
    }
}
