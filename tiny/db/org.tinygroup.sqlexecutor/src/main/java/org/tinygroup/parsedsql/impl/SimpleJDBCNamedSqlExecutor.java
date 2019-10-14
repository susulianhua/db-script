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
package org.tinygroup.parsedsql.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlRowSetResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.tinygroup.context.Context;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.parsedsql.JDBCNamedSqlExecutor;
import org.tinygroup.parsedsql.ParsedSql;
import org.tinygroup.parsedsql.ResultSetCallback;
import org.tinygroup.parsedsql.SQLParser;
import org.tinygroup.parsedsql.SqlParsedResult;
import org.tinygroup.parsedsql.base.Column;
import org.tinygroup.parsedsql.base.DatabaseType;
import org.tinygroup.parsedsql.base.DefaultTableMetaDataProvider;
import org.tinygroup.parsedsql.base.SQLStatementType;
import org.tinygroup.parsedsql.base.TableMetaData;
import org.tinygroup.parsedsql.base.TableMetaDataProvider;
import org.tinygroup.parsedsql.util.NamedParameterUtils;

/**
 *
 * @author ballackhui
 *
 */
public class SimpleJDBCNamedSqlExecutor implements JDBCNamedSqlExecutor {

	/** Default maximum number of entries for this template's SQL cache: 256 */
	public static final int DEFAULT_CACHE_LIMIT = 256;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SimpleJDBCNamedSqlExecutor.class);
	private volatile int cacheLimit = DEFAULT_CACHE_LIMIT;
	/** Cache of original SQL String to ParsedSql representation */
	@SuppressWarnings("serial")
	private final Map<String, ParsedSql> parsedSqlCache = new LinkedHashMap<String, ParsedSql>(
			DEFAULT_CACHE_LIMIT, 0.75f, true) {
		@Override
		protected boolean removeEldestEntry(Map.Entry<String, ParsedSql> eldest) {
			return size() > getCacheLimit();
		}
	};
	private SQLParser sqlParser;
	public static final String GENERATE_KEY = "GENERATE_KEY";

	private TableMetaDataProvider tableMetaDataProvider;

	public SimpleJDBCNamedSqlExecutor() {
		super();
		tableMetaDataProvider = new DefaultTableMetaDataProvider();
	}

	/**
	 * Return the maximum number of entries for this template's SQL cache.
	 */
	public int getCacheLimit() {
		return this.cacheLimit;
	}

	/**
	 * Specify the maximum number of entries for this template's SQL cache.
	 * Default is 256.
	 */
	public void setCacheLimit(int cacheLimit) {
		this.cacheLimit = cacheLimit;
	}

	public SQLParser getSqlParser() {
		return sqlParser;
	}

	public void setSqlParser(SQLParser sqlParser) {
		this.sqlParser = sqlParser;
	}

	@Override
	public SqlRowSet queryForSqlRowSet(String sql, DataSource dataSource,
			Context context) throws SQLException {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query(
				getPreparedStatementCreator(sql, dataSource, context,new GetGeneratedKeys()),
				new SqlRowSetResultSetExtractor());
	}

	/**
	 * 
	 * @param sql
	 * @param dataSource
	 * @param context
	 * @param generatedKeys 是否需要通过statement.getGeneratedKeys方式生成key值
	 * @return
	 * @throws SQLException
	 */
	private PreparedStatementCreator getPreparedStatementCreator(String sql,
			DataSource dataSource, Context context,GetGeneratedKeys generatedKeys) throws SQLException {
		SqlParsedResult sqlParsedResult = null;
		if (sqlParser != null) {
			LOGGER.logMessage(LogLevel.DEBUG, "before SQLParser SQL：{0}", sql);
			DatabaseType databaseType = getDatabaseTypeWithDataSource(dataSource);
			sqlParsedResult = sqlParser.parse(databaseType, sql, context);
			sql = sqlParsedResult.getSqlBuilder().toSQL();
			LOGGER.logMessage(LogLevel.DEBUG, "after SQLParser SQL：{0}", sql);
		}
		SqlParameterSource paramSource = new MapSqlParameterSource(
				context.getTotalItemMap());
		ParsedSql parsedSql = getParsedSql(sql);
		String sqlToUse = NamedParameterUtils.substituteNamedParameters(
				parsedSql, paramSource);
		Object[] params = NamedParameterUtils.buildValueArray(parsedSql,
				paramSource, null);
		List<SqlParameter> declaredParameters = NamedParameterUtils
				.buildSqlParameterList(parsedSql, paramSource);
		PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
				sqlToUse, declaredParameters);
		if (sqlParsedResult != null
				&& sqlParsedResult.getSqlStatementType().equals(
						SQLStatementType.INSERT)) {
			boolean exist = judgePrimaryExist(dataSource, sqlParsedResult);
			if (!exist) {
				pscf.setReturnGeneratedKeys(true);
				generatedKeys.setGetGeneratedKeys(true);
			}
		}
		return pscf.newPreparedStatementCreator(params);
	}

	private DatabaseType getDatabaseTypeWithDataSource(DataSource dataSource)
			throws SQLException {
		String databaseTypeStr;
		Connection connection = dataSource.getConnection();
		try {
			databaseTypeStr = connection.getMetaData().getDatabaseProductName();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		DatabaseType databaseType = DatabaseType.valueFrom(databaseTypeStr);
		return databaseType;
	}

	@Override
	public int execute(String sql, DataSource dataSource, Context context)
			throws SQLException {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		GetGeneratedKeys getGeneratedKeys = new GetGeneratedKeys();
		PreparedStatementCreator preparedStatementCreator = getPreparedStatementCreator(
				sql, dataSource, context,getGeneratedKeys);
		if (getGeneratedKeys.isGetGeneratedKeys()) {
			KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
			int count = jdbcTemplate.update(preparedStatementCreator,
					generatedKeyHolder);
			if (!generatedKeyHolder.getKeyList().isEmpty()) {
				context.put(GENERATE_KEY, generatedKeyHolder.getKey());
			}
			return count;
		}
		return jdbcTemplate.update(preparedStatementCreator);
	}

	private boolean judgePrimaryExist(DataSource dataSource,
			SqlParsedResult sqlParsedResult) {
		TableMetaData tableMetaData = tableMetaDataProvider
				.generatedKeyNamesWithMetaData(dataSource, null, null,
						sqlParsedResult.getTable().getName());
		String[] keyNames = tableMetaData.getKeyNames();
		Collection<Column> columns = sqlParsedResult.getColumns();
		boolean primaryKeyExist = false;
		for (int i = 0; i < keyNames.length; i++) {
			String keyName = keyNames[i];
			for (Column column : columns) {
				String columnName = column.getColumnName();
				if (columnName.equalsIgnoreCase(keyName)) {
					primaryKeyExist = true;
					break;
				}
			}
		}
		return primaryKeyExist;
	}

	protected ParsedSql getParsedSql(String sql) {
		if (getCacheLimit() <= 0) {
			return NamedParameterUtils.parseSqlStatement(sql);
		}
		synchronized (this.parsedSqlCache) {
			ParsedSql parsedSql = this.parsedSqlCache.get(sql);
			if (parsedSql == null) {
				parsedSql = NamedParameterUtils.parseSqlStatement(sql);
				this.parsedSqlCache.put(sql, parsedSql);
			}
			return parsedSql;
		}
	}

	@Override
	public void extractResultSetCallback(String sql, DataSource dataSource,
			Context context, ResultSetCallback callback) throws SQLException {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.query(
				getPreparedStatementCreator(sql, dataSource, context,new GetGeneratedKeys()),
				new TinyResultSetCallback(callback));
	}
	
	class GetGeneratedKeys{
		private boolean getGeneratedKeys;

		public boolean isGetGeneratedKeys() {
			return getGeneratedKeys;
		}

		public void setGetGeneratedKeys(boolean getGeneratedKeys) {
			this.getGeneratedKeys = getGeneratedKeys;
		}
	}

}
