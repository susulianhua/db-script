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
import org.tinygroup.threadgroup.AbstractProcessor;
import org.tinygroup.tinypc.PCRuntimeException;
import org.tinygroup.tinypc.Warehouse;
import org.tinygroup.tinypc.Work;
import org.tinygroup.tinypc.Worker;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luoguo on 14-1-8.
 */
public class WorkExecutor extends AbstractProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkExecutor.class);
    private final Worker worker;
    private final Work work;
    private final List<Warehouse> warehouseList;
    private final List<Worker> workers;


    public WorkExecutor(Work work, Worker worker, List<Warehouse> warehouseList, List<Worker> workers)
            throws RemoteException {
        super(worker.getId());
        this.worker = worker;
        this.work = work;
        this.warehouseList = warehouseList;
        this.workers = cloneWorkers(workers);
    }

    private List<Worker> cloneWorkers(List<Worker> acceptWorkers) {
        List<Worker> workerList = new ArrayList<Worker>();
        for (Worker woker : acceptWorkers) {
            workerList.add(woker);
        }
        return workerList;
    }


    protected void action() throws RemoteException {
        List<Worker> workersActived = new ArrayList<Worker>();
        workersActived.add(worker);
        workers.remove(worker);
        doWork(worker, workersActived);
    }

    private void doWork(Worker w, List<Worker> workersActived) throws RemoteException {
        Warehouse warehouse = null;
        try {
            LOGGER.logMessage(LogLevel.DEBUG, "worker:{0}开始执行", w.getId());
            warehouse = w.work(work);
            LOGGER.logMessage(LogLevel.DEBUG, "worker:{0}执行完成", w.getId());
        } catch (Exception e) {
            LOGGER.errorMessage("worker:{0}工作时发生异常", e, w.getId());
            redoWork(workersActived);
        }
        synchronized (warehouseList) {
            warehouseList.add(warehouse);
        }
    }

    private void redoWork(List<Worker> workersActived) throws RemoteException {
        LOGGER.logMessage(LogLevel.DEBUG, "开始重新查找worker");
        Worker redoWorker = null;
        for (Worker worker : workers) {
            if (worker.acceptWork(work)) {
                LOGGER.logMessage(LogLevel.DEBUG, "查找到worker:{0}", worker.getId());
                redoWorker = worker;
                workersActived.add(worker);
            }
        }
        if (redoWorker == null) {
            throw new PCRuntimeException(String.format("没有对应于work:%s的工人", work.getType()));
        }
        workers.remove(redoWorker);
        doWork(redoWorker, workersActived);
    }
}