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

import org.tinygroup.tinysqldsl.ComplexSelect;
import org.tinygroup.tinysqldsl.Select;
import org.tinygroup.tinysqldsl.operator.SetOperationInstanceCallBack;
import org.tinygroup.tinysqldsl.select.*;

import java.util.ArrayList;
import java.util.List;

/**
 * db2复杂查询操作对象
 *
 * @author renhui
 */
public class DB2ComplexSelect extends ComplexSelect<DB2ComplexSelect> {

    public DB2ComplexSelect() {
        super();
    }

    @SuppressWarnings("rawtypes")
    public static DB2ComplexSelect union(Select... selects) {
        return setOperation(new SetOperationInstanceCallBack() {
            public SetOperation instanceOperation() {
                return new UnionOperation();
            }
        }, selects);
    }

    @SuppressWarnings("rawtypes")
    public static DB2ComplexSelect unionAll(Select... selects) {
        return setOperation(new SetOperationInstanceCallBack() {
            public SetOperation instanceOperation() {
                return new UnionOperation(true);
            }
        }, selects);
    }

    @SuppressWarnings("rawtypes")
    public static DB2ComplexSelect setOperation(
            SetOperationInstanceCallBack instance, Select... selects) {
        DB2ComplexSelect complexSelect = new DB2ComplexSelect();
        List<PlainSelect> plainSelects = new ArrayList<PlainSelect>();
        List<SetOperation> operations = new ArrayList<SetOperation>();
        for (int i = 0; i < selects.length; i++) {
            Select select = selects[i];
            plainSelects.add(select.getPlainSelect());
            if (i != 0) {
                operations.add(instance.instanceOperation());
            }
        }
        complexSelect.operationList.setOpsAndSelects(plainSelects, operations);
        return complexSelect;
    }

    @SuppressWarnings("rawtypes")
    public static DB2ComplexSelect minus(Select... selects) {
        return setOperation(new SetOperationInstanceCallBack() {
            public SetOperation instanceOperation() {
                return new MinusOperation();
            }
        }, selects);
    }

    @SuppressWarnings("rawtypes")
    public static DB2ComplexSelect intersect(Select... selects) {
        return setOperation(new SetOperationInstanceCallBack() {
            public SetOperation instanceOperation() {
                return new IntersectOperation();
            }
        }, selects);
    }

    public DB2ComplexSelect page(int start, int limit) {
        this.stringBuilder = getLimitString(sql(), start, limit);
        return this;
    }
}
