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

import org.apache.jcs.JCS;
import org.apache.jcs.engine.control.CompositeCache;
import org.tinygroup.cache.Cache;
import org.tinygroup.cache.CacheManager;
import org.tinygroup.cache.exception.CacheException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JcsCache implements Cache {
    private JCS jcs;
    private CacheManager cacheManager;

    public JcsCache() {
    }

    private void checkSerializable(Object object) {
        if (!(object instanceof Serializable)) {
            throw new RuntimeException("对象必须实现Serializable接口");
        }
    }

    public Object get(String key) {
        return jcs.get(key);
    }

    public void put(String key, Object object) {
        checkSerializable(object);
        try {
            jcs.put(key, object);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    public void putSafe(String key, Object object) {
        checkSerializable(object);
        try {
            jcs.putSafe(key, object);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    public void put(String groupName, String key, Object object) {
        checkSerializable(object);
        try {
            jcs.putInGroup(key, groupName, object);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    public Object get(String groupName, String key) {
        try {
            return jcs.getFromGroup(key, groupName);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public Set<String> getGroupKeys(String group) {
        return jcs.getGroupKeys(group);
    }

    public void cleanGroup(String group) {
        jcs.invalidateGroup(group);
    }

    public void clear() {
        try {
            jcs.clear();
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    public void remove(String key) {
        try {
            jcs.remove(key);
        } catch (Exception e) {
            throw new CacheException(e);
        }

    }

    public void remove(String group, String key) {
        try {
            jcs.remove(key, group);
        } catch (Exception e) {
            throw new CacheException(e);
        }

    }

    public String getStats() {
        return jcs.getStats();
    }

    public int freeMemoryElements(int numberToFree) {
        try {
            return jcs.freeMemoryElements(numberToFree);
        } catch (org.apache.jcs.access.exception.CacheException e) {
            throw new CacheException(e);
        }
    }

    public void init(String region) {
        try {
            jcs = JCS.getInstance(region);
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    public void destroy() {
        CompositeCache.elementEventQ.destroy();
        cacheManager.removeCache(this);
    }

    public void setCacheManager(CacheManager manager) {
        this.cacheManager = manager;
    }

    public Object[] get(String[] keys) {
        List<Object> objs = new ArrayList<Object>();
        if (keys != null && keys.length > 0) {
            for (int i = 0; i < keys.length; i++) {
                objs.add(get(keys[i]));
            }
        }
        return objs.toArray();
    }

    public Object[] get(String group, String[] keys) {
        List<Object> objs = new ArrayList<Object>();
        if (keys != null && keys.length > 0) {
            for (int i = 0; i < keys.length; i++) {
                objs.add(get(group, keys[i]));
            }
        }
        return objs.toArray();
    }

    public void remove(String[] keys) {
        if (keys != null && keys.length > 0) {
            for (int i = 0; i < keys.length; i++) {
                remove(keys[i]);
            }
        }
    }

    public void remove(String group, String[] keys) {
        if (keys != null && keys.length > 0) {
            for (int i = 0; i < keys.length; i++) {
                remove(group, keys[i]);
            }
        }
    }

}
