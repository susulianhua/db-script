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
package com.xquant.database.tablespace.impl;

import com.xquant.database.ProcessorManager;
import com.xquant.database.config.tablespace.TableSpace;
import com.xquant.database.config.tablespace.TableSpaces;
import com.xquant.database.exception.DatabaseRuntimeException;
import com.xquant.database.tablespace.TableSpaceProcessor;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.xquant.database.exception.DatabaseErrorCode.TABLESPACE__ADD_ALREADY_ERROR;

/**
 * Created by wangwy11342 on 2016/8/1.
 */
@Component("tableSpaceProcessor")
public class TableSpaceProcessorImpl implements TableSpaceProcessor {

    private static TableSpaceProcessor tableSpaceProcessor = new TableSpaceProcessorImpl();
    private ProcessorManager processorManager;
    private Map<String, TableSpace> nameMap = new HashMap<String, TableSpace>();
    private Map<String, TableSpace> idMap = new HashMap<String, TableSpace>();

    public static TableSpaceProcessor getTableSpaceProcessor() {
        return tableSpaceProcessor;
    }

    public ProcessorManager getProcessorManager() {
        return processorManager;
    }

    public void setProcessorManager(ProcessorManager processorManager) {
        this.processorManager = processorManager;
    }

    public void addTableSpaces(TableSpaces tableSpaces) {
        for (TableSpace tableSpace : tableSpaces.getTableSpaces()) {
            addTableSpace(tableSpace);
        }
    }

    public void addTableSpace(TableSpace tableSpace) {
        nameMap.put(tableSpace.getName(), tableSpace);
        if (idMap.containsKey(tableSpace.getId())) {
            //重复异常
            throw new DatabaseRuntimeException(TABLESPACE__ADD_ALREADY_ERROR, tableSpace.getName(), tableSpace.getId());
        }
        idMap.put(tableSpace.getId(), tableSpace);
    }

    public TableSpace getTableSpace(String tableSpaceId) {
        if (idMap.containsKey(tableSpaceId)) {
            return idMap.get(tableSpaceId);
        }
        throw new RuntimeException(String.format("找不到ID：[%s]的表空间。", tableSpaceId));
    }

    public void removeTableSpaces(TableSpaces tableSpaces) {
        if (!MapUtils.isEmpty(nameMap)) {
            for (TableSpace tableSpace : tableSpaces.getTableSpaces()) {
                removeTableSpace(tableSpace);
            }
        }
    }

    public void removeTableSpace(TableSpace tableSpace) {
        if (!MapUtils.isEmpty(nameMap)) {
            nameMap.remove(tableSpace.getName());
        }
        idMap.remove(tableSpace.getId());
    }
}
