package org.tinygroup.databasechange;

import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.io.*;
import java.util.List;

/**
 * 表删除语句生成工具
 */
public class TableSqlDropUtil {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DatabaseInstallerStart.class);

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {

        if (args.length != 1) {
            System.exit(0);
        }
        String fileName = args[0];
        long start = System.currentTimeMillis();
        LOGGER.logMessage(LogLevel.INFO, "开始生成数据库删表sql");
        try {
            DatabaseInstallerStart installerStart = new DatabaseInstallerStart();
            List<String> dropTableSqls = installerStart.getDropSqls();
            StringBuilder builder = new StringBuilder();
            TableSqlUtil.appendSqlText(builder, dropTableSqls);
            StreamUtil.writeText(builder, new FileOutputStream(new File(
                    fileName)), "UTF-8", true);
        } catch (Exception e) {
            e.printStackTrace(new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(new File(fileName)), "UTF-8"), true));
        }
        LOGGER.logMessage(LogLevel.INFO, "生成数据库删表sql过程结束,总执行时间:{0}",
                System.currentTimeMillis() - start);
    }
}
