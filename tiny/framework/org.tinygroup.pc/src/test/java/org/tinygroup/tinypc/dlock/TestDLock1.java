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
package org.tinygroup.tinypc.dlock;

import org.tinygroup.rmi.RmiServer;
import org.tinygroup.rmi.impl.RmiServerImpl;
import org.tinygroup.threadgroup.AbstractProcessor;
import org.tinygroup.threadgroup.MultiThreadProcessor;
import org.tinygroup.tinypc.TestUtil;

import java.rmi.RemoteException;

/**
 * Created by luoguo on 14-1-13.
 */
public class TestDLock1 {
    public static void main(String[] args) throws Exception {
        RmiServer rmiServer = new RmiServerImpl(TestUtil.SIP, TestUtil.SP);
        DistributedLockImpl distributedLock = new DistributedLockImpl();
        rmiServer.registerRemoteObject(distributedLock, "lock1");
        MultiThreadProcessor processor = new MultiThreadProcessor("aa");
        for (int i = 0; i < 8; i++) {
            processor.addProcessor(new RunLock("aa" + i));
        }
        long s = System.currentTimeMillis();
        processor.start();
        long e = System.currentTimeMillis();
        System.out.println(e - s);
        rmiServer.stop();
    }
}

class RunLock extends AbstractProcessor {
    public RunLock(String name) {
        super(name);
    }


    protected void action() throws Exception {
        try {
            RmiServer client = new RmiServerImpl(TestUtil.CIP, TestUtil.CP, TestUtil.SIP, TestUtil.SP);
            DistributedLock lock = client.getObject("lock1");
            for (int i = 0; i < 1000; i++) {
                long token = lock.lock();
                lock.unlock(token);
            }
            System.out.println("end-" + Thread.currentThread().getId());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}