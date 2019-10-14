package org.tinygroup.parsedsql;

import java.util.Collection;
import java.util.HashSet;

import org.tinygroup.parsedsql.base.Column;
import org.tinygroup.parsedsql.base.SQLStatementType;
import org.tinygroup.parsedsql.base.Table;

public class SqlParsedResult {
	
	private SQLStatementType sqlStatementType;

	private SQLBuilder sqlBuilder;
	
	private final Collection<Column> columns=new HashSet<Column>();
	private Table table;

	public SQLStatementType getSqlStatementType() {
		return sqlStatementType;
	}

	public void setSqlStatementType(SQLStatementType sqlStatementType) {
		this.sqlStatementType = sqlStatementType;
	}

	public SQLBuilder getSqlBuilder() {
		return sqlBuilder;
	}

	public void setSqlBuilder(SQLBuilder sqlBuilder) {
		this.sqlBuilder = sqlBuilder;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public Collection<Column> getColumns() {
		return columns;
	}

}
