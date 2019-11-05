package com.xquant.script.controller;

import com.xquant.common.StreamUtil;
import com.xquant.common.TableSqlUtil;
import com.xquant.databasebuilstaller.DatabaseInstallerStart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

/**
 * 表删除语句生成工具
 */
public class TableSqlDropUtil {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DatabaseInstallerStart.class);

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {

        String fileName = "TableSqlDrop.txt";
        long start = System.currentTimeMillis();
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
    }
}
