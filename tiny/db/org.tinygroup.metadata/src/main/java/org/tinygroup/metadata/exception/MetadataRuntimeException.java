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
package org.tinygroup.metadata.exception;

import org.tinygroup.context.Context;
import org.tinygroup.exception.BaseRuntimeException;

import java.util.Locale;

/**
 * Created by wangwy11342 on 2016/6/8.
 */
public class MetadataRuntimeException extends BaseRuntimeException {

    private static final long serialVersionUID = -2096292363827109578L;

    public MetadataRuntimeException() {
        super();
    }

    public MetadataRuntimeException(String errorCode, Context context) {
        super(errorCode, context);
    }

    public MetadataRuntimeException(String errorCode, Object... params) {
        super(errorCode, params);
    }

    public MetadataRuntimeException(String errorCode, String defaultErrorMsg,
                                    Context context, Locale locale) {
        super(errorCode, defaultErrorMsg, context, locale);
    }

    public MetadataRuntimeException(String errorCode, String defaultErrorMsg,
                                    Locale locale, Object... params) {
        super(errorCode, defaultErrorMsg, locale, params);
    }

    public MetadataRuntimeException(String errorCode, String defaultErrorMsg,
                                    Locale locale, Throwable throwable, Object... params) {
        super(errorCode, defaultErrorMsg, locale, throwable, params);
    }

    public MetadataRuntimeException(String errorCode, String defaultErrorMsg,
                                    Throwable throwable, Object... params) {
        super(errorCode, defaultErrorMsg, throwable, params);
    }

    public MetadataRuntimeException(String errorCode, Throwable throwable,
                                    Object... params) {
        super(errorCode, throwable, params);
    }

    public MetadataRuntimeException(Throwable cause) {
        super(cause);
    }
}
