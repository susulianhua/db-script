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
package org.tinygroup.jdbctemplatedslsession.pageprocess;

import org.tinygroup.tinysqldsl.ComplexSelect;
import org.tinygroup.tinysqldsl.Select;
import org.tinygroup.tinysqldsl.extend.InformixComplexSelect;
import org.tinygroup.tinysqldsl.extend.InformixSelect;

/**
 * @author wangwy
 */
public class InformixPageSqlMatchProcess extends AbstractPageSqlMatchProcess {

    @Override
    protected String internalSqlProcess(Select select, int start, int limit) {
        InformixSelect informixSelect = new InformixSelect();
        informixSelect.setPlainSelect(select.getPlainSelect());
        informixSelect.page(start, limit);
        return informixSelect.parsedSql();
    }

    @Override
    protected String dbType() {
        return "Informix";
    }

    @Override
    public String getCountSql(String originSql) {
        return "select count(*) from (" + originSql + ") as count_temp";
    }

    @Override
    protected String internalComplexSelectSqlProcess(
            ComplexSelect complexSelect, int start, int limit) {
        InformixComplexSelect informixComplexSelect = new InformixComplexSelect();
        informixComplexSelect.setOperationList(complexSelect.getOperationList());
        informixComplexSelect.page(start, limit);
        return informixComplexSelect.parsedSql();
    }

}
