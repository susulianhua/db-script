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
package org.tinygroup.database.table;

import org.tinygroup.database.config.table.ForeignReference;
import org.tinygroup.database.config.table.Table;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TableSort implements Comparator<Table> {

    public int compare(Table table1, Table table2) {
        List<String> dependTables1 = getDependList(table1);
        List<String> dependTables2 = getDependList(table2);
        if (dependTables1.size() > 0 && dependTables2.size() == 0) {
            return 1;
        }
        boolean contains1 = dependTables1.contains(table2.getId());
        boolean contains2 = dependTables2.contains(table1.getId());
        if (contains1 && contains2) {
            throw new RuntimeException(String.format(
                    "表1[name:%s]与表2[name:%s]相互依赖", table1.getName(),
                    table2.getName()));
        } else if (contains1) {
            return 1;
        } else if (contains2) {
            return -1;
        }
        return 0;
    }

    private List<String> getDependList(Table table) {
        List<ForeignReference> foreigns = table.getForeignReferences();
        List<String> dependencies = new ArrayList<String>();
        for (ForeignReference foreignReference : foreigns) {
            dependencies.add(foreignReference.getMainTable());
        }
        return dependencies;
    }
}
