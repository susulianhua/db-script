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
package org.tinygroup.springmvc.util;

import java.util.List;
import java.util.Map;

/**
 * Extension of the {@code Map} interface that stores multiple values.
 *
 * @author Arjen Poutsma
 * @since 3.0
 */
public interface MultiValueMap<K, V> extends Map<K, List<V>> {

    /**
     * Return the first value for the given key.
     *
     * @param key the key
     * @return the first value for the specified key, or <code>null</code>
     */
    V getFirst(K key);

    /**
     * Add the given single value to the current list of values for the given key.
     *
     * @param key   the key
     * @param value the value to be added
     */
    void add(K key, V value);

    /**
     * Set the given single value under the given key.
     *
     * @param key   the key
     * @param value the value to set
     */
    void set(K key, V value);

    /**
     * Set the given values under.
     *
     * @param values the values.
     */
    void setAll(Map<K, V> values);

    /**
     * Returns the first values contained in this {@code MultiValueMap}.
     *
     * @return a single value representation of this map
     */
    Map<K, V> toSingleValueMap();

}
