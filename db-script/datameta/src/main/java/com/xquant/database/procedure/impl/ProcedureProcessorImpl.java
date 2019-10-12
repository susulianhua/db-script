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
package com.xquant.database.procedure.impl;

import com.xquant.database.ProcessorManager;
import com.xquant.database.config.procedure.Procedure;
import com.xquant.database.config.procedure.Procedures;
import com.xquant.database.exception.DatabaseRuntimeException;
import com.xquant.database.procedure.ProcedureProcessor;
import com.xquant.database.procedure.ProcedureSqlProcessor;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xquant.database.exception.DatabaseErrorCode.PROCEDURE__ADD_ALREADY_ERROR;

public class ProcedureProcessorImpl implements ProcedureProcessor {
    private static ProcedureProcessor procedureProcessor = new ProcedureProcessorImpl();
    Map<String, Procedure> idMap = new HashMap<String, Procedure>();

    ProcessorManager processorManager;

    public static ProcedureProcessor getProcedureProcessor() {
        return procedureProcessor;
    }


    public ProcessorManager getProcessorManager() {
        return processorManager;
    }

    public void setProcessorManager(ProcessorManager processorManager) {
        this.processorManager = processorManager;
    }

    public Procedure getProcedure(String procedureId) {
        return idMap.get(procedureId);
    }

    public String getCreateSql(String procedureId, String language) {
        Procedure procedure = getProcedure(procedureId);
        if (procedure == null) {
            throw new RuntimeException(String.format("过程[name:%s]不存在,", procedureId));
        }
        return getCreateSql(procedure, language);
    }

    public List<String> getCreateSql(String language) {
        List<String> list = new ArrayList<String>();
        for (Procedure procedure : idMap.values()) {
            String createSql = getCreateSql(procedure, language);
            if (StringUtils.isEmpty(createSql)) {
                continue;
            }
            list.add(getCreateSql(procedure, language));
        }
        return list;
    }

    private String getCreateSql(Procedure procedure, String language) {
//		ProcessorManager processorManager = SpringBeanContainer.getBean(DataBaseUtil.PROCESSORMANAGER_BEAN);
        ProcedureSqlProcessor sqlProcessor = (ProcedureSqlProcessor) processorManager.getProcessor(language, "procedure");
        return sqlProcessor.getCreateSql(procedure);
    }

    public void addProcedures(Procedures procedures) {
        for (Procedure procedure : procedures.getProcedureList()) {
            if (idMap.containsKey(procedure.getName())) {
                throw new DatabaseRuntimeException(PROCEDURE__ADD_ALREADY_ERROR, procedure.getName(), procedure.getId());
            }
            idMap.put(procedure.getId(), procedure);
        }
    }

    public void removeProcedures(Procedures procedures) {
        for (Procedure procedure : procedures.getProcedureList()) {
            idMap.remove(procedure.getId());
        }
    }

    public String getDropSql(String procedureId, String language) {
        Procedure procedure = getProcedure(procedureId);
        if (procedure == null) {
            throw new RuntimeException(String.format("过程[id:%s]不存在,", procedureId));
        }
        return getDropSql(procedure, language);
    }

    public List<String> getDropSql(String language) {
        List<String> list = new ArrayList<String>();
        for (Procedure procedure : idMap.values()) {
            list.add(getDropSql(procedure, language));
        }
        return list;
    }

    private String getDropSql(Procedure procedure, String language) {
//		ProcessorManager processorManager = SpringBeanContainer.getBean(DataBaseUtil.PROCESSORMANAGER_BEAN);
        ProcedureSqlProcessor sqlProcessor = (ProcedureSqlProcessor) processorManager.getProcessor(language, "procedure");
        return sqlProcessor.getDropSql(procedure);
    }
}
