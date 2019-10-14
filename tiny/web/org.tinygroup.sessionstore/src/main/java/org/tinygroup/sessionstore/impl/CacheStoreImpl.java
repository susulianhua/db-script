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
package org.tinygroup.sessionstore.impl;

import org.tinygroup.cache.Cache;
import org.tinygroup.commons.tools.Assert;
import org.tinygroup.sessionstore.CacheStore;
import org.tinygroup.weblayer.webcontext.session.SessionConfig;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class CacheStoreImpl implements CacheStore {

    private Cache cache;

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    public void init(String storeName, SessionConfig sessionConfig)
            throws Exception {
        Assert.assertNotNull(cache, "cache is required");
    }

    public Iterable<String> getAttributeNames(String sessionID,
                                              StoreContext storeContext) {
        Set<String> sessionData = cache.getGroupKeys(sessionID);
        if (sessionData == null) {
            return Collections.emptyList();
        } else {
            return sessionData;
        }
    }

    public Object loadAttribute(String attrName, String sessionID,
                                StoreContext storeContext) {
        return cache.get(sessionID, attrName);
    }

    public void invaldiate(String sessionID, StoreContext storeContext) {
        cache.cleanGroup(sessionID);
    }

    public void commit(Map<String, Object> modifiedAttrs, String sessionID,
                       StoreContext storeContext) {
        for (Map.Entry<String, Object> entry : modifiedAttrs.entrySet()) {
            String attrName = entry.getKey();
            Object attrValue = entry.getValue();

            if (attrValue == null) {
                cache.remove(sessionID, attrName);
            } else {
                cache.put(sessionID, attrName, attrValue);
            }
        }

    }

}
