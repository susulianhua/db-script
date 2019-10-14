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

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.parsedsql.exception.ParsedSqlException;

/**
 * MySQL的UPDATE语句访问器.
 *
 * @author renhui
 */
public class MySQLUpdateVisitor extends AbstractMySQLVisitor {

    private int itemIndex = 0;
    private int itemSize;
    private int processNum;//去除更新项数目

    public boolean visit(final MySqlUpdateStatement x) {
        print("UPDATE ");

        if (x.isLowPriority()) {
            print("LOW_PRIORITY ");
        }

        if (x.isIgnore()) {
            print("IGNORE ");
        }

        x.getTableSource().accept(this);

        println();
        print("SET ");
        itemSize = x.getItems().size();
        for (int i = 0, size = x.getItems().size(); i < size; ++i) {
//            if (i != 0) {
//                print(", ");
//            }
            x.getItems().get(i).accept(this);
        }
        if (processNum == itemSize) {
            throw new ParsedSqlException("sql is incorrect：no update item exist");
        }
        if (x.getWhere() != null) {
            println();
            incrementIndent();
            print("WHERE ");
            x.getWhere().setParent(x);
            x.getWhere().accept(this);
            decrementIndent();
        }

        if (x.getOrderBy() != null) {
            println();
            x.getOrderBy().accept(this);
        }

        if (x.getLimit() != null) {
            println();
            x.getLimit().accept(this);
        }
        return false;
    }


    public boolean visit(final SQLUpdateSetItem x) {
        SQLExpr value = x.getValue();
        if (value instanceof SQLVariantRefExpr) {
            SQLVariantRefExpr refExpr = (SQLVariantRefExpr) value;
            String refName = refExpr.getName();
            boolean exist = getContext().exist(StringUtil.substringAfter(refName, "@"));
            if (itemIndex != 0 && exist) {
                print(", ");
            }
            if (!exist) {
                processNum++;
                print(" ");
                return false;
            }
        }else{
        	if(itemIndex!=0){
        		print(", ");
        	}
        }
        super.visit(x);
        itemIndex++;
        return false;
    }

}
