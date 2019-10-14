package org.tinygroup.parsedsql.base;

import org.tinygroup.parsedsql.exception.ParsedSqlException;

/**
 * 支持的数据库类型.
 *
 * @author renhui
 */
public enum DatabaseType {

	H2("H2"), MySQL("MySQL"), Oracle("Oracle"), SQLServer("SQL Server"), DB2(
			"DB2"), PostgreSQL("PostgreSQL");

	private String aliasName;

	DatabaseType(String aliasName) {
		this.aliasName = aliasName;
	}

	public String getAliasName() {
		return aliasName;
	}

	/**
	 * 获取数据库类型枚举.
	 *
	 * @param databaseProductName
	 *            数据库类型
	 * @return 数据库类型枚举
	 */
	public static DatabaseType valueFrom(final String databaseProductName) {
		for (DatabaseType databaseType : values()) {
			if (databaseProductName.contains(databaseType.name())
					|| databaseProductName
							.contains(databaseType.getAliasName())) {
				return databaseType;
			}
		}
		throw new ParsedSqlException("Can not support database type [{0}].",
				databaseProductName);

	}
}
