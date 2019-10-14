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
package org.tinygroup.weblayer.webcontext.form.exception;

import org.tinygroup.weblayer.webcontext.WebContextException;

/**
 * 表单重复提交异常。
 * <p>
 * 这是由于用户误操作产生的异常，严重程度较低。但如果允许表单可以重复提交，
 * 潜在可能存在商业风险。因此，当检测到表单重复提交时，以本异常抛出。
 *
 * @author renhui
 */
public class DuplicateFormSubmitException extends WebContextException {


    /**
     *
     */
    private static final long serialVersionUID = -1182760785292528626L;

    public DuplicateFormSubmitException() {
        super();
    }

    public DuplicateFormSubmitException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateFormSubmitException(String message) {
        super(message);
    }

    public DuplicateFormSubmitException(Throwable cause) {
        super(cause);
    }

}