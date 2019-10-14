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
package org.tinygroup.jsqlparser.statement.select;

import java.util.List;

/**
 * One of the parts of a "WITH" clause of a "SELECT" statement
 */
public class WithItem implements SelectBody {

    private String name;
    private List<SelectItem> withItemList;
    private SelectBody selectBody;

    /**
     * The name of this WITH item (for example, "myWITH" in "WITH myWITH AS
     * (SELECT A,B,C))"
     *
     * @return the name of this WITH
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * The {@link SelectBody} of this WITH item is the part after the "AS"
     * keyword
     *
     * @return {@link SelectBody} of this WITH item
     */
    public SelectBody getSelectBody() {
        return selectBody;
    }

    public void setSelectBody(SelectBody selectBody) {
        this.selectBody = selectBody;
    }

    /**
     * The {@link SelectItem}s in this WITH (for example the A,B,C in "WITH
     * mywith (A,B,C) AS ...")
     *
     * @return a list of {@link SelectItem}s
     */
    public List<SelectItem> getWithItemList() {
        return withItemList;
    }

    public void setWithItemList(List<SelectItem> withItemList) {
        this.withItemList = withItemList;
    }


    public String toString() {
        return name + ((withItemList != null) ? " " + PlainSelect.getStringList(withItemList, true, true) : "")
                + " AS (" + selectBody + ")";
    }


    public void accept(SelectVisitor visitor) {
        visitor.visit(this);
    }
}
