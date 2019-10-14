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
package org.tinygroup.jsqlparser.util;

import org.tinygroup.jsqlparser.expression.Alias;
import org.tinygroup.jsqlparser.expression.BinaryExpression;
import org.tinygroup.jsqlparser.statement.select.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Connect all selected expressions with a binary expression. Out of select a,b
 * from table one gets select a || b as expr from table. The type of binary
 * expression is set by overwriting this class abstract method
 * createBinaryExpression.
 *
 * @author tw
 */
public abstract class ConnectExpressionsVisitor implements SelectVisitor, SelectItemVisitor {

    private final List<SelectExpressionItem> itemsExpr = new LinkedList<SelectExpressionItem>();
    private String alias = "expr";

    public ConnectExpressionsVisitor() {
    }

    public ConnectExpressionsVisitor(String alias) {
        this.alias = alias;
    }

    /**
     * Create instances of this binary expression that connects all selected
     * expressions.
     *
     * @return
     */
    protected abstract BinaryExpression createBinaryExpression();


    public void visit(PlainSelect plainSelect) {
        for (SelectItem item : plainSelect.getSelectItems()) {
            item.accept(this);
        }

        if (itemsExpr.size() > 1) {
            BinaryExpression binExpr = createBinaryExpression();
            binExpr.setLeftExpression(itemsExpr.get(0).getExpression());
            for (int i = 1; i < itemsExpr.size() - 1; i++) {
                binExpr.setRightExpression(itemsExpr.get(i).getExpression());
                BinaryExpression binExpr2 = createBinaryExpression();
                binExpr2.setLeftExpression(binExpr);
                binExpr = binExpr2;
            }
            binExpr.setRightExpression(itemsExpr.get(itemsExpr.size() - 1).getExpression());

            SelectExpressionItem sei = new SelectExpressionItem();
            sei.setExpression(binExpr);

            plainSelect.getSelectItems().clear();
            plainSelect.getSelectItems().add(sei);
        }

        ((SelectExpressionItem) plainSelect.getSelectItems().get(0)).setAlias(new Alias(alias));
    }


    public void visit(SetOperationList setOpList) {
        for (PlainSelect select : setOpList.getPlainSelects()) {
            select.accept(this);
        }
    }


    public void visit(WithItem withItem) {
    }


    public void visit(AllTableColumns allTableColumns) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public void visit(AllColumns allColumns) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public void visit(SelectExpressionItem selectExpressionItem) {
        itemsExpr.add(selectExpressionItem);
    }
}
