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

import org.tinygroup.tinypc.Warehouse;
import org.tinygroup.tinypc.impl.WorkDefault;

import java.rmi.RemoteException;

public class PlusWork extends WorkDefault {
    //加法参数在上下文中的key
    public static final String PARAM = "param";
    //加法结果在上下文中的key
    public static final String RESULT = "result";
    //任务类型
    public static final String TYPE = "plus";

    public PlusWork(String type, String id, Warehouse inputWarehouse)
            throws RemoteException {
        super(type, id, inputWarehouse);
    }

}
