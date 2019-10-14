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
package org.tinygroup.tinypc.car;

import org.tinygroup.tinypc.Warehouse;
import org.tinygroup.tinypc.Work;
import org.tinygroup.tinypc.impl.AbstractWorker;

import java.rmi.RemoteException;

/**
 * Created by luoguo on 14-1-28.
 */
public abstract class StepSecondWorker extends AbstractWorker {
    /**
     *
     */
    private static final long serialVersionUID = 8513328972486061954L;

    public StepSecondWorker(String s) throws RemoteException {
        super(s);
    }

    protected boolean acceptWork(Work work, String type) {
//        String workClass = work.getInputWarehouse().get("class");
//        if (workClass != null && workClass.equals(type)) {
//            return true;
//        }
        return true;
    }

    protected Warehouse doWork(Work work, String type) throws RemoteException {
        System.out.println(String.format("Base:%s ", work.getInputWarehouse().get("baseInfo")));
        System.out.println(String.format("%s is Ok", type));
        return work.getInputWarehouse();
    }
}
