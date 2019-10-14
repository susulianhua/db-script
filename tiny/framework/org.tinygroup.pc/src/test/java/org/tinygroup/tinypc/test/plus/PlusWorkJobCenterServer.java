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
package org.tinygroup.tinypc.test.plus;

import org.tinygroup.tinypc.Foreman;
import org.tinygroup.tinypc.JobCenter;
import org.tinygroup.tinypc.TestUtil;
import org.tinygroup.tinypc.Worker;
import org.tinygroup.tinypc.impl.ForemanSelectOneWorker;
import org.tinygroup.tinypc.impl.JobCenterLocal;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class PlusWorkJobCenterServer {
    public static void main(String[] args) {
        try {
            JobCenter jobCenter = new JobCenterLocal(TestUtil.SIP, TestUtil.SP);
            jobCenter.getRmiServer();
            regForman(jobCenter);
            regWoker(jobCenter, 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Worker> regWoker(JobCenter jobCenter, int length)
            throws RemoteException {
        List<Worker> list = new ArrayList<Worker>();
        for (int i = 0; i < length; i++) {
            Worker worker = new PlusWorker();
            jobCenter.registerWorker(worker);
            list.add(worker);
        }
        return list;
    }

    private static Foreman regForman(JobCenter jobCenter)
            throws RemoteException {
        Foreman f = new ForemanSelectOneWorker(PlusWork.TYPE);
        jobCenter.registerForeman(f);
        return f;
    }

    public void runThread() {
        MyThread t = new PlusWorkJobCenterServer.MyThread();
        t.run();
    }

    class MyThread extends Thread {
        private boolean end = false;

        public void run() {
            while (!end) {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
