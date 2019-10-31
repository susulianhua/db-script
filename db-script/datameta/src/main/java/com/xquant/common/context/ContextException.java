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
package com.xquant.common.context;

public class ContextException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1904537543205756914L;

    public ContextException(String info) {
        super(info);
    }

    public ContextException(Throwable e) {
        super(e);
    }

    public ContextException(String info, Throwable e) {
        super(info, e);
    }
}
