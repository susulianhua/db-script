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
package org.tinygroup.tinypc.impl;

import org.tinygroup.rmi.RmiServer;
import org.tinygroup.rmi.impl.RmiServerImpl;
import org.tinygroup.tinypc.WorkQueue;

import java.io.IOException;

/**
 * Created by luoguo on 14-1-23.
 */
public class JobCenterLocal extends AbstractJobCenter {
    public JobCenterLocal() throws IOException {
        this(DEFAULT_PORT);
    }

    public JobCenterLocal(int port) throws IOException {
        RmiServer rmiServer = new RmiServerImpl(port);
        WorkQueue workQueue = new WorkQueueImpl();
        setWorkQueue(workQueue);
        rmiServer.registerLocalObject(workQueue, "WorkQueue");
        setRmiServer(rmiServer);
    }

    public JobCenterLocal(String host, int port) throws IOException {
        RmiServer rmiServer = new RmiServerImpl(host, port);
        WorkQueue workQueue = new WorkQueueImpl();
        setWorkQueue(workQueue);
        rmiServer.registerLocalObject(workQueue, "WorkQueue");
        setRmiServer(rmiServer);
    }
}
