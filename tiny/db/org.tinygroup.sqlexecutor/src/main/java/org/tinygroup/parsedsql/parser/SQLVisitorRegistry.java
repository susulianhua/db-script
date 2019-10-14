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
package org.tinygroup.parsedsql.parser;

import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;
import org.tinygroup.parsedsql.base.DatabaseType;
import org.tinygroup.parsedsql.exception.ParsedSqlException;
import org.tinygroup.parsedsql.visitor.MySQLDeleteVisitor;
import org.tinygroup.parsedsql.visitor.MySQLInsertVisitor;
import org.tinygroup.parsedsql.visitor.MySQLSelectVisitor;
import org.tinygroup.parsedsql.visitor.MySQLUpdateVisitor;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author renhui
 *
 */
public final class SQLVisitorRegistry {

    private static final Map<DatabaseType, Class<? extends SQLASTOutputVisitor>> SELECT_REGISTRY = new HashMap<DatabaseType, Class<? extends SQLASTOutputVisitor>>(
            DatabaseType.values().length);

    private static final Map<DatabaseType, Class<? extends SQLASTOutputVisitor>> INSERT_REGISTRY = new HashMap<DatabaseType, Class<? extends SQLASTOutputVisitor>>(
            DatabaseType.values().length);

    private static final Map<DatabaseType, Class<? extends SQLASTOutputVisitor>> UPDATE_REGISTRY = new HashMap<DatabaseType, Class<? extends SQLASTOutputVisitor>>(
            DatabaseType.values().length);

    private static final Map<DatabaseType, Class<? extends SQLASTOutputVisitor>> DELETE_REGISTRY = new HashMap<DatabaseType, Class<? extends SQLASTOutputVisitor>>(
            DatabaseType.values().length);

    static {
        registerSelectVistor();
        registerInsertVistor();
        registerUpdateVistor();
        registerDeleteVistor();
    }

    private static void registerSelectVistor() {
        SELECT_REGISTRY.put(DatabaseType.H2, MySQLSelectVisitor.class);
        SELECT_REGISTRY.put(DatabaseType.MySQL, MySQLSelectVisitor.class);
        // TODO 其他数据库先使用MySQL, 只能使用标准SQL
//        SELECT_REGISTRY.put(DatabaseType.Oracle, OracleSelectVisitor.class);
//        SELECT_REGISTRY.put(DatabaseType.SQLServer, SqlServerSelectVisitor.class);
//        SELECT_REGISTRY.put(DatabaseType.DB2, DB2SelectVisitor.class);
//        SELECT_REGISTRY.put(DatabaseType.PostgreSQL, PostgresqlSelectVisitor.class);
    }

    private static void registerInsertVistor() {
        INSERT_REGISTRY.put(DatabaseType.H2, MySQLInsertVisitor.class);
        INSERT_REGISTRY.put(DatabaseType.MySQL, MySQLInsertVisitor.class);
        // TODO 其他数据库先使用MySQL, 只能使用标准SQL
//        INSERT_REGISTRY.put(DatabaseType.Oracle, OracleInsertVisitor.class);
//        INSERT_REGISTRY.put(DatabaseType.SQLServer, SqlServerInsertVisitor.class);
//        INSERT_REGISTRY.put(DatabaseType.DB2, DB2InsertVisitor.class);
//        INSERT_REGISTRY.put(DatabaseType.PostgreSQL, PostgresqlInsertVisitor.class);
    }

    private static void registerUpdateVistor() {
        UPDATE_REGISTRY.put(DatabaseType.H2, MySQLUpdateVisitor.class);
        UPDATE_REGISTRY.put(DatabaseType.MySQL, MySQLUpdateVisitor.class);
        // TODO 其他数据库先使用MySQL, 只能使用标准SQL
//		UPDATE_REGISTRY.put(DatabaseType.Oracle, OracleUpdateVisitor.class);
//		UPDATE_REGISTRY.put(DatabaseType.SQLServer, SqlServerUpdateVisitor.class);
//		UPDATE_REGISTRY.put(DatabaseType.DB2, DB2UpdateVisitor.class);
//		UPDATE_REGISTRY.put(DatabaseType.PostgreSQL, PostgresqlUpdateVisitor.class);
    }

    private static void registerDeleteVistor() {
        DELETE_REGISTRY.put(DatabaseType.H2, MySQLDeleteVisitor.class);
        DELETE_REGISTRY.put(DatabaseType.MySQL, MySQLDeleteVisitor.class);
        // TODO 其他数据库先使用MySQL, 只能使用标准SQL
//		DELETE_REGISTRY.put(DatabaseType.Oracle, OracleDeleteVisitor.class);
//		DELETE_REGISTRY.put(DatabaseType.SQLServer, SqlServerDeleteVisitor.class);
//		DELETE_REGISTRY.put(DatabaseType.DB2, DB2DeleteVisitor.class);
//		DELETE_REGISTRY.put(DatabaseType.PostgreSQL, PostgresqlDeleteVisitor.class);
    }

    /**
     * 获取SELECT访问器.
     *
     * @param databaseType
     *            数据库类型
     * @return SELECT访问器
     */
    public static Class<? extends SQLASTOutputVisitor> getSelectVistor(
            final DatabaseType databaseType) {
        return getVistor(databaseType, SELECT_REGISTRY);
    }

    /**
     * 获取INSERT访问器.
     *
     * @param databaseType
     *            数据库类型
     * @return INSERT访问器
     */
    public static Class<? extends SQLASTOutputVisitor> getInsertVistor(
            final DatabaseType databaseType) {
        return getVistor(databaseType, INSERT_REGISTRY);
    }

    /**
     * 获取UPDATE访问器.
     *
     * @param databaseType
     *            数据库类型
     * @return UPDATE访问器
     */
    public static Class<? extends SQLASTOutputVisitor> getUpdateVistor(
            final DatabaseType databaseType) {
        return getVistor(databaseType, UPDATE_REGISTRY);
    }

    /**
     * 获取DELETE访问器.
     *
     * @param databaseType
     *            数据库类型
     * @return DELETE访问器
     */
    public static Class<? extends SQLASTOutputVisitor> getDeleteVistor(
            final DatabaseType databaseType) {
        return getVistor(databaseType, DELETE_REGISTRY);
    }

    private static Class<? extends SQLASTOutputVisitor> getVistor(
            final DatabaseType databaseType,
            final Map<DatabaseType, Class<? extends SQLASTOutputVisitor>> registry) {
        if (!registry.containsKey(databaseType)) {
            throw new ParsedSqlException("Cannot support database type [{0}]",
                    databaseType.name());
        }
        return registry.get(databaseType);
    }
}