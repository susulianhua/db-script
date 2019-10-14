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
package org.tinygroup.sequence.exception;

import org.tinygroup.context.Context;
import org.tinygroup.exception.BaseRuntimeException;

import java.util.Locale;

public class SequenceException extends BaseRuntimeException {

    public SequenceException() {
        super();
    }

    public SequenceException(String errorCode, Context context, Locale locale) {
        super(errorCode, context, locale);
    }

    public SequenceException(String errorCode, Context context) {
        super(errorCode, context);
    }

    public SequenceException(String errorCode, Object... params) {
        super(errorCode, params);
    }

    public SequenceException(String errorCode, String defaultErrorMsg,
                             Context context, Locale locale) {
        super(errorCode, defaultErrorMsg, context, locale);
    }

    public SequenceException(String errorCode, String defaultErrorMsg,
                             Locale locale, Object... params) {
        super(errorCode, defaultErrorMsg, locale, params);
    }

    public SequenceException(String errorCode, String defaultErrorMsg,
                             Locale locale, Throwable throwable, Object... params) {
        super(errorCode, defaultErrorMsg, locale, throwable, params);
    }

    public SequenceException(String errorCode, String defaultErrorMsg,
                             Throwable throwable, Object... params) {
        super(errorCode, defaultErrorMsg, throwable, params);
    }

    public SequenceException(String errorCode, Throwable throwable,
                             Object... params) {
        super(errorCode, throwable, params);
    }

    public SequenceException(Throwable cause) {
        super(cause);
    }

}
