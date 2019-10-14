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

import java.io.Serializable;

/**
 * A fetch clause in the form FETCH (FIRST | NEXT) row_count (ROW | ROWS) ONLY
 */
public class Fetch implements Serializable {

    private long rowCount;
    private boolean fetchJdbcParameter = false;
    private boolean isFetchParamFirst = false;
    private String fetchParam = "ROW";

    public long getRowCount() {
        return rowCount;
    }

    public void setRowCount(long l) {
        rowCount = l;
    }

    public boolean isFetchJdbcParameter() {
        return fetchJdbcParameter;
    }

    public void setFetchJdbcParameter(boolean b) {
        fetchJdbcParameter = b;
    }

    public String getFetchParam() {
        return fetchParam;
    }

    public void setFetchParam(String s) {
        this.fetchParam = s;
    }

    public boolean isFetchParamFirst() {
        return isFetchParamFirst;
    }

    public void setFetchParamFirst(boolean b) {
        this.isFetchParamFirst = b;
    }

    @Override
    public String toString() {
        return " FETCH " + (isFetchParamFirst ? "FIRST" : "NEXT") + " " + (fetchJdbcParameter ? "?" : rowCount + "") + " " + fetchParam + " ONLY";
    }
}
