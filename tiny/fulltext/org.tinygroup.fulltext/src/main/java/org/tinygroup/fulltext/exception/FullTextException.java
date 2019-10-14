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
package org.tinygroup.fulltext.exception;

/**
 * 全文检索异常
 *
 * @author yancheng11334
 */
public class FullTextException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 5212305943513874678L;

    public FullTextException() {
        super();
    }

    public FullTextException(String message) {
        super(message);
    }

    public FullTextException(String message, Throwable cause) {
        super(message, cause);
    }

    public FullTextException(Throwable cause) {
        super(cause);
    }
}
