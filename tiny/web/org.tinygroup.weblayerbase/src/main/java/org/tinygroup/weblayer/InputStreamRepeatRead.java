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
package org.tinygroup.weblayer;

import javax.servlet.http.HttpServletRequest;

/**
 * request输入流可进行重复读的处理类
 *
 * @author renhui
 *
 */
public class InputStreamRepeatRead {

    private boolean open = true;

    public InputStreamRepeatRead(boolean open) {
        super();
        this.open = open;
    }

    public HttpServletRequest requestWrapper(HttpServletRequest request) {
        if (open && isPostMethod(request)) {
            return new MultipleReadServletRequest(request);
        }
        return request;
    }

    private boolean isPostMethod(HttpServletRequest request) {
        return request.getMethod().equalsIgnoreCase("post");
    }

}
