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
package org.tinygroup.parsedsql.exception;

import org.tinygroup.exception.BaseRuntimeException;

public class ParsedSqlException extends BaseRuntimeException {

    private static final long serialVersionUID = -7596862467651803772L;

    public ParsedSqlException() {
        super();
    }

    public ParsedSqlException(String errorCode, Object... params) {
        super(errorCode, params);
    }

    public ParsedSqlException(String errorCode, Throwable throwable,
                              Object... params) {
        super(errorCode, throwable, params);
    }

    public ParsedSqlException(Throwable cause) {
        super(cause);
    }

}
