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
package com.xquant.databasebuilstaller;

import org.springframework.core.Ordered;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 功能说明:数据库安装处理接口
 * <p>
 * 开发人员: renhui <br>
 * 开发时间: 2013-8-15 <br>
 * <br>
 */
public interface InstallProcessor extends Ordered {
    String TABLE_INSTALL_PROCESSOR = "tableInstallProcessor";
    String INITDATA_INSTALL_PROCESSOR = "initDataInstallProcessor";
    String PROCEDURE_INSTALL_PROCESSOR = "procedureInstallProcessor";
    String VIEW_INSTALL_PROCESSOR = "viewInstallProcessor";
    String DATABASE_INSTALL_PROCESSOR = "databaseInstaller";
    String TRIGGER_INSTALL_PROCESSOR = "triggerInstallProcessor";
    String SEQUENCE_INSTALL_PROCESSOR = "sequenceInstallProcessor";


    /**
     * 获取前置处理sql
     *
     * @param isFull     是否增量
     * @param language   数据库语言
     * @param connection 连接对象
     * @return
     */
    List<String> getPreProcessSqls(boolean isFull, String language, Connection connection) throws SQLException;

    /**
     * 获取处理sql
     *
     * @param isFull
     * @param language
     * @param connection 连接对象
     * @return
     */
    List<String> getProcessSqls(boolean isFull, String language, Connection connection) throws SQLException;

    /**
     * 获取后置sql
     *
     * @param isFull
     * @param language
     * @param connection 连接对象
     * @return
     */
    List<String> getPostProcessSqls(boolean isFull, String language, Connection connection) throws SQLException;

}
