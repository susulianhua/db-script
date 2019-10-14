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
package org.tinygroup.cachefactory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.tinygroup.cache.Cache;
import org.tinygroup.cache.CacheManager;

public class CacheFactoryBean implements FactoryBean, InitializingBean {

    private String cacheRegion;
    private Cache cache;
    private CacheManager cacheManager;

    public Object getObject() throws Exception {
        return cache;
    }

    public Class getObjectType() {
        return Cache.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {
        if (cache == null) {
            cache = cacheManager.createCache(cacheRegion);
        }
    }

    public String getCacheRegion() {
        return cacheRegion;
    }

    public void setCacheRegion(String cacheRegion) {
        this.cacheRegion = cacheRegion;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

}
