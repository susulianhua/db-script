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
package org.tinygroup.jsqlparser.statement.create.table;

import org.tinygroup.jsqlparser.statement.select.PlainSelect;

import java.util.List;

/**
 * An index (unique, primary etc.) in a CREATE TABLE statement
 */
public class Index {

    private String type;
    private List<String> columnsNames;
    private String name;

    /**
     * A list of strings of all the columns regarding this index
     */
    public List<String> getColumnsNames() {
        return columnsNames;
    }

    public void setColumnsNames(List<String> list) {
        columnsNames = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String string) {
        name = string;
    }

    /**
     * The type of this index: "PRIMARY KEY", "UNIQUE", "INDEX"
     */
    public String getType() {
        return type;
    }

    public void setType(String string) {
        type = string;
    }


    public String toString() {
        return type + " " + PlainSelect.getStringList(columnsNames, true, true) + (name != null ? " " + name : "");
    }
}
