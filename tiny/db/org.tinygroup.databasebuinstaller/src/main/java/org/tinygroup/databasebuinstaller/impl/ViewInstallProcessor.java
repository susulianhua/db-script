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

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.database.config.view.View;
import org.tinygroup.database.util.DataBaseUtil;
import org.tinygroup.database.view.ViewProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.metadata.checkupdate.MetaDataFileInfo;
import org.tinygroup.metadata.checkupdate.MetaDataFileManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能说明: 数据库视图安装处理器
 * <p>
 * 开发人员: renhui <br>
 * 开发时间: 2013-8-15 <br>
 * <br>
 */
public class ViewInstallProcessor extends AbstractInstallProcessor {

    private ViewProcessor viewProcessor;

    private MetaDataFileManager metaDataFileManager = MetaDataFileManager.getInstance();

    public ViewProcessor getViewProcessor() {
        return viewProcessor;
    }

    public void setViewProcessor(ViewProcessor viewProcessor) {
        this.viewProcessor = viewProcessor;
    }

    public int getOrder() {
        return 10;
    }

    @Override
    public List<String> getProcessSqls(boolean isFull, String language,
                                       Connection connection) throws SQLException {
        logger.logMessage(LogLevel.INFO, "开始获取数据库视图安装操作执行语句");

        List<String> createViewSqls = new ArrayList<String>();
        List<View> views = viewProcessor.getViews();

        for (View view : views) {
            if (DataBaseUtil.isNeedCache()) {
                MetaDataFileInfo metaDataFileInfo = new MetaDataFileInfo();
                metaDataFileInfo.setType("VIEW");
                metaDataFileInfo.setResourceId(view.getId());
                String timeStr = String.valueOf(viewProcessor
                        .getLastModifiedTime(view.getId()));
                metaDataFileInfo.setModifiedTime(timeStr);
                //元数据信息时间表不发生变更,则跳过
                if (!metaDataFileManager.createSqlAndCheckUpdate(metaDataFileInfo)) {
                    continue;
                }
            }
            String viewSql;
            if (isFull) {
                viewSql = viewProcessor.getCreateSql(view, language);
            } else {
                viewSql = viewProcessor.getUpdateSql(createViewSqls, view, connection, language);
            }

            if (!StringUtil.isBlank(viewSql)) {
                createViewSqls.add(viewSql);
            }
        }
        if (createViewSqls.size() != 0) {
            logger.logMessage(LogLevel.INFO, "生成sql:{0}", createViewSqls);
        } else {
            logger.logMessage(LogLevel.INFO, "无需生成Sql");
        }
        logger.logMessage(LogLevel.INFO, "获取数据库视图安装操作执行语句结束");

        return createViewSqls;
    }

}
