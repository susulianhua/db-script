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
package com.xquant.databasebuilstaller.impl;

import com.xquant.database.initdata.InitDataProcessor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

/**
 * 功能说明:数据库初始化安装处理器
 * <p>
 * 开发人员: renhui <br>
 * 开发时间: 2013-8-15 <br>
 * <br>
 */
public class InitDataInstallProcessor extends AbstractInstallProcessor {
    private static final String INIT_DATA_KEY = "INIT_DATA";

    private InitDataProcessor initDataProcessor;


    public InitDataProcessor getInitDataProcessor() {
        return initDataProcessor;
    }


    public void setInitDataProcessor(InitDataProcessor initDataProcessor) {
        this.initDataProcessor = initDataProcessor;
    }

    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }


    @Override
    public List<String> getProcessSqls(boolean isFull, String language,
                                       Connection connection) throws SQLException {
        logger.info( "开始获取数据库初始化数据安装操作执行语句");
        List<String> sqls = new ArrayList<String>();
        sqls.addAll(initDataProcessor.getInitSql(language, connection, isFull));

        if (sqls.size() != 0) {
            logger.info( "生成sql:{0}", sqls);
        } else {
            logger.info( "无需生成Sql");
        }
        logger.info( "获取数据库初始化数据安装操作执行语句结束");
        return sqls;
    }


}
