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
package org.tinygroup.databasebuinstaller.impl;

import org.tinygroup.database.procedure.ProcedureProcessor;
import org.tinygroup.logger.LogLevel;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能说明:存储过程安装处理器
 * <p>
 * 开发人员: renhui <br>
 * 开发时间: 2013-8-15 <br>
 * <br>
 */
public class ProcedureInstallProcessor extends AbstractInstallProcessor {

    private ProcedureProcessor procedureProcessor;

    public ProcedureProcessor getProcedureProcessor() {
        return procedureProcessor;
    }

    public void setProcedureProcessor(ProcedureProcessor procedureProcessor) {
        this.procedureProcessor = procedureProcessor;
    }

    public int getOrder() {
        return 15;
    }

    @Override
    public List<String> getProcessSqls(boolean isFull, String language,
                                       Connection connection) throws SQLException {
        logger.logMessage(LogLevel.INFO, "开始获取数据库存储过程安装操作执行语句");

        List<String> sqls = new ArrayList<String>();
        sqls.addAll(procedureProcessor.getCreateSql(language));
        if (sqls.size() != 0) {
            logger.logMessage(LogLevel.INFO, "生成sql:{0}", sqls);
        } else {
            logger.logMessage(LogLevel.INFO, "无需生成Sql");
        }
        logger.logMessage(LogLevel.INFO, "获取数据库存储过程安装操作执行语句结束");
        return sqls;
    }

}
