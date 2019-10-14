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
package org.tinygroup.weblayer.webcontext.basic.interceptor;

/**
 * 检查status消息。
 *
 * @author renhui
 */
public interface StatusMessageInterceptor extends ResponseHeaderInterceptor {
    /**
     * 检查status消息。
     *
     * @return 返回值表示修改status消息，返回<code>null</code>则表示不设置status消息。
     */
    String checkStatusMessage(int sc, String msg);
}
