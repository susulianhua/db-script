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
package org.tinygroup.commons.exceptions;

/**
 * 代表未预期的失败。
 *
 * @author renhui
 */
public class UnexpectedFailureException extends RuntimeException {
    private static final long serialVersionUID = -8227335536836081391L;

    public UnexpectedFailureException() {
        super();
    }

    public UnexpectedFailureException(String message) {
        super(message);
    }

    public UnexpectedFailureException(Throwable cause) {
        super(cause);
    }

    public UnexpectedFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
