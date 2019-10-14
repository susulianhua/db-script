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
package org.tinygroup.tinypc;

import org.tinygroup.rmi.RemoteObject;

import java.rmi.RemoteException;
import java.util.List;

/**
 * 任务分解者
 * 为了加快处理速度，需要对工作进行分解，这个时候就需要任务分解器来处理
 * <p/>
 * Created by luoguo on 14-1-8.
 */
public interface WorkSplitter extends RemoteObject {
    /**
     * 把一个任务分解为多个任务
     *
     * @param work    原始工作
     * @param workers 参与的工人
     */
    List<Warehouse> split(Work work, List<Worker> workers) throws RemoteException;

}
