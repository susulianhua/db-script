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

import com.xquant.database.config.sequence.Sequence;
import com.xquant.database.sequence.SequenceProcessor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SequenceInstallProcessor extends AbstractInstallProcessor {

    private SequenceProcessor processor;

    public SequenceProcessor getProcessor() {
        return processor;
    }

    public void setProcessor(SequenceProcessor processor) {
        this.processor = processor;
    }

    @Override
    public List<String> getProcessSqls(boolean isFull, String language,
                                       Connection connection) throws SQLException {
        logger.info("开始获取数据库序列安装操作执行语句");

        List<String> sqls = new ArrayList<String>();
        List<Sequence> sequences = processor.getSequences(language);
        for (Sequence sequence : sequences) {
            logger.info( "开始生成序列语句,序列名:{0}",
                    sequence.getName());
            // 全量或者不存在
            if (isFull
                    || !processor.checkSequenceExist(language, sequence,
                    connection)) {
                sqls.add(processor.getCreateSql(sequence.getName(), language));
            }
        }
        if (sqls.size() != 0) {
            logger.info( "生成sql:{0}", sqls);
        } else {
            logger.info( "无需生成Sql");
        }
        logger.info( "获取数据库序列安装操作执行语句结束");

        return sqls;
    }

}
