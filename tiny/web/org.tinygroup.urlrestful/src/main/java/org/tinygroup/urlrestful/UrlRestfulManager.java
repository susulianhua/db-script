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
package org.tinygroup.urlrestful;

import org.tinygroup.urlrestful.config.Rules;

/**
 * restful管理器
 *
 * @author renhui
 */
public interface UrlRestfulManager {

    String URL_RESTFUL_XSTREAM = "urlrestful";

    /**
     * 增加restful配置信息
     *
     * @param Rules
     */
    void addRules(Rules Rules);

    /**
     * 移除restful配置信息
     *
     * @param Rules
     */
    void removeRules(Rules Rules);

    /**
     * 根据请求路径、请求的方法以及请求头的accept 组装此次请求的上下文对象
     *
     * @param requestPath
     * @param httpMethod
     * @param accept
     * @return
     */
    Context getContext(String requestPath, String httpMethod, String accept);


}
