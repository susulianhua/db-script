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
package org.tinygroup.jsqlparser.schema;

import org.tinygroup.jsqlparser.expression.Expression;
import org.tinygroup.jsqlparser.expression.ExpressionVisitor;

import java.io.Serializable;

/**
 * A column. It can have the table name it belongs to.
 */
public final class Column implements Expression, MultiPartName, Serializable {

    private Table table;
    private String columnName;

    public Column() {
    }

    public Column(Table table, String columnName) {
        setTable(table);
        setColumnName(columnName);
    }

    public Column(String columnName) {
        this(null, columnName);
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String string) {
        columnName = string;
    }


    public String getFullyQualifiedName() {
        StringBuilder fqn = new StringBuilder();

        if (table != null) {
            fqn.append(table.getFullyQualifiedName());
        }
        if (fqn.length() > 0) {
            fqn.append('.');
        }
        if (columnName != null) {
            fqn.append(columnName);
        }
        return fqn.toString();
    }


    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }


    public String toString() {
        return getFullyQualifiedName();
    }
}