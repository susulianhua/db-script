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
package com.xquant.database.customesql.impl;

import com.xquant.database.config.SqlBody;
import com.xquant.database.config.customsql.CustomSql;
import com.xquant.database.config.customsql.CustomSqls;
import com.xquant.database.customesql.CustomSqlProcessor;
import com.xquant.database.util.DataBaseUtil;
import com.xquant.metadata.checkupdate.MetaDataFileInfo;
import com.xquant.metadata.checkupdate.MetaDataFileManager;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomSqlProcessorImpl implements CustomSqlProcessor {
    private static CustomSqlProcessor defaultCustomSqlProcessor = new CustomSqlProcessorImpl();
    private MetaDataFileManager metaDataFileManager = MetaDataFileManager.getInstance();
    private Map<String, Long> customSqlModifiedTimeMap = new HashMap<String, Long>();

    private CustomSqls customSqls;

    public static CustomSqlProcessor getCustomSqlProcessor() {
        return defaultCustomSqlProcessor;
    }

    public CustomSqls getCustomSqls() {
        return customSqls;
    }

    public void setCustomSqls(CustomSqls customSqls) {
        this.customSqls = customSqls;
    }

    public List<String> getCustomSqls(String type, String language) {
        Map<String, Map<String, List<String>>> sqlsMap = new HashMap<String, Map<String, List<String>>>();
        createSqlsByCustomSql(sqlsMap);
        List<String> customSqls = null;
        if (sqlsMap.containsKey(language)) {
            customSqls = sqlsMap.get(language).get(type);
        }
        if (customSqls == null) {
            customSqls = new ArrayList<String>();
        }
        return customSqls;
    }

    /**
     * 创建
     */
    private void createSqlsByCustomSql(Map<String, Map<String, List<String>>> sqlsMap) {
        if (customSqls == null || customSqls.getCustomSqlList() == null) {
            return;
        }
        for (CustomSql sql : customSqls.getCustomSqlList()) {
            if (DataBaseUtil.isNeedCache()) {
                MetaDataFileInfo metaDataFileInfo = new MetaDataFileInfo();
                metaDataFileInfo.setType("CUSTOM_SQL");
                metaDataFileInfo.setResourceId(sql.getId());
                String timeStr = String.valueOf(getLastModifiedTime(sql.getId()));
                metaDataFileInfo.setModifiedTime(timeStr);
                //元数据信息时间表中未变更则跳过
                if (!metaDataFileManager.createSqlAndCheckUpdate(metaDataFileInfo)) {
                    continue;
                }
            }
            String type = sql.getType();
            for (SqlBody body : sql.getSqlBodyList()) {
                // 未填写数据库类型的默认为标准sql
                String language = null == body.getDialectTypeName() ? STANDARD_SQL_TYPE
                        : body.getDialectTypeName();
                String sqlStr = body.getContent();
                if (!StringUtils.isEmpty(sqlStr)) {
                    addSql(language, sqlStr, type, sqlsMap);
                }
            }
        }
    }


    public void addCustomSqls(CustomSqls customsqls) {
        this.customSqls = customsqls;
    }

    private void addSql(String language, String sql, String type, Map<String, Map<String, List<String>>> sqlsMap) {
        if (!sqlsMap.containsKey(language)) {
            Map<String, List<String>> map = new HashMap<String, List<String>>();
            sqlsMap.put(language, map);
        }
        Map<String, List<String>> map = sqlsMap.get(language);
        if (!map.containsKey(type)) {
            List<String> sqlList = new ArrayList<String>();
            map.put(type, sqlList);
        }
        List<String> sqlList = map.get(type);
        sqlList.add(sql);
    }

    public void removeCustomSqls(CustomSqls customsqls) {
        if (customsqls == null) {
            return;
        }
        if (customsqls.getCustomSqlList() == null)
            return;
        /*for (CustomSql sql : customsqls.getCustomSqlList()) {
            String type = sql.getType();
            for (SqlBody body : sql.getSqlBodyList()) {
                String language = body.getDialectTypeName();
                String sqlStr = body.getContent();
                removeSql(language, sqlStr, type);
            }
        }*/
        this.customSqls = null;
    }

    public void registerModifiedTime(CustomSqls customSqls, long lastModify) {
        for (CustomSql customSql : customSqls.getCustomSqlList()) {
            customSqlModifiedTimeMap.put(customSql.getId(), lastModify);
        }
    }

    public long getLastModifiedTime(String customSqlId) {
        return customSqlModifiedTimeMap.get(customSqlId);
    }

    private void removeSql(String language, String sql, String type) {
        /*Map<String, List<String>> map = sqlsMap.get(language);
        if (!CollectionUtil.isEmpty(map)) {
            List<String> sqlList = map.get(type);
            if (!CollectionUtil.isEmpty(sqlList)) {
                sqlList.remove(sql);
            }
        }*/
    }
}
