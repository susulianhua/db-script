package org.tinygroup.parsedsql.base;

import com.google.common.base.Objects;

public final class Column {

	private final String columnName;

	private final String tableName;

	public Column(String columnName, String tableName) {
		super();
		this.columnName = columnName;
		this.tableName = tableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public String getTableName() {
		return tableName;
	}

	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Column column = (Column) obj;
		return Objects.equal(this.columnName.toUpperCase(),
				column.columnName.toUpperCase())
				&& Objects.equal(this.tableName.toUpperCase(),
						column.tableName.toUpperCase());
	}

	public int hashCode() {
		return Objects.hashCode(columnName.toUpperCase(),
				tableName.toUpperCase());
	}

	@Override
	public String toString() {
		return "Column [columnName=" + columnName + ", tableName=" + tableName
				+ "]";
	}

}