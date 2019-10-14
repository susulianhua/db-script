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
package org.tinygroup.cache.redis;

import org.tinygroup.cache.AbstractCacheManager;
import org.tinygroup.cache.Cache;
import org.tinygroup.jedis.JedisManager;

public class RedisCacheManager extends AbstractCacheManager {

    private JedisManager jedisManager;

    public JedisManager getJedisManager() {
        return jedisManager;
    }

    public void setJedisManager(JedisManager jedisManager) {
        this.jedisManager = jedisManager;
    }

    public void shutDown() {
        removeCaches();
        cacheMap.clear();
    }

    protected Cache newCache(String region) {
        RedisCache cache = new RedisCache();
        cache.setCacheManager(this);
        cache.init(region);
        return cache;
    }

    protected void internalRemoveCache(Cache cache) {
        cache.clear();
    }

}
