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

import org.tinygroup.tinypc.Warehouse;
import org.tinygroup.tinypc.Work;
import org.tinygroup.tinypc.WorkStatus;

import java.rmi.RemoteException;

/**
 * Created by luoguo on 14-1-8.
 */
public class WorkDefault implements Work {
    /**
     *
     */
    private static final long serialVersionUID = -7558871247472425574L;
    private String id;
    private String type;
    private Warehouse inputWarehouse;
    private boolean needSerialize = false;
    private Work nextStepWork = null;
    private WorkStatus workStatus = WorkStatus.WAITING;
    private String foremanType;


    private WorkDefault() throws RemoteException {
        this.id = Util.getUuid();
    }

    public WorkDefault(String type) throws RemoteException {
        this();
        this.type = type;
    }

    public WorkDefault(String type, String id) throws RemoteException {
        this.type = type;
        this.id = id;
    }

    public WorkDefault(String type, Warehouse inputWarehouse) throws RemoteException {
        this(type);
        this.inputWarehouse = inputWarehouse;
    }

    public WorkDefault(String type, String id, Warehouse inputWarehouse) throws RemoteException {
        this(type, id);
        this.inputWarehouse = inputWarehouse;
    }


    public String getType() {
        return type;
    }

    public synchronized Work getNextWork() {
        return nextStepWork;
    }

    public synchronized Work setNextWork(Work nextStepWork) {
        this.nextStepWork = nextStepWork;
        return nextStepWork;
    }


    public boolean isNeedSerialize() {
        return needSerialize;
    }


    public synchronized void setNeedSerialize(boolean needSerialize) {
        this.needSerialize = needSerialize;
    }

    public synchronized WorkStatus getWorkStatus() {
        return workStatus;
    }

    public synchronized void setWorkStatus(WorkStatus workStatus) {
        this.workStatus = workStatus;
    }

    public synchronized Warehouse getInputWarehouse() {
        return inputWarehouse;
    }

    public synchronized void setInputWarehouse(Warehouse inputWarehouse) {
        this.inputWarehouse = inputWarehouse;
    }

    public synchronized String getId() {
        return id;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Work) {
            Work object = (Work) obj;
            return ((Work) obj).getId().equals(object.getId());
        }
        return false;
    }

    public int hashCode() {
        return getId().hashCode();
    }

    public String getForemanType() {
        if (foremanType != null)
            return foremanType;
        return getType();
    }

    public void setForemanType(String foremanType) {
        this.foremanType = foremanType;
    }
}
