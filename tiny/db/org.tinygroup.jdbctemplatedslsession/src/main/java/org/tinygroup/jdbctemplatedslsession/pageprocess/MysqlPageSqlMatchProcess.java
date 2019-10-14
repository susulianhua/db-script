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
import org.tinygroup.tinysqldsl.extend.MysqlComplexSelect;
import org.tinygroup.tinysqldsl.extend.MysqlSelect;
import org.tinygroup.tinysqldsl.select.Limit;

/**
 * @author renhui
 */
public class MysqlPageSqlMatchProcess extends AbstractPageSqlMatchProcess {

    @Override
    protected String internalSqlProcess(Select select, int start, int limit) {
        MysqlSelect mysqlSelect = new MysqlSelect();
        mysqlSelect.setPlainSelect(select.getPlainSelect());
        mysqlSelect.limit(start, limit);
        return mysqlSelect.parsedSql();
    }

    @Override
    protected String dbType() {
        return "MySQL";
    }

    @Override
    protected String internalComplexSelectSqlProcess(
            ComplexSelect complexSelect, int start, int limit) {
        MysqlComplexSelect mysql = new MysqlComplexSelect();
        mysql.setOperationList(complexSelect.getOperationList());
        mysql.limit(new Limit(start, limit));
        return mysql.parsedSql();
    }

}
