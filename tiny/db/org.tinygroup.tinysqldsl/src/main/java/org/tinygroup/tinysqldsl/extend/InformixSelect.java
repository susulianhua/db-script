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
package org.tinygroup.tinysqldsl.extend;

import org.tinygroup.tinysqldsl.Select;
import org.tinygroup.tinysqldsl.base.Table;
import org.tinygroup.tinysqldsl.formitem.FromItemList;
import org.tinygroup.tinysqldsl.selectitem.AllColumns;
import org.tinygroup.tinysqldsl.selectitem.SelectItem;

/**
 * informix数据库相关的查询
 *
 * @author renhui
 */
public class InformixSelect extends Select<InformixSelect> {

    public InformixSelect() {
        super();
    }

    public static InformixSelect select(SelectItem... selectItems) {
        InformixSelect select = new InformixSelect();
        select.getPlainSelect().addSelectItems(selectItems);
        return select;
    }

    public static InformixSelect selectFrom(Table... tables) {
        InformixSelect select = new InformixSelect();
        select.getPlainSelect().addSelectItems(new AllColumns());
        select.getPlainSelect().setFromItem(new FromItemList(tables));
        return select;
    }


    public InformixSelect into(Table... tables) {
        plainSelect.addIntoTables(tables);
        return this;
    }

    public InformixSelect page(int start, int limit) {
        this.stringBuilder = getLimitString(sql(), start, limit);
        return this;
    }

    public StringBuilder getLimitString(String sql, int start, int limit) {

        StringBuilder pagingSelect = new StringBuilder(sql.length() + 8).append(sql);
        if (start <= 0) {
            pagingSelect.insert(sql.toLowerCase().indexOf("select") + 6
                    , " first " + limit);
            return pagingSelect;
        }
        pagingSelect.insert(sql.toLowerCase().indexOf("select") + 6, " skip " + start + " first " + limit);
        return pagingSelect;
    }

    @Override
    protected Select newSelect() {
        return new InformixSelect();
    }
}
