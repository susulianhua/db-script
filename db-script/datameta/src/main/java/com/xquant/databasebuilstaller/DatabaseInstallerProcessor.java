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


import com.xquant.database.table.dropsql.impl.DropTableSqlProcessorImpl;
import com.xquant.database.util.DataBaseUtil;
import com.xquant.metadata.checkupdate.MetaDataFileInfo;
import com.xquant.metadata.checkupdate.MetaDataFileManager;
import com.xquant.metadata.util.ConfigUtil;
import com.xquant.xmlparser.node.XmlNode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 功能说明:数据库安装类
 * <p>
 * <p>
 * 开发人员: renhui <br>
 * 开发时间: 2013-8-15 <br>
 * <br>
 */
public class DatabaseInstallerProcessor {
    public static final String DATABASE_INSTALLER_BEAN_NAME = "databaseInstaller";
    private static final String defaultLanguage = "oracle";
    private static final String METADATA_TABLE_NAME = "TINY_METADATA_RESOURCE";

    private Logger logger = LoggerFactory
            .getLogger(DatabaseInstallerProcessor.class);
    private String dbLanguage = "";
    private List<InstallProcessor> installProcessors = new ArrayList<InstallProcessor>();
    private XmlNode componentConfig;
    private XmlNode applicationConfig;
    private DataSource dataSource;
    private MetaDataFileManager metaDataFileManager = MetaDataFileManager.getInstance();

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getDbLanguage() {
        return dbLanguage;
    }

    public void setDbLanguage(String dbLanguage) {
        this.dbLanguage = dbLanguage;
    }

    public List<InstallProcessor> getInstallProcessors() {
        return installProcessors;
    }

    public void setInstallProcessors(List<InstallProcessor> installProcessors) {
        this.installProcessors = installProcessors;
    }

    public String getNodeName() {
        return "database-installer";
    }

    public String getLanguage() {
        if ("".equals(dbLanguage)) {
            return defaultLanguage;
        }
        return dbLanguage;
    }


    public List<String> getChangeSqls() {
        return getSqls(false);
    }

    public List<String> getFullSqls() {
        return getSqls(true);
    }

    /**
     * 生成删除语句
     * @return
     */
    public List<String> getDropSqls() {
        return DropTableSqlProcessorImpl.getInstance(dbLanguage).getDropSqls();
    }

    /**
     * 工具对应的方法
     *
     * @param isFull
     * @return
     */
    private List<String> getSqls(boolean isFull) {
        installSort();
        List<String> sqls = new ArrayList<String>();
        Connection con = null;
        try {
            if (DataBaseUtil.fromSourceLocal.get() == null) {
                DataBaseUtil.fromSourceLocal.set("tool");
            }
            if (!isFull) {
                con = DataSourceHolder.getDataSource().getConnection();
            }
            for (InstallProcessor installProcessor : installProcessors) {
                try {
                    List<String> preProcessSqls = installProcessor
                            .getPreProcessSqls(isFull, dbLanguage, con);
                    if (!CollectionUtils.isEmpty(preProcessSqls)) {
                        sqls.addAll(preProcessSqls);
                    }
                } catch (Exception e) {
                    logger.error(
                            "处理器获取前置sql时出现异常,processor:{0},language:{1}", e,
                            installProcessor.getClass(), dbLanguage);
                    throw new RuntimeException(e);
                }
            }
            for (InstallProcessor installProcessor : installProcessors) {
                try {
                    List<String> processSqls = installProcessor.getProcessSqls(
                            isFull, dbLanguage, con);
                    if (!CollectionUtils.isEmpty(processSqls)) {
                        sqls.addAll(processSqls);
                    }
                } catch (Exception e) {
                    logger.error(
                            "处理器获取处理sql时出现异常,processor:{0},language:{1}", e,
                            installProcessor.getClass(), dbLanguage);
                    throw new RuntimeException(e);
                }
            }
            for (InstallProcessor installProcessor : installProcessors) {
                try {
                    List<String> postProcessSqls = installProcessor
                            .getPostProcessSqls(isFull, dbLanguage, con);
                    if (!CollectionUtils.isEmpty(postProcessSqls)) {
                        sqls.addAll(postProcessSqls);
                    }
                } catch (Exception e) {
                    logger.error(
                            "处理器获取后置sql时出现异常,processor:{0},language:{1}", e,
                            installProcessor.getClass(), dbLanguage);
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            DataBaseUtil.fromSourceLocal.remove();
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {

                }
            }
        }
        return sqls;
    }
    // 对安装处理器进行排序
    private void installSort() {
        Collections.sort(installProcessors, new Comparator<InstallProcessor>() {
            public int compare(InstallProcessor o1, InstallProcessor o2) {
                if (o1 != null && o2 != null) {
                    return o1.getOrder() > o2.getOrder() ? 1
                            : (o1.getOrder() == o2.getOrder() ? 0 : -1);
                }
                return 0;
            }
        });
    }
    //核心方法
    public void process() {
        try {
            DataBaseUtil.fromSourceLocal.set("processor");
            logger.info( "开始进行{0}数据库安装处理", dbLanguage);
            installSort(); //对安装处理器进行排序
            process(dbLanguage);   //按顺序执行各个处理器的逻辑
            logger.info("{0}数据库安装处理结束", dbLanguage);
        } finally {
            DataBaseUtil.fromSourceLocal.remove();
        }
    }

    public String getApplicationNodePath() {
        return "/application/database-install-processor/database-installer";
    }

    public String getComponentConfigPath() {
        return null;
    }

    public void config(XmlNode applicationConfig, XmlNode componentConfig) {
        this.applicationConfig = applicationConfig;
        this.componentConfig = componentConfig;
        if (applicationConfig == null) {
            dbLanguage = defaultLanguage;
        } else {
            XmlNode node = applicationConfig.getSubNode("database");
            dbLanguage = node.getAttribute("type");
            if (dbLanguage == null || "".equals(dbLanguage)) {
                dbLanguage = defaultLanguage;
            }

            //todo 封装这部分重复代码
            XmlNode checkStrictNode = applicationConfig.getSubNode("check-strict");
            if (checkStrictNode != null) {
                String checkStrictValue = checkStrictNode.getAttribute("value");
                if ("false".equalsIgnoreCase(checkStrictValue)) {
                    ConfigUtil.setIsCheckStrict(false);
                }
            }
            XmlNode checkModifiedNode = applicationConfig.getSubNode("check-modified");
            if (checkModifiedNode != null) {
                String checkModifiedValue = checkModifiedNode.getAttribute("value");
                if ("false".equalsIgnoreCase(checkModifiedValue)) {
                    ConfigUtil.setIsCheckModified(false);
                }
            }
            XmlNode initdataDelNode = applicationConfig.getSubNode("initdata-conflict-delete");
            if (initdataDelNode != null) {
                String initdataDelNodeValue = initdataDelNode.getAttribute("value");
                if ("true".equalsIgnoreCase(initdataDelNodeValue)) {
                    ConfigUtil.setInitDataDel(true);
                }
            }

            XmlNode char2byteNode = applicationConfig.getSubNode("db-char2byte-length");
            if (char2byteNode != null) {
                String char2byteDbLen = char2byteNode.getAttribute("value");
                if (!StringUtils.isEmpty(char2byteDbLen)) {
                    ConfigUtil.setChar2ByteSize(Integer.valueOf(char2byteDbLen));
                }
            }

            String propertyValue =
                    ConfigUtil.getPropertyValue(applicationConfig, "db-varchar2byte-length", "value");
            if (!StringUtils.isEmpty(propertyValue)) {
                ConfigUtil.setVarchar2ByteSize(Integer.valueOf(propertyValue));
            }

            String useDbTrigger =
                    ConfigUtil.getPropertyValue(applicationConfig, "autoincrement-use-dbtrigger", "value");
            if (!StringUtils.isEmpty(useDbTrigger) && "false".equalsIgnoreCase(useDbTrigger)) {
                ConfigUtil.setUseDbTrigger(false);
            }

            String tableNamePrefix =
                    ConfigUtil.getPropertyValue(applicationConfig,"table-name-prefix","value");
            if(!StringUtils.isEmpty(tableNamePrefix)){
                ConfigUtil.setTableNamePrefix(tableNamePrefix);
            }

        }

        logger.info( "当前数据库语言为:{dbLanguage}", dbLanguage);
    }


    public void start() {
        process();
    }

    public void stop() {

    }


    public void init() {
        if (dataSource != null) {
            DataSourceHolder.setDataSource(dataSource);
        }
        //非工具
        String from = DataBaseUtil.fromSourceLocal.get();
        if (!"tool".equals(from)) {
            Connection con = null;
            try {
                con = DataSourceHolder.getDataSource().getConnection();
                initMetaDataInfoTable(con);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException e) {
                    }
                }
            }
        }

    }
   //循环安装处理器列表，并执行处理器的process方法
    public void process(String language) {
        DataSource dataSource = DataSourceHolder.getDataSource();
        Connection con = null;
        try {
            con = dataSource.getConnection();
            //需要才初始化
            if (DataBaseUtil.isNeedCache()) {
                initMetaDataManager(con);
            }
            List<String> preSqls = new ArrayList<String>();
            for (InstallProcessor installProcessor : installProcessors) {
                try {
                    List<String> preProcessSqls = installProcessor
                            .getPreProcessSqls(false, language, con);
                    if (!CollectionUtils.isEmpty(preProcessSqls)) {
                        preSqls.addAll(preProcessSqls);
                    }
                } catch (Exception e) {
                    logger.error(
                            "处理器获取前置sql时出现异常,processor:{0},language:{1}", e,
                            installProcessor.getClass(), dbLanguage);
                    throw new RuntimeException(e);
                }
            }
            execute(preSqls, con);

            List<String> sqls = new ArrayList<String>();
            for (InstallProcessor installProcessor : installProcessors) {
                try {
                    List<String> processSqls = installProcessor.getProcessSqls(
                            false, language, con);
                    if (!CollectionUtils.isEmpty(processSqls)) {
                        sqls.addAll(processSqls);
                    }
                } catch (Exception e) {
                    logger.error(
                            "处理器获取处理sql时出现异常,processor:{0},language:{1}", e,
                            installProcessor.getClass(), dbLanguage);
                    //出现异常时抛出
                    throw new RuntimeException(e);
                }
            }

            execute(sqls, con);

            List<String> postSqls = new ArrayList<String>();
            for (InstallProcessor installProcessor : installProcessors) {
                try {
                    List<String> postProcessSqls = installProcessor
                            .getPostProcessSqls(false, language, con);
                    if (!CollectionUtils.isEmpty(postProcessSqls)) {
                        postSqls.addAll(postProcessSqls);
                    }
                } catch (Exception e) {
                    logger.error(
                            "处理器获取后置sql时出现异常,processor:{0},language:{1}", e,
                            installProcessor.getClass(), dbLanguage);
                    throw new RuntimeException(e);
                }
            }
            execute(postSqls, con);

            //如果需要缓存则保存
            if (DataBaseUtil.isNeedCache()) {
                execute(metaDataFileManager.getInsertSqls(), con);
                execute(metaDataFileManager.getUpdateSqls(), con);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    private void initMetaDataInfoTable(Connection con) throws SQLException {
        Statement statement = null;
        try {
            boolean metadataExists = checkResourceTableExists(con);
            statement = con.createStatement();
            if (!metadataExists) {
                statement.execute(MetaDataFileManager.TABLE_CREATE_SQL);
            }
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * 判断表是否存在
     * @param con
     * @return
     */
    private boolean checkResourceTableExists(Connection con) {
        Statement statement = null;
        try {
            statement = con.createStatement();
            statement.executeQuery("select * from " + METADATA_TABLE_NAME);
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.debug("statement关闭失败", e);
                }
            }
        }
    }

    private void initMetaDataManager(Connection con) throws SQLException {
        Statement statement = null;
        try {
            statement = con.createStatement();
            ResultSet metaRs = statement.executeQuery("select * from " + METADATA_TABLE_NAME);
            while (metaRs.next()) {
                String type = metaRs.getString("TYPE");
                String resourceId = metaRs.getString("RESOURCE_ID");
                String modifiedTime = metaRs.getString("MODIFIED_TIME");
                MetaDataFileInfo metaDataFileInfo = new MetaDataFileInfo();
                metaDataFileInfo.setResourceId(resourceId);
                metaDataFileInfo.setType(type);
                metaDataFileInfo.setModifiedTime(modifiedTime);
                metaDataFileManager.put(metaDataFileInfo);
            }
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    public void execute(List<String> sqls, Connection con) throws SQLException {
        Statement statement = null;
        try {
            statement = con.createStatement();
            logger.info( "开始执行sql,共{0}句sql", sqls.size());
            for (String sql : sqls) {
                logger.info("执行sql:{0}", sql);
                statement.execute(sql);
            }
            logger.info( "执行sql处理完成");
        } catch (SQLException ex) {
            logger.error("执行sql处理异常，已回滚!");
            throw ex;
        } finally {
            if (statement != null) {
                statement.close();
            }

        }
    }


}
