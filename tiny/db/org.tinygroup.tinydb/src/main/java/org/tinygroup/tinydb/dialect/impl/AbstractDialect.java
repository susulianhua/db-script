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
package org.tinygroup.tinydb.dialect.impl;

import org.springframework.dao.DataAccessException;
import org.tinygroup.database.dialectfunction.DialectFunctionProcessor;
import org.tinygroup.database.dialectfunction.impl.DialectFunctionProcessorImpl;
import org.tinygroup.tinydb.dialect.Dialect;

import javax.sql.DataSource;

public abstract class AbstractDialect implements Dialect {

    protected DialectFunctionProcessor functionProcessor = new DialectFunctionProcessorImpl();
    protected int paddingLength = 0;
    private DataSource dataSource;
    private String incrementerName;

    public boolean supportsLimit() {
        return false;
    }

    /**
     * Return the data source to retrieve the value from.
     */
    public DataSource getDataSource() {
        return this.dataSource;
    }

    /**
     * Set the data source to retrieve the value from.
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Return the name of the sequence/table.
     */
    public String getIncrementerName() {
        return this.incrementerName;
    }

    /**
     * Set the name of the sequence/table.
     */
    public void setIncrementerName(String incrementerName) {
        this.incrementerName = incrementerName;
    }

    /**
     * Return the padding length for String values.
     */
    public int getPaddingLength() {
        return this.paddingLength;
    }

    /**
     * Set the padding length, i.e. the length to which a string result
     * should be pre-pended with zeroes.
     */
    public void setPaddingLength(int paddingLength) {
        this.paddingLength = paddingLength;
    }

    public int nextIntValue() throws DataAccessException {
        return getNextKey();
    }

    public long nextLongValue() throws DataAccessException {
        return getNextKey();
    }

    public String nextStringValue() throws DataAccessException {
        String s = Long.toString(getNextKey());
        int len = s.length();
        if (len < this.paddingLength) {
            StringBuffer buf = new StringBuffer(this.paddingLength);
            for (int i = 0; i < this.paddingLength - len; i++) {
                buf.append('0');
            }
            buf.append(s);
            s = buf.toString();
        }
        return s;
    }

    public DialectFunctionProcessor getFunctionProcessor() {
        return functionProcessor;
    }

    public void setFunctionProcessor(DialectFunctionProcessor functionProcessor) {
        this.functionProcessor = functionProcessor;
    }

}
