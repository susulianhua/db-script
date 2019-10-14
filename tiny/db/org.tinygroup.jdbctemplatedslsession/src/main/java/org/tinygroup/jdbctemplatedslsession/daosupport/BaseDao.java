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
package org.tinygroup.jdbctemplatedslsession.daosupport;

import org.tinygroup.tinysqldsl.Pager;

import java.io.Serializable;
import java.util.List;

/**
 * 基础dao类
 *
 * @param <T>
 * @author renhui
 */
public interface BaseDao<T, KeyType extends Serializable> {

    T add(T t);

    int edit(T t);

    int deleteByKey(KeyType pk);

    T getByKey(KeyType pk);

    int deleteByKeys(KeyType... pks);

    List<T> query(T t, OrderBy... orderArgs);

    Pager<T> queryPager(int start, int limit, T t, OrderBy... orderArgs);

    /**
     * 批量插入(轮询)
     *
     * @param objects
     * @return
     */
    int[] batchInsert(List<T> objects);

    /**
     * 批量更新(轮询)
     *
     * @param objects
     * @return
     */
    int[] batchUpdate(List<T> objects);

    /**
     * 批量删除(轮询)
     *
     * @param objects
     * @return
     */
    int[] batchDelete(List<T> objects);

    /**
     * 批量插入(预编译方式,sql固定)
     *
     * @param objects
     * @return
     */
    int[] preparedBatchInsert(List<T> objects);

    /**
     * 批量更新(预编译方式,sql固定)
     *
     * @param objects
     * @return
     */
    int[] preparedBatchUpdate(List<T> objects);

    /**
     * 批量删除(预编译方式,sql固定)
     *
     * @param objects
     * @return
     */
    int[] preparedBatchDelete(List<T> objects);

}
