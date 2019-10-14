package org.tinygroup.parsedsql.base;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.parsedsql.exception.ParsedSqlException;

/**
 * @author renhui
 */
public class DefaultTableMetaDataProvider implements TableMetaDataProvider {

    private static final String DEFAULT_CATALOG_KEY = "CATALOG_KEY";

    private static final String DEFAULT_SCHEMA_KEY = "SCHEMA_KEY";

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultTableMetaDataProvider.class);

    private Map<String, TableMetaData> caches = new HashMap<String, TableMetaData>();

    public TableMetaData generatedKeyNamesWithMetaData(DataSource dataSource,
                                                       String catalogName, String schemaName, String tableName) {
        String key = buildCacheKey(catalogName, schemaName, tableName);
        if (caches.containsKey(key)) {
            return caches.get(key);
        }
        Connection con = null;
        try {
            TableMetaData tableMetaData = new TableMetaData();
            con = dataSource.getConnection();
            if (con == null) {
                // should only happen in test environments
                throw new ParsedSqlException(
                        "Connection returned by DataSource [" + dataSource
                                + "] was null");
            }
            DatabaseMetaData databaseMetaData = con.getMetaData();
            if (databaseMetaData == null) {
                // should only happen in test environments
                throw new ParsedSqlException(
                        "DatabaseMetaData returned by Connection [" + con
                                + "] was null");
            }
            boolean storesUpperCaseIdentifiers = storeUpper(databaseMetaData);
            boolean storesLowerCaseIdentifiers = storeLower(databaseMetaData);
            String catalogUse = nameToUse(catalogName,
                    storesUpperCaseIdentifiers, storesLowerCaseIdentifiers);
            String schemaUse = nameToUse(schemaName,
                    storesUpperCaseIdentifiers, storesLowerCaseIdentifiers);
            String tableUse = nameToUse(tableName, storesUpperCaseIdentifiers,
                    storesLowerCaseIdentifiers);

            tableMetaData.setCatalogName(catalogUse);
            tableMetaData.setSchemaName(schemaUse);
            tableMetaData.setTableName(tableName);
            tableMetaData.setColumnMetaDatas(getColumnMetaData(catalogUse,
                    schemaUse, tableUse, databaseMetaData));
            tableMetaData.setKeyNames(getKeyNames(catalogUse, schemaUse,
                    tableUse, databaseMetaData));
            caches.put(key, tableMetaData);
            return tableMetaData;
        } catch (SQLException ex) {
            throw new ParsedSqlException(
                    "Error while extracting DatabaseMetaData", ex);
        } catch (AbstractMethodError err) {
            throw new ParsedSqlException(
                    "JDBC DatabaseMetaData method not implemented by JDBC driver - upgrade your driver",
                    err);
        } finally {
        	try {
				con.close();
			} catch (SQLException e) {
				throw new ParsedSqlException(
	                    "Connection close error:", e);
			}
        }

    }

    private String buildCacheKey(String catalogName, String schemaName,
                                 String tableName) {
        StringBuilder cacheKey = new StringBuilder();
        String catalogKey = catalogName;
        if (catalogKey == null) {
            catalogKey = DEFAULT_CATALOG_KEY;
        }
        cacheKey.append(catalogKey).append("-");
        String schemaKey = schemaName;
        if (schemaKey == null) {
            schemaKey = DEFAULT_SCHEMA_KEY;
        }
        cacheKey.append(schemaKey).append("-").append(tableName);
        return cacheKey.toString();
    }

    private List<ColumnMetaData> getColumnMetaData(String catalogUse,
                                                   String schemaUse, String tableUse, DatabaseMetaData databaseMetaData) {
        ResultSet tableColumns = null;
        List<ColumnMetaData> columnMetaDatas = new ArrayList<ColumnMetaData>();
        try {
            tableColumns = databaseMetaData.getColumns(catalogUse, schemaUse,
                    tableUse, null);
            while (tableColumns.next()) {
                ColumnMetaData meta = new ColumnMetaData(
                        tableColumns.getString("COLUMN_NAME"),
                        tableColumns.getInt("DATA_TYPE"),
                        tableColumns.getBoolean("NULLABLE"),
                        tableColumns.getObject("COLUMN_DEF"));
                columnMetaDatas.add(meta);
                LOGGER.logMessage(LogLevel.DEBUG,
                        "Retrieved metadata: " + meta.getParameterName() + " "
                                + meta.getSqlType() + " " + meta.isNullable()
                                + " " + meta.getDefaultValue());
            }
        } catch (SQLException se) {
            LOGGER.logMessage(LogLevel.WARN,
                    "Error while retreiving metadata for procedure columns: "
                            + se.getMessage());
        } finally {
            try {
                if (tableColumns != null) {
                    tableColumns.close();
                }
            } catch (SQLException se) {
                LOGGER.logMessage(LogLevel.WARN,
                        "Problem closing resultset for procedure column metadata "
                                + se.getMessage());
            }
        }

        return columnMetaDatas;
    }

    private String[] getKeyNames(String catalogUse, String schemaUse,
                                 String tableUse, DatabaseMetaData databaseMetaData) {
        ResultSet keyResult = null;
        List<String> keyNames = new ArrayList<String>();
        try {
            keyResult = databaseMetaData.getPrimaryKeys(catalogUse, schemaUse,
                    tableUse);
            while (keyResult != null && keyResult.next()) {
                keyNames.add(keyResult.getString("COLUMN_NAME"));
            }
        } catch (SQLException se) {
            LOGGER.logMessage(LogLevel.WARN,
                    "Error while accessing table meta data results:{0}",
                    se.getMessage());
        } finally {
            if (keyResult != null) {
                try {
                    keyResult.close();
                } catch (SQLException e) {
                    LOGGER.logMessage(LogLevel.WARN,
                            "Error while closing table meta data reults {0}",
                            e.getMessage());
                }
            }
        }
        return keyNames.toArray(new String[0]);
    }

    private boolean storeLower(DatabaseMetaData databaseMetaData) {
        boolean storesLowerCaseIdentifiers = false;
        try {
            storesLowerCaseIdentifiers = databaseMetaData
                    .storesLowerCaseIdentifiers();
        } catch (SQLException se) {
            LOGGER.logMessage(
                    LogLevel.WARN,
                    "Error retrieving 'DatabaseMetaData.storesLowerCaseIdentifiers' -{0} ",
                    se.getMessage());
        }
        return storesLowerCaseIdentifiers;
    }

    private boolean storeUpper(DatabaseMetaData databaseMetaData) {
        boolean storesUpperCaseIdentifiers = false;
        try {
            storesUpperCaseIdentifiers = databaseMetaData
                    .storesUpperCaseIdentifiers();
        } catch (SQLException se) {
            LOGGER.logMessage(
                    LogLevel.WARN,
                    "Error retrieving 'DatabaseMetaData.storesUpperCaseIdentifiers' - {0}",
                    se.getMessage());
        }
        return storesUpperCaseIdentifiers;
    }

    private String nameToUse(String name, boolean storesUpperCaseIdentifiers,
                             boolean storesLowerCaseIdentifiers) {
        if (name == null) {
            return null;
        } else if (storesUpperCaseIdentifiers) {
            return name.toUpperCase();
        } else if (storesLowerCaseIdentifiers) {
            return name.toLowerCase();
        } else {
            return name;
        }
    }

}
