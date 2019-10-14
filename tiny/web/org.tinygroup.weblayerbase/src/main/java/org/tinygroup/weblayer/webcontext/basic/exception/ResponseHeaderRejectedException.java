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
package org.tinygroup.weblayer.webcontext.basic.exception;

/**
 * 代表一个response header被拒绝的异常。
 *
 * @author renhui
 */
public class ResponseHeaderRejectedException extends RuntimeException {
    private static final long serialVersionUID = -5208648752795414458L;

    public ResponseHeaderRejectedException() {
    }

    public ResponseHeaderRejectedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResponseHeaderRejectedException(String message) {
        super(message);
    }

    public ResponseHeaderRejectedException(Throwable cause) {
        super(cause);
    }
}
