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

import org.tinygroup.tinypc.JobCenter;
import org.tinygroup.tinypc.TestUtil;
import org.tinygroup.tinypc.Warehouse;
import org.tinygroup.tinypc.Work;
import org.tinygroup.tinypc.impl.JobCenterRemote;
import org.tinygroup.tinypc.impl.WarehouseDefault;

import java.io.IOException;
import java.rmi.RemoteException;

public class PlusWorkJobCenterClient {
    public static void main(String[] args) {
        try {
            JobCenter jobCenter = new JobCenterRemote(TestUtil.CIP,
                    TestUtil.CP, TestUtil.SIP, TestUtil.SP);
            Work work = createWork();
            Warehouse resultWarehouse = jobCenter.doWork(work);
            Integer result = resultWarehouse.get(PlusWork.RESULT);
            System.out.println("result is " + result);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static Work createWork() throws RemoteException {
        Warehouse inputWarehouse = new WarehouseDefault();
        int[] plusParams = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        inputWarehouse.put(PlusWork.PARAM, plusParams);
        Work work = new PlusWork(PlusWork.TYPE, "work1", inputWarehouse);
        return work;
    }
}
