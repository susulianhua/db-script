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
package org.tinygroup.template.impl;

import org.tinygroup.template.TemplateCache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by luoguo on 2014/6/14.
 */
public class TemplateCacheDefault<K, T> implements TemplateCache<K, T> {
    private Map<K, T> cache = new ConcurrentHashMap<K, T>();


    public T get(K key) {
        return cache.get(key);
    }


    public void put(K key, T object) {
        cache.put(key, object);
    }


    public boolean contains(K key) {
        return cache.containsKey(key);
    }

    public void remove(K key) {
        cache.remove(key);
    }
}
