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
package org.tinygroup.jdbctemplatedslsession.daosupport;


import org.tinygroup.tinysqldsl.base.Column;
import org.tinygroup.tinysqldsl.expression.Expression;
import org.tinygroup.tinysqldsl.select.OrderByElement;

/**
 * 排序信息
 *
 * @author renhui
 */
public class OrderBy {

    private OrderByElement orderByElement;

    public OrderBy(Expression expression, boolean asc) {
        super();
        if (asc) {
            orderByElement = OrderByElement.asc(expression);
        } else {
            orderByElement = OrderByElement.desc(expression);
        }
    }

    public OrderBy(String columnName, boolean asc) {
        super();
        if (asc) {
            orderByElement = OrderByElement.asc(new Column(columnName));
        } else {
            orderByElement = OrderByElement.desc(new Column(columnName));
        }
    }

    public OrderBy(Column column, boolean asc) {
        super();
        if (asc) {
            orderByElement = OrderByElement.asc(column);
        } else {
            orderByElement = OrderByElement.desc(column);
        }
    }

    public OrderByElement getOrderByElement() {
        return orderByElement;
    }

}
