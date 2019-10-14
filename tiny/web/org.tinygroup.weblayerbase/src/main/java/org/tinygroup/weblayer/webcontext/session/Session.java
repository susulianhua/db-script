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
package org.tinygroup.weblayer.webcontext.session;

import java.io.Serializable;

/**
 * 内部session接口
 *
 * @author renhui
 */
public interface Session extends Serializable {

    /**
     * 取得session ID。
     *
     * @return session ID
     */
    String getSessionID();

    /**
     * 取得session的创建时间。
     *
     * @return 创建时间戮
     */
    long getCreationTime();

    /**
     * 取得最近访问时间。
     *
     * @return 最近访问时间戮
     */
    long getLastAccessedTime();

    /**
     * 取得session的最大不活动期限，超过此时间，session就会失效。
     *
     * @return 不活动期限的秒数
     */
    int getMaxInactiveInterval();

    /**
     * 判断session有没有过期。
     *
     * @return 如果过期了，则返回<code>true</code>
     */
    boolean isExpired();

    /**
     * session失效
     */
    void invalidate();


}
