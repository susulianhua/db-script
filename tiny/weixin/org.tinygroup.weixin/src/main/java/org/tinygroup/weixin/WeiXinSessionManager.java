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
package org.tinygroup.weixin;


/**
 * 微信会话管理
 *
 * @author yancheng11334
 */
public interface WeiXinSessionManager {

    /**
     * 默认的bean配置名称
     */
    public static final String DEFAULT_BEAN_NAME = "weiXinSessionManager";

    /**
     * 创建会话
     *
     * @param sessionId
     * @return
     */
    WeiXinSession createWeiXinSession(String sessionId);

    /**
     * 查询会话
     *
     * @param sessionId
     * @return
     */
    WeiXinSession getWeiXinSession(String sessionId);

    /**
     * 添加会话
     *
     * @param session
     */
    void addWeiXinSession(WeiXinSession session);

    /**
     * 手动删除会话
     *
     * @param sessionId
     * @return
     */
    void removeWeiXinSession(String sessionId);

    /**
     * 遍历会话
     *
     * @return
     */
    WeiXinSession[] getWeiXinSessions();

    /**
     * 清理会话过期的Session
     */
    void expireWeiXinSessions();

    /**
     * 清理全部Session
     */
    void clear();

    /**
     * Session最大过期时间设置，单位s，默认0
     *
     * @return
     */
    int getMaxInactiveInterval();

    /**
     * Session清理线程首次延迟时间，单位s,默认值60
     *
     * @return
     */
    int getExpireTimerDelay();

    /**
     * Session清理线程运行周期，单位s，默认值300
     *
     * @return
     */
    int getExpireTimePeriod();
}
