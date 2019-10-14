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

import java.util.List;

import org.tinygroup.commons.tools.Assert;
import org.tinygroup.context.Context;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.parsedsql.SQLParser;
import org.tinygroup.parsedsql.SqlParsedResult;
import org.tinygroup.parsedsql.base.DatabaseType;
import org.tinygroup.parsedsql.base.SQLStatementType;
import org.tinygroup.parsedsql.exception.ParsedSqlException;
import org.tinygroup.parsedsql.parser.SQLVisitor;
import org.tinygroup.parsedsql.parser.SQLVisitorRegistry;
import org.tinygroup.parsedsql.parser.VisitorLogProxy;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.dialect.db2.parser.DB2StatementParser;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.oracle.parser.OracleStatementParser;
import com.alibaba.druid.sql.dialect.postgresql.parser.PGSQLStatementParser;
import com.alibaba.druid.sql.dialect.sqlserver.parser.SQLServerStatementParser;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;
import com.google.common.base.Preconditions;

public class DefaultSQLParser implements SQLParser {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultSQLParser.class);

    private static SQLStatementParser getSQLStatementParser(
            final DatabaseType databaseType, final String sql) {
        switch (databaseType) {
            case H2:
            case MySQL:
                return new MySqlStatementParser(sql);
            case Oracle:
                return new OracleStatementParser(sql);
            case SQLServer:
                return new SQLServerStatementParser(sql);
            case DB2:
                return new DB2StatementParser(sql);
            case PostgreSQL:
                return new PGSQLStatementParser(sql);
            default:
                throw new ParsedSqlException("Cannot support database type [{0}]",
                        databaseType);
        }
    }

    private static SQLASTOutputVisitor getSQLVisitor(
            final DatabaseType databaseType, final SQLStatement sqlStatement) {
        if (sqlStatement instanceof SQLSelectStatement) {
            return VisitorLogProxy.enhance(SQLVisitorRegistry
                    .getSelectVistor(databaseType));
        }
        if (sqlStatement instanceof SQLInsertStatement) {
            return VisitorLogProxy.enhance(SQLVisitorRegistry
                    .getInsertVistor(databaseType));
        }
        if (sqlStatement instanceof SQLUpdateStatement) {
            return VisitorLogProxy.enhance(SQLVisitorRegistry
                    .getUpdateVistor(databaseType));
        }
        if (sqlStatement instanceof SQLDeleteStatement) {
            return VisitorLogProxy.enhance(SQLVisitorRegistry
                    .getDeleteVistor(databaseType));
        }
        throw new ParsedSqlException("Unsupported SQL statement: [{0}]",
                sqlStatement);
    }

    @Override
    public SqlParsedResult parse(DatabaseType databaseType, String sql,
                        Context context) {
    	LOGGER.debugMessage("Logic SQL: {0}", sql);
		SQLStatementParser sqlStatementParser = getSQLStatementParser(
				databaseType, sql);
		List<SQLStatement> sqlStatements = sqlStatementParser
				.parseStatementList();
		Assert.assertTrue(sqlStatements.size() == 1,
				"the length of sqlStatements must be one");
		SQLStatement sqlStatement = sqlStatements.get(0);
		LOGGER.debugMessage("Get {0} SQL Statement", sqlStatement.getClass()
				.getName());
        SQLASTOutputVisitor visitor = getSQLVisitor(databaseType, sqlStatement);
        Preconditions.checkArgument(visitor instanceof SQLVisitor);
        SQLVisitor sqlVisitor = (SQLVisitor) visitor;
        sqlVisitor.setContext(context);
        sqlStatement.accept(visitor);
        SqlParsedResult parsedResult=sqlVisitor.getParsedResult();
        parsedResult.setSqlBuilder(sqlVisitor.getSQLBuilder());
        parsedResult.setSqlStatementType(getType(sqlStatement));
        return parsedResult;
    }
    
    private SQLStatementType getType(SQLStatement sqlStatement) {
		if (sqlStatement instanceof SQLSelectStatement) {
			return SQLStatementType.SELECT;
		}
		if (sqlStatement instanceof SQLInsertStatement) {
			return SQLStatementType.INSERT;
		}
		if (sqlStatement instanceof SQLUpdateStatement) {
			return SQLStatementType.UPDATE;
		}
		if (sqlStatement instanceof SQLDeleteStatement) {
			return SQLStatementType.DELETE;
		}
		return SQLStatementType.OTHER;
	}
}
