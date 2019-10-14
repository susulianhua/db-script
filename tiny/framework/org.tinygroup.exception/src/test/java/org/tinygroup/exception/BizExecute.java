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
package org.tinygroup.exception;

import java.util.Locale;

public class BizExecute {
    private Locale locale = new Locale("zh", "CN");

    public void execute() {
        throw new BaseRuntimeException("0TE111011027", "", locale);
    }

    /**
     * 既能找到code对应msg又有默认msg
     */
    public void executeCodeWithMsg() {
        String msg = null;
        throw new BaseRuntimeException("0TE111011027", "default msg", locale);
    }

    /**
     * 没有对应msg
     */
    public void executeNoCodeVal() {
        String msg = null;
        throw new BaseRuntimeException("0TE111011028", msg, locale);
    }


    public void executeWithMsg() {
        throw new BaseRuntimeException("0TE111011028", "default msg", locale);
    }

    public void executeNoParam() {
        String msg = null;
        throw new BaseRuntimeException("", msg, locale);
    }

    public void executeBaseException() {
        throw new BaseRuntimeException(new Exception("from throwable"));
    }

    public void executeCodeWithMsgWithThrowable(String errorCode, String errormsg, Throwable e, Object... params) {
        throw new BaseRuntimeException(errorCode, errormsg, locale, e, params);
    }

    public void executeCodeMsg(String errorCode, String defaultErrorMsg, Object... params) {
        throw new BaseRuntimeException(errorCode, defaultErrorMsg, locale, params);
    }


    public void executeEmptyCodeWithMsg() {
        throw new BaseRuntimeException("", "default msg", locale);
    }
}
