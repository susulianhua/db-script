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
package org.tinygroup.parsedsql;

import static org.dbunit.Assertion.assertEquals;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.dbunit.DatabaseUnitException;
import org.dbunit.IDatabaseTester;
import org.dbunit.database.CachedResultSetTable;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.ResultSetTableMetaData;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.h2.H2Connection;
import org.dbunit.ext.mysql.MySqlConnection;
import org.dbunit.operation.DatabaseOperation;
import org.h2.tools.RunScript;
import org.junit.Before;
import org.tinygroup.parsedsql.base.DatabaseType;

/**
 * 使用dbunit进行数据库测试的基类
 * @author renhui
 *
 */
public abstract class AbstractDBUnitTest {

    protected static final DatabaseType CURRENT_DB_TYPE = DatabaseType.H2;

    private static final Map<String, DataSource> DATA_SOURCES = new HashMap<String, DataSource>();

    private final DataBaseEnvironment dbEnv = new DataBaseEnvironment(
            CURRENT_DB_TYPE);

    @Before
    public void createSchema() throws SQLException {
        for (String each : getSchemaFiles()) {
            Connection conn = createDataSource(getFileName(each)).getConnection();
            RunScript.execute(conn, new InputStreamReader(
                    AbstractDBUnitTest.class.getClassLoader()
                            .getResourceAsStream(each)));
            conn.close();
        }
    }

    @Before
    public final void importDataSet() throws Exception {
        for (String each : getDataSetFiles()) {
            InputStream is = AbstractDBUnitTest.class.getClassLoader()
                    .getResourceAsStream(each);
            IDataSet dataSet = new FlatXmlDataSetBuilder()
                    .build(new InputStreamReader(is));
            IDatabaseTester databaseTester = new DatabaseTester(
                    dbEnv.getDriverClassName(),
                    dbEnv.getURL(getFileName(each)), dbEnv.getUsername(),
                    dbEnv.getPassword());
            databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
            databaseTester.setDataSet(dataSet);
            databaseTester.onSetup();
        }
    }

    protected abstract List<String> getSchemaFiles();

    protected abstract List<String> getDataSetFiles();

    protected final Map<String, DataSource> createDataSourceMap(
            final String dataSourceName) {
        Map<String, DataSource> result = new HashMap<String, DataSource>(
                getDataSetFiles().size());
        for (String each : getDataSetFiles()) {
            result.put(String.format(dataSourceName, getFileName(each)),
                    createDataSource(getFileName(each)));
        }
        return result;
    }

    protected DataSource createDataSource(final String dataSetFile) {
        if (DATA_SOURCES.containsKey(dataSetFile)) {
            return DATA_SOURCES.get(dataSetFile);
        }
        BasicDataSource result = new BasicDataSource();
        result.setDriverClassName(dbEnv.getDriverClassName());
        result.setUrl(dbEnv.getURL(getFileName(dataSetFile)));
        result.setUsername(dbEnv.getUsername());
        result.setPassword(dbEnv.getPassword());
//		result.setTimeBetweenEvictionRunsMillis(1000);
//		result.setMinEvictableIdleTimeMillis(1000);
//		result.setMaxIdle(5);
        result.setMaxActive(1000);
        DATA_SOURCES.put(dataSetFile, result);
        return result;
    }

    private String getFileName(final String dataSetFile) {
        String fileName = new File(dataSetFile).getName();
        if (-1 == fileName.lastIndexOf(".")) {
            return fileName;
        }
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    protected void assertDataSet(final String expectedDataSetFile,
                                 final Connection connection, final String actualTableName,
                                 final String sql, boolean canClose, final Object... params) throws SQLException,
            DatabaseUnitException {
        Connection conn = connection;
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            int i = 1;
            for (Object each : params) {
                ps.setObject(i++, each);
            }
            ITable actualTable = getConnection(connection).createTable(
                    actualTableName, ps);
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
                    .build(new InputStreamReader(AbstractDBUnitTest.class
                            .getClassLoader().getResourceAsStream(
                                    expectedDataSetFile)));
            assertEquals(expectedDataSet.getTable(actualTableName), actualTable);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (conn != null && canClose) {
                conn.close();
            }
        }
    }

    protected void assertDataSet(final String expectedDataSetFile,
                                 final Connection connection, final String actualTableName,
                                 final String sql, boolean canClose) throws SQLException, DatabaseUnitException {
        Connection conn = connection;
        try {
            ITable actualTable = getConnection(connection).createQueryTable(
                    actualTableName, sql);
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
                    .build(new InputStreamReader(AbstractDBUnitTest.class
                            .getClassLoader().getResourceAsStream(
                                    expectedDataSetFile)));
            ITable filteredActualTable = DefaultColumnFilter.includedColumnsTable(actualTable, expectedDataSet.getTable(actualTableName).getTableMetaData().getColumns());
            assertEquals(expectedDataSet.getTable(actualTableName), filteredActualTable);
        } finally {
            if (conn != null && canClose) {
                conn.close();
            }
        }
    }

    private IDatabaseConnection getConnection(final Connection connection)
            throws DatabaseUnitException {
        switch (dbEnv.getDatabaseType()) {
            case H2:
                return new H2Connection(connection, "PUBLIC");
            case MySQL:
                return new MySqlConnection(connection, "db_single");
            default:
                throw new UnsupportedOperationException(dbEnv.getDatabaseType()
                        .name());
        }
    }

    private ITable getITable(ResultSet resultSet, Connection connection, String tableName) throws DatabaseUnitException, SQLException {
        IDatabaseConnection iDatabaseConnection = getConnection(connection);
        boolean caseSensitiveTableNames = iDatabaseConnection.getConfig().getFeature(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES);
        ITableMetaData metaData = new ResultSetTableMetaData(tableName, resultSet, iDatabaseConnection, caseSensitiveTableNames);
        ITable table = new CachedResultSetTable(metaData, resultSet);
        return table;
    }


    protected void assertDataSet(ResultSet resultSet, Connection connection, final String expectedDataSetFile, String actualTableName)
            throws DatabaseUnitException, SQLException {
        ITable itable = getITable(resultSet, connection, actualTableName);
        IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
                .build(new InputStreamReader(AbstractDBUnitTest.class
                        .getClassLoader().getResourceAsStream(
                                expectedDataSetFile)));
        assertEquals(itable, expectedDataSet.getTable(actualTableName));
    }

    protected void assertResultSetAndExpectedSql(ResultSet resultSet, Connection connection, final String expectedSql, String actualTableName)
            throws DatabaseUnitException, SQLException {
        ITable actualTable = getITable(resultSet, connection, actualTableName);
        ITable expectedITable = getConnection(connection).createQueryTable(
                actualTableName, expectedSql);
        assertEquals(expectedITable, actualTable);
    }
}