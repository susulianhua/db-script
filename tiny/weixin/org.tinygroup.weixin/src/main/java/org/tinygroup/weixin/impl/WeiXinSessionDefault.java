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
package org.tinygroup.weixin.impl;

import org.tinygroup.weixin.WeiXinSession;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class WeiXinSessionDefault implements WeiXinSession {

    /**
     *
     */
    private static final long serialVersionUID = -7387128460312069070L;

    private String sessionId;

    private long creationTime;

    private long lastAccessedTime;

    private int maxInactiveInterval;

    /**
     * 参数缓存对象
     */
    private Map<String, Serializable> parameterMaps = new HashMap<String, Serializable>();

    /**
     * 默认方案采用微信用户Id作为会话Id
     *
     * @param sessionId
     */
    public WeiXinSessionDefault(String sessionId) {
        this(sessionId, 0);
    }

    /**
     * 默认方案采用微信用户Id作为会话Id
     *
     * @param sessionId
     * @param maxInactiveInterval
     */
    public WeiXinSessionDefault(String sessionId, int maxInactiveInterval) {
        this.creationTime = System.currentTimeMillis();
        this.lastAccessedTime = creationTime;
        this.sessionId = sessionId;
        this.maxInactiveInterval = maxInactiveInterval;
    }


    public String getSessionId() {
        return this.sessionId;
    }

    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    public void setMaxInactiveInterval(int maxInactiveInterval) {
        this.maxInactiveInterval = maxInactiveInterval;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    public boolean isExpired() {
        if (maxInactiveInterval == 0) {
            return false;
        } else if (lastAccessedTime + maxInactiveInterval * 1000 < System.currentTimeMillis()) {
            return true;
        }
        return false;
    }

    public void update() {
        this.lastAccessedTime = System.currentTimeMillis();
    }

    public boolean contains(String name) {
        return parameterMaps.containsKey(name);
    }

    @SuppressWarnings("unchecked")
    public <T extends Serializable> T getParameter(String name) {
        return (T) parameterMaps.get(name);
    }

    public <T extends Serializable> void setParameter(String name, T value) {
        if (value == null) {
            parameterMaps.remove(name);
        } else {
            parameterMaps.put(name, value);
        }
    }

}
