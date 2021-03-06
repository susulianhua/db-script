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
package com.xquant.database.exception;


import static java.lang.String.format;


/**
 * Created by wangwy11342 on 2016/6/8.
 */
public class DatabaseRuntimeException extends RuntimeException {
    String errorCode;

    private static final long serialVersionUID = -2096292363827109578L;

    public DatabaseRuntimeException() {
        super();
    }


    public DatabaseRuntimeException(String errorCode, Object... params) {
        this.errorCode = format(errorCode, params);
    }

    public DatabaseRuntimeException(Throwable cause) {
        super(cause);
    }

    @Override
    public String toString(){
        return this.errorCode;
    }
}
