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
/**
 *
 */
package org.tinygroup.multilevelcache;

import org.tinygroup.cache.Cache;
import org.tinygroup.cache.CacheManager;

import java.util.*;

/**
 * @author Administrator
 */
public class MapCache implements Cache {

    Map<String, Object> cache;
    Map<String, Set<String>> groupMap;

    public void init(String region) {
        cache = new HashMap<String, Object>();
        groupMap = new HashMap<String, Set<String>>();
    }

    public Object get(String key) {
        return cache.get(key);
    }

    public Object get(String group, String key) {
        return get(key);
    }

    public Object[] get(String[] keys) {
        List<Object> objs = new ArrayList<Object>();
        for (String key : keys) {
            objs.add(cache.get(key));
        }
        return objs.toArray();
    }

    public Object[] get(String group, String[] keys) {
        //由于是测试用例，简单写了
        return get(keys);
    }

    public void put(String key, Object object) {
        cache.put(key, object);
    }

    public void putSafe(String key, Object object) {
        put(key, object);
    }

    public void put(String groupName, String key, Object object) {

        if (groupMap.get(groupName) == null) {
            groupMap.put(groupName, new HashSet<String>());
        }
        groupMap.get(groupName).add(key);
        put(key, object);
    }

    public Set<String> getGroupKeys(String group) {
        return groupMap.get(group);
    }

    public void cleanGroup(String group) {
        groupMap.remove(group);
    }

    public void clear() {
        cache.clear();
    }

    public void remove(String key) {
        cache.remove(key);
    }

    public void remove(String group, String key) {
        remove(key);
    }

    public void remove(String[] keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    public void remove(String group, String[] keys) {
        remove(keys);
    }

    public String getStats() {
        return "OK";
    }

    public int freeMemoryElements(int numberToFree) {
        return 0;
    }

    public void destroy() {

    }

    public void setCacheManager(CacheManager manager) {

    }

}
