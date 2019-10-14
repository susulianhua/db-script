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

public class SqlServerPageSqlMatchProcess extends AbstractPageSqlMatchProcess {

    @Override
    protected String dbType() {
        return "SQL Server";
    }

    @Override
    public String getCountSql(String originSql) {
        //sqlserver count语句中存在order by会报错，又因为分页语句支持的也不是很好，所以直接抛出异常
        throw new UnsupportedSqlOperationException("0TE120059001", dbType());
    }

    /**
     *
     * @param select
     * @param start
     * @param limit
     * @return
     */
    @Override
    protected String internalSqlProcess(Select select, int start, int limit) {
        //sqlserver本身支持分页，但是要结合order by唯一字段来确定rownum，如果order by不唯一rownum还会重复
        //故暂时不支持
        throw new UnsupportedSqlOperationException("0TE120059001", dbType());
    }

    @Override
    protected String internalComplexSelectSqlProcess(
            ComplexSelect complexSelect, int start, int limit) {
        throw new UnsupportedSqlOperationException("0TE120059001", dbType());
    }

}
