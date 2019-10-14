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
import org.tinygroup.tinysqldsl.extend.H2ComplexSelect;
import org.tinygroup.tinysqldsl.extend.H2Select;

/**
 * @author renhui
 */
public class H2PageSqlMatchProcess extends AbstractPageSqlMatchProcess {

    @Override
    protected String internalSqlProcess(Select select, int start, int limit) {
        H2Select h2Select = new H2Select();
        h2Select.setPlainSelect(select.getPlainSelect());
        h2Select.limit(start, limit);
        return h2Select.parsedSql();
    }

    @Override
    protected String dbType() {
        return "H2";
    }

    @Override
    protected String internalComplexSelectSqlProcess(
            ComplexSelect complexSelect, int start, int limit) {
        H2ComplexSelect h2ComplexSelect = new H2ComplexSelect();
        h2ComplexSelect.setOperationList(complexSelect.getOperationList());
        h2ComplexSelect.limit(start, limit);
        return h2ComplexSelect.parsedSql();
    }
}
