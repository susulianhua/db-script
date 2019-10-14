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
package org.tinygroup.databasechange;

import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.database.util.DataBaseUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.io.*;
import java.util.List;

/**
 * 表格结果变更信息记录的工具类
 *
 * @author renhui
 */
public class TableSqlFullUtil {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DatabaseInstallerStart.class);

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.exit(0);
        }
        String fileName = args[0];
        long start = System.currentTimeMillis();
        LOGGER.logMessage(LogLevel.INFO, "开始生成数据库全量sql");
        try {
            DataBaseUtil.fromSourceLocal.set("tool");
            DatabaseInstallerStart installerStart = new DatabaseInstallerStart();
            StringBuilder builder = new StringBuilder();
            List<String> processSqls = installerStart.getFullSqls();
            TableSqlUtil.appendSqlText(builder, processSqls);
            StreamUtil.writeText(builder, new FileOutputStream(new File(
                    fileName)), "UTF-8", true);
        } catch (Exception e) {
            e.printStackTrace(new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(new File(fileName)), "UTF-8"), true));
        } finally {
            DataBaseUtil.fromSourceLocal.remove();
        }
        LOGGER.logMessage(LogLevel.INFO, "生成数据库变更sql过程结束,总执行时间:{0}",
                System.currentTimeMillis() - start);
    }
}
