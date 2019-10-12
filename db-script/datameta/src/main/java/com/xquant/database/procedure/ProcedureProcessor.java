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
package com.xquant.database.procedure;

import com.xquant.database.ProcessorManager;
import com.xquant.database.config.procedure.Procedure;
import com.xquant.database.config.procedure.Procedures;

import java.util.List;

public interface ProcedureProcessor {

    Procedure getProcedure(String procedureName);

    String getCreateSql(String procedureName, String language);

    List<String> getCreateSql(String language);

    String getDropSql(String procedureName, String language);

    List<String> getDropSql(String language);

    void addProcedures(Procedures procedures);

    void removeProcedures(Procedures procedures);

    ProcessorManager getProcessorManager();

    void setProcessorManager(ProcessorManager processorManager);
}
