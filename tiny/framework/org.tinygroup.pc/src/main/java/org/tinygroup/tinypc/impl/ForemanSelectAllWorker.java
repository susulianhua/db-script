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

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.threadgroup.MultiThreadProcessor;
import org.tinygroup.tinypc.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * 包工头 如果有任务分解器，则给每个工人一个子任务执行 如果没有任务解器，则让每个工人执行一次 Created by luoguo on 14-1-8.
 */
public class ForemanSelectAllWorker extends AbstractForeman {

    /**
     *
     */
    private static final long serialVersionUID = -8936491160583306823L;
    private static final transient Logger LOGGER = LoggerFactory
            .getLogger(ForemanSelectAllWorker.class);

    public ForemanSelectAllWorker(String type) throws RemoteException {
        super(type);
    }

    public ForemanSelectAllWorker(String type, WorkCombiner workCombiner)
            throws RemoteException {
        this(type);
        setWorkCombiner(workCombiner);
    }

    public ForemanSelectAllWorker(String type, WorkSplitter workSplitter)
            throws RemoteException {
        this(type);
        setWorkSplitter(workSplitter);
    }

    public ForemanSelectAllWorker(String type, WorkCombiner workCombiner,
                                  WorkSplitter workSplitter) throws RemoteException {
        this(type);
        setWorkSplitter(workSplitter);
        setWorkCombiner(workCombiner);
    }

    public ForemanSelectAllWorker(String type,
                                  WorkSplitterCombiner workSplitterCombiner) throws RemoteException {
        this(type);
        setWorkSplitter(workSplitterCombiner);
        setWorkCombiner(workSplitterCombiner);
    }

    public Warehouse work(Work work, List<Worker> workerList)
            throws RemoteException {
        MultiThreadProcessor processors = new MultiThreadProcessor(
                String.format("id:%s," + "type:%s", work.getId(),
                        work.getType()));
        List<Warehouse> warehouseList = new ArrayList<Warehouse>();

        if (getWorkSplitter() != null) {
            List<Warehouse> splitWarehouseList = getWorkSplitter().split(work,
                    workerList);
            LOGGER.logMessage(LogLevel.INFO, "任务[type:{0},id:{1}]被分割为{2}份",
                    work.getType(), work.getId(), splitWarehouseList.size());
            int j = 0;
            for (int i = 0; i < splitWarehouseList.size(); i++) {
                Work subWork = new WorkDefault(work.getType());
                subWork.setInputWarehouse(splitWarehouseList.get(i));
                if (j >= workerList.size()) {
                    j = 0;
                }
                Worker worker = workerList.get(j);
                processors.addProcessor(new WorkExecutor(subWork, worker,
                        warehouseList, workerList));
                j++;
            }
        } else {
            for (int i = 0; i < workerList.size(); i++) {
                Work subWork = new WorkDefault(work.getType());
                subWork.setInputWarehouse(work.getInputWarehouse());
                Worker worker = workerList.get(i);
                processors.addProcessor(new WorkExecutor(subWork, worker,
                        warehouseList, workerList));
            }
        }

        processors.start();
        Warehouse outputWarehouse = work.getInputWarehouse();
        if (getWorkCombiner() != null) {
            outputWarehouse = getWorkCombiner().combine(warehouseList);
        }
        return outputWarehouse;
    }

}
