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


import com.xquant.database.customesql.CustomSqlProcessor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomSqlInstallProcessor extends AbstractInstallProcessor {

    private CustomSqlProcessor customSqlProcessor;

    public void setCustomSqlProcessor(CustomSqlProcessor customSqlProcessor) {
        this.customSqlProcessor = customSqlProcessor;
    }

    @Override
    public List<String> getPreProcessSqls(boolean isFull, String language,
                                          Connection connection) throws SQLException {
        // 根据顺序加载自定义sql
        List<String> customSqls = new ArrayList<String>();
        customSqls.addAll(customSqlProcessor
                .getCustomSqls(CustomSqlProcessor.BEFORE,
                        CustomSqlProcessor.STANDARD_SQL_TYPE));
        customSqls.addAll(customSqlProcessor
                .getCustomSqls(CustomSqlProcessor.BEFORE,
                        language));
        return customSqls;
    }

    @Override
    public List<String> getPostProcessSqls(boolean isFull, String language,
                                           Connection connection) throws SQLException {
        List<String> customSqls = new ArrayList<String>();
        customSqls
                .addAll(customSqlProcessor.getCustomSqls(
                        CustomSqlProcessor.AFTER,
                        CustomSqlProcessor.STANDARD_SQL_TYPE));
        customSqls.addAll(customSqlProcessor.getCustomSqls(
                CustomSqlProcessor.AFTER, language));

        return customSqls;
    }

}
