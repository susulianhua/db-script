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
package org.tinygroup.jsqlparser.expression;

/**
 * A basic class for binary expressions, that is expressions having a left
 * member and a right member which are in turn expressions.
 */
public abstract class BinaryExpression implements Expression {

    private Expression leftExpression;
    private Expression rightExpression;
    private boolean not = false;

    public BinaryExpression() {
    }

    public Expression getLeftExpression() {
        return leftExpression;
    }

    public void setLeftExpression(Expression expression) {
        leftExpression = expression;
    }

    public Expression getRightExpression() {
        return rightExpression;
    }

    public void setRightExpression(Expression expression) {
        rightExpression = expression;
    }

    public void setNot() {
        not = true;
    }

    public boolean isNot() {
        return not;
    }


    public String toString() {
        return (not ? "NOT " : "") + getLeftExpression() + " " + getStringExpression() + " " + getRightExpression();
    }

    public abstract String getStringExpression();
}
