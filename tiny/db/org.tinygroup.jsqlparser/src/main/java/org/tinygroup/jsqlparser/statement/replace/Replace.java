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
package org.tinygroup.jsqlparser.statement.replace;

import org.tinygroup.jsqlparser.expression.Expression;
import org.tinygroup.jsqlparser.expression.operators.relational.ItemsList;
import org.tinygroup.jsqlparser.schema.Column;
import org.tinygroup.jsqlparser.schema.Table;
import org.tinygroup.jsqlparser.statement.Statement;
import org.tinygroup.jsqlparser.statement.StatementVisitor;
import org.tinygroup.jsqlparser.statement.select.PlainSelect;

import java.util.List;

/**
 * The replace statement.
 */
public class Replace implements Statement {

    private Table table;
    private List<Column> columns;
    private ItemsList itemsList;
    private List<Expression> expressions;
    private boolean useValues = true;


    public void accept(StatementVisitor statementVisitor) {
        statementVisitor.visit(this);
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table name) {
        table = name;
    }

    /**
     * A list of {@link org.tinygroup.jsqlparser.schema.Column}s either from a "REPLACE
     * mytab (col1, col2) [...]" or a "REPLACE mytab SET col1=exp1, col2=exp2".
     *
     * @return a list of {@link org.tinygroup.jsqlparser.schema.Column}s
     */
    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> list) {
        columns = list;
    }

    /**
     * An {@link ItemsList} (either from a "REPLACE mytab VALUES (exp1,exp2)" or
     * a "REPLACE mytab SELECT * FROM mytab2") it is null in case of a "REPLACE
     * mytab SET col1=exp1, col2=exp2"
     */
    public ItemsList getItemsList() {
        return itemsList;
    }

    public void setItemsList(ItemsList list) {
        itemsList = list;
    }

    /**
     * A list of {@link org.tinygroup.jsqlparser.expression.Expression}s (from a
     * "REPLACE mytab SET col1=exp1, col2=exp2"). <br>
     * it is null in case of a "REPLACE mytab (col1, col2) [...]"
     */
    public List<Expression> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<Expression> list) {
        expressions = list;
    }

    public boolean isUseValues() {
        return useValues;
    }

    public void setUseValues(boolean useValues) {
        this.useValues = useValues;
    }


    public String toString() {
        StringBuilder sql = new StringBuilder();
        sql.append("REPLACE ").append(table);

        if (expressions != null && columns != null) {
            // the SET col1=exp1, col2=exp2 case
            sql.append(" SET ");
            // each element from expressions match up with a column from columns.
            for (int i = 0, s = columns.size(); i < s; i++) {
                sql.append(columns.get(i)).append("=").append(expressions.get(i));
                sql.append((i < s - 1) ? ", " : "");
            }
        } else if (columns != null) {
            // the REPLACE mytab (col1, col2) [...] case
            sql.append(" ").append(PlainSelect.getStringList(columns, true, true));
        }

        if (itemsList != null) {
            // REPLACE mytab SELECT * FROM mytab2
            // or VALUES ('as', ?, 565)

            if (useValues) {
                sql.append(" VALUES");
            }

            sql.append(" ").append(itemsList);
        }

        return sql.toString();
    }
}
