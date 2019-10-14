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
package org.tinygroup.weblayer.webcontext.session.model;

import org.tinygroup.cache.Cache;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.weblayer.webcontext.session.Session;
import org.tinygroup.weblayer.webcontext.session.SessionManager;

import java.util.Set;

/**
 * 默认实现，存放在缓存中
 *
 * @author renhui
 */
public class CacheSessionManager implements SessionManager {

    public static final String SESSION_GROUP = "sessionGroup";
    private final Object lock = new Object();
    private Cache sessionCaches;

    public Cache getSessionCaches() {
        return sessionCaches;
    }

    public void setSessionCaches(Cache sessionCaches) {
        this.sessionCaches = sessionCaches;
    }

    public void addSession(Session session) {
        synchronized (lock) {
            sessionCaches.put(SESSION_GROUP, session.getSessionID(), session);
        }
    }

    public void expireSession(Session session) {
        synchronized (lock) {
            sessionCaches.remove(SESSION_GROUP, session.getSessionID());
        }
    }

    public Session[] queryAllSessions() {
        synchronized (lock) {
            Set<String> keys = sessionCaches.getGroupKeys(SESSION_GROUP);
            if (CollectionUtil.isEmpty(keys)) {
                return new Session[0];
            }
            Session[] Sessions = new Session[keys.size()];
            int index = 0;
            for (String key : keys) {
                Sessions[index++] = querySessionById(key);
            }
            return Sessions;
        }
    }

    public Session querySessionById(String sessionId) {
        synchronized (lock) {
            return (Session) sessionCaches.get(SESSION_GROUP, sessionId);
        }
    }


}