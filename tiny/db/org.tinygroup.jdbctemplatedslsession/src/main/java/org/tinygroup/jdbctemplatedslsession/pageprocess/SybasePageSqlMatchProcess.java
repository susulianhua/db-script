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

import org.tinygroup.jdbctemplatedslsession.exception.UnsupportedSqlOperationException;
import org.tinygroup.tinysqldsl.ComplexSelect;
import org.tinygroup.tinysqldsl.Select;

public class SybasePageSqlMatchProcess extends AbstractPageSqlMatchProcess {

    @Override
    protected String internalSqlProcess(Select select, int start, int limit) {
        throw new UnsupportedSqlOperationException("0TE120059001", dbType());
    }

    @Override
    public String getCountSql(String originSql) {
        //sybase count语句中存在order by都会报错，又因为分页语句实际上也不支持,所以直接抛异常
        throw new UnsupportedSqlOperationException("0TE120059001", dbType());
    }

    @Override
    protected String dbType() {
        return "Adaptive Server Enterprise";
    }

    @Override
    protected String internalComplexSelectSqlProcess(
            ComplexSelect complexSelect, int start, int limit) {
        throw new UnsupportedSqlOperationException("0TE120059001", dbType());
    }


}
