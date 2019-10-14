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
package org.tinygroup.container;

import java.util.Comparator;
import java.util.List;

/**
 * 容器<br>
 * 用于放置一些列表数据
 *
 * @param <K>
 * @author luoguo
 */
public interface Container<K extends Comparable<K>, T extends BaseObject<K>>
        extends BaseObject<K> {
    List<T> getList();// 返回原始排序列表

    void setList(List<T> list);// 设置包含的内容

    List<T> getList(Comparator<T> comparator);// 返回经过排序的对象列表

    boolean contains(T object);// 返回是否包含某对象

}
