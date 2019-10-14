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
package org.tinygroup.parsedsql.visitor;

import java.util.List;

import org.tinygroup.parsedsql.base.Column;
import org.tinygroup.parsedsql.base.Table;
import org.tinygroup.parsedsql.util.SQLUtil;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;

/**
 * MySQL的INSERT语句访问器.
 *
 * @author renhui
 */
public class MySQLInsertVisitor extends AbstractMySQLVisitor {
	@Override
	public boolean visit(final MySqlInsertStatement x) {
		SQLName expr = x.getTableName();
		String tableName = expr.toString();
		String schema = null;
		if (expr instanceof SQLPropertyExpr) {
			SQLPropertyExpr propertyExpr = (SQLPropertyExpr) expr;
			tableName = propertyExpr.getSimpleName();
			schema = propertyExpr.getOwner().toString();
		}
		tableName = SQLUtil.getExactlyValue(tableName);
		getParsedResult().setTable(new Table(tableName, schema, x.getAlias()));
		List<SQLExpr> columns = x.getColumns();
		for (SQLExpr sqlExpr : columns) {
			String columnName = SQLUtil.getExactlyValue(sqlExpr.toString());
			Column column = new Column(columnName, tableName);
			getParsedResult().getColumns().add(column);
		}
		return super.visit(x);
	}

}
