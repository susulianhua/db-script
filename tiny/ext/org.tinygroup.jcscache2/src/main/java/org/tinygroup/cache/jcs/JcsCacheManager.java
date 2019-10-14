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
package org.tinygroup.cache.jcs;

import org.apache.commons.jcs.engine.control.CompositeCacheManager;
import org.tinygroup.cache.AbstractCacheManager;
import org.tinygroup.cache.Cache;
import org.tinygroup.cache.exception.CacheException;

/**
 * @author renhui
 */
public class JcsCacheManager extends AbstractCacheManager {

    private static JcsCacheManager cacheManager;
    private CompositeCacheManager manager;

    private JcsCacheManager() {
        this(CompositeCacheManager.getInstance());
    }

    private JcsCacheManager(CompositeCacheManager manager) {
        super();
        this.manager = manager;
    }

    public static JcsCacheManager getInstance() {
        if (cacheManager == null) {
            cacheManager = new JcsCacheManager();
        }
        return cacheManager;
    }

    public static JcsCacheManager getInstance(CompositeCacheManager manager) {
        if (cacheManager == null) {
            cacheManager = new JcsCacheManager(manager);
        }
        return cacheManager;
    }

    public void shutDown() {
        manager.shutDown();
    }

    @Override
    protected Cache newCache(String region) {
        try {
            JcsCache jcsCache = new JcsCache();
            jcsCache.init(region);
            return jcsCache;
        } catch (Exception e) {
            throw new CacheException(e);
        }

    }

    @Override
    protected void internalRemoveCache(Cache cache) {
        manager.freeCache(cacheMap.inverse().get(cache));
    }

}
