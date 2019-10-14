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
package org.tinygroup.database.procedure.impl;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.database.config.SqlBody;
import org.tinygroup.database.config.procedure.Procedure;
import org.tinygroup.database.procedure.ProcedureSqlProcessor;
import org.tinygroup.database.util.DataBaseUtil;

public abstract class ProcedureSqlProcessorImpl implements
        ProcedureSqlProcessor {
    protected abstract String getDatabaseType();

    public String getCreateSql(Procedure procedure) {
        StringBuffer sb = new StringBuffer();
        String defaultContent = "";
        String dialectContent = null;
        for (SqlBody sql : procedure.getProcedureBodyList()) {
            if ("default".equals(sql.getDialectTypeName())) {
                defaultContent = sql.getContent();
            } else if (getDatabaseType().equals(sql.getDialectTypeName())) {
                dialectContent = sql.getContent();
                break;
            }
        }
        String finalSql;
        if (dialectContent != null) {
            finalSql = dialectContent;
        } else {
            finalSql = defaultContent;
        }

        if (!StringUtil.isEmpty(finalSql)) {
            sb.append(finalSql);
        }

        String from = DataBaseUtil.fromSourceLocal.get();
        //从工具
        if (sb.length() > 0 && from != null && from.equals("tool")) {
            sb.append("\n/\n");
        }
        return sb.toString();
    }

    private void appendHead(StringBuffer sb, Procedure procedure) {
        sb.append("CREATE OR REPLACE PROCEDURE ");
        sb.append(procedure.getName());
    }


    public String getDropSql(Procedure procedure) {
        return "DROP PROCEDURE " + procedure.getName() + ";";
    }

}
