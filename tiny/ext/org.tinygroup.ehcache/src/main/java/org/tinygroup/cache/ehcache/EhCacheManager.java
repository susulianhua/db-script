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
package org.tinygroup.cache.ehcache;

import net.sf.ehcache.CacheManager;
import org.tinygroup.cache.AbstractCacheManager;
import org.tinygroup.cache.Cache;

/**
 * @author renhui
 */
public class EhCacheManager extends AbstractCacheManager {

    private static EhCacheManager cacheManager;
    private net.sf.ehcache.CacheManager manager;

    private EhCacheManager() {
        this(CacheManager.getInstance());
    }

    private EhCacheManager(CacheManager manager) {
        super();
        this.manager = manager;
    }

    public static EhCacheManager getInstance() {
        if (cacheManager == null) {
            cacheManager = new EhCacheManager();
        }
        return cacheManager;
    }

    public static EhCacheManager getInstance(CacheManager manager) {
        if (cacheManager == null) {
            cacheManager = new EhCacheManager(manager);
        }
        return cacheManager;
    }

    public void shutDown() {
        manager.shutdown();
    }

    @Override
    protected Cache newCache(String region) {
        EhCache cache = new EhCache();
        cache.init(region);
        return cache;
    }

    @Override
    protected void internalRemoveCache(Cache cache) {
        manager.removeCache(cacheMap.inverse().get(cache));
    }

}
