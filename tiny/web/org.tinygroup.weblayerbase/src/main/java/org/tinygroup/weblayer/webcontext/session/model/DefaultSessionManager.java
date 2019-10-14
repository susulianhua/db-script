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

import org.tinygroup.weblayer.webcontext.session.Session;
import org.tinygroup.weblayer.webcontext.session.SessionManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认实现，存放在内存中
 *
 * @author renhui
 */
public class DefaultSessionManager implements SessionManager {

    private static SessionManager manager;
    private final Object lock = new Object();
    protected Map<String, Session> sessions = new ConcurrentHashMap<String, Session>();

    private DefaultSessionManager() {

    }

    public static SessionManager getSingleton() {
        if (manager == null) {
            synchronized (SessionManager.class) {
                if (manager == null) {
                    manager = new DefaultSessionManager();
                }
            }
        }
        return manager;
    }

    public void addSession(Session session) {
        synchronized (lock) {
            sessions.put(session.getSessionID(), session);
        }
    }

    public void expireSession(Session session) {
        synchronized (lock) {
            sessions.remove(session.getSessionID());
        }
    }

    public Session[] queryAllSessions() {
        synchronized (lock) {
            Session[] httpSessions = new Session[sessions.size()];
            int index = 0;
            for (Session session : sessions.values()) {
                httpSessions[index++] = session;
            }
            return httpSessions;
        }
    }

    public Session querySessionById(String sessionId) {
        synchronized (lock) {
            return sessions.get(sessionId);
        }
    }

}
