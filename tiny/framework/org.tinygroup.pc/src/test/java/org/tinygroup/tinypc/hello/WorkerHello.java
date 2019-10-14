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
package org.tinygroup.tinypc.hello;

import org.tinygroup.tinypc.Warehouse;
import org.tinygroup.tinypc.Work;
import org.tinygroup.tinypc.impl.AbstractWorker;
import org.tinygroup.tinypc.impl.WarehouseDefault;

import java.rmi.RemoteException;

/**
 * Created by luoguo on 14-1-8.
 */
public class WorkerHello extends AbstractWorker {
    private static final long serialVersionUID = 624918239419991539L;

    public WorkerHello() throws RemoteException {
        super("hello");
    }

    public Warehouse doWork(Work work) throws RemoteException {
        String name = work.getInputWarehouse().get("name");
        System.out.println(String.format("id %s: Hello %s", getId(), name));
        Warehouse outputWarehouse = new WarehouseDefault();
        outputWarehouse.put("helloInfo", "Hello," + name);
        return outputWarehouse;
    }
}
