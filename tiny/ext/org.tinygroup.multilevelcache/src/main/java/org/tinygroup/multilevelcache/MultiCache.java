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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author yanwj
 */
public class MultiCache implements Cache {

    /**
     * 1级缓存
     */
    Cache cacheLevel1;

    /**
     * 2级缓存
     */
    Cache cacheLevel2;

    CacheManager manager;

    public MultiCache() {
    }

    public MultiCache(Cache cacheLevel1, Cache cacheLevel2) {
        super();
        this.cacheLevel1 = cacheLevel1;
        this.cacheLevel2 = cacheLevel2;
    }

    /**
     * 1，2级缓存，分别自己初始化，防止缓存重复
     */
    public void init(String region) {

    }

    public Object get(String key) {
        Object obj = cacheLevel1.get(key);
        if (obj == null) {
            obj = cacheLevel2.get(key);
            cacheLevel1.put(key, obj);
        }
        return obj;
    }

    public Object get(String group, String key) {
        Object obj = cacheLevel1.get(group, key);
        if (obj == null) {
            obj = cacheLevel2.get(group, key);
            cacheLevel1.put(group, key, obj);
        }
        return obj;
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

    public void put(String key, Object object) {
        cacheLevel1.put(key, object);
        cacheLevel2.put(key, object);
    }

    public void putSafe(String key, Object object) {
        cacheLevel1.putSafe(key, object);
        cacheLevel2.putSafe(key, object);
    }

    public void put(String groupName, String key, Object object) {
        cacheLevel1.put(groupName, key, object);
        cacheLevel2.put(groupName, key, object);
    }

    public Set<String> getGroupKeys(String group) {
        Set<String> firstKeys = cacheLevel1.getGroupKeys(group);
        if (firstKeys != null && firstKeys.size() > 0) {
            return firstKeys;
        }
        Set<String> secondKeys = cacheLevel2.getGroupKeys(group);
        if (secondKeys != null && secondKeys.size() > 0) {
            return secondKeys;
        }
        return new HashSet<String>();
    }

    public void cleanGroup(String group) {
        cacheLevel1.cleanGroup(group);
        cacheLevel2.cleanGroup(group);
    }

    public void clear() {
        cacheLevel1.clear();
        cacheLevel2.clear();
    }

    public void remove(String key) {
        cacheLevel1.remove(key);
        cacheLevel2.remove(key);
    }

    public void remove(String group, String key) {
        cacheLevel1.remove(group, key);
        cacheLevel2.remove(group, key);

    }

    public void remove(String[] keys) {
        cacheLevel1.remove(keys);
        cacheLevel2.remove(keys);
    }

    public void remove(String group, String[] keys) {
        cacheLevel1.remove(group, keys);
        cacheLevel2.remove(group, keys);
    }

    public String getStats() {
        StringBuffer sb = new StringBuffer();
        sb.append("level1 cache:" + cacheLevel1.getStats() + "\r\n");
        sb.append("level2 cache:" + cacheLevel2.getStats() + "\r\n");
        return sb.toString();
    }

    /**
     * 同时释放1，2级缓存内存空间
     * 只返回两者释放最大值
     */
    public int freeMemoryElements(int numberToFree) {
        int firstNum = 0;
        int secondNum = 0;
        try {
            firstNum = cacheLevel1.freeMemoryElements(numberToFree);
        } catch (Exception e) {
            //异常内存，异常不处理
        }
        try {
            secondNum = cacheLevel2.freeMemoryElements(numberToFree);
        } catch (Exception e) {
            //
        }

        return firstNum > secondNum ? firstNum : secondNum;
    }

    /**
     * 外部处理，不在这里处理，原因和init方法同理，本类只使用缓存，不涉及初始化和销毁
     */
    public void destroy() {

    }

    public void setCacheManager(CacheManager manager) {
        this.manager = manager;
    }

}
