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
package org.tinygroup.cache;

import java.util.Set;

/**
 * 功能说明: 缓存通用接口
 * <p>
 * <p>
 * 开发人员: renhui <br>
 * 开发时间: 2013-5-22 <br>
 * <br>
 */
public interface Cache {
    /**
     * 缓存区域初始化
     * <p>
     * 参考{@link
     * org.tinygroup.cache.CacheManager.createCache(region)}
     *
     * @param region 缓存区域
     */
    void init(String region);

    /**
     * 获取key对应的缓存值
     *
     * @param key 缓存key
     * @return 返回key对应的缓存值
     */
    Object get(String key);

    /**
     * 从指定组中的缓存中获取key对应的缓存值
     *
     * @param groupName 缓存分组
     * @param key       缓存名称
     * @return 缓存值
     */
    Object get(String group, String key);

    /**
     * 分别获取key数组中对应的缓存值
     *
     * @param keys 缓存名称数组
     * @return 返回keys对应的缓存值数组
     */
    Object[] get(String[] keys);

    /**
     * 从指定组中的缓存中分别获取keys对应的缓存值，以值数组形式返回
     *
     * @param groupName 缓存分组名
     * @param keys      缓存名称数组
     * @return 返回缓存值数组
     */
    Object[] get(String group, String[] keys);


    /**
     * 缓存内容存储接口
     *
     * @param key    缓存名称
     * @param Object 缓存值
     */
    void put(String key, Object object);

    /**
     * 缓存内容存储接口,与put不同的是，此方法会先根据key判断缓存是否已经存在
     *
     * @param key
     * @param Object
     */
    void putSafe(String key, Object object);

    /**
     * 缓存内容存储接口
     *
     * @param groupName 缓存分组
     * @param key       缓存名称
     * @param object    缓存值
     */
    void put(String groupName, String key, Object object);

    /**
     * 获取指定组下所有缓存key列表
     *
     * @param group 缓存分组
     * @return 返回缓存key列表
     */
    Set<String> getGroupKeys(String group);

    /**
     * 清除指定组下的所有缓存
     *
     * @param group 缓存分组
     */
    void cleanGroup(String group);

    /**
     * 清除缓存实例下所有缓存内容
     */
    void clear();

    /**
     * 移除key对应的缓存内容
     *
     * @param key 缓存名称
     */
    void remove(String key);

    /**
     * 移除指定组下缓存名称为key的缓存
     *
     * @param group 缓存分组
     * @param key   缓存名称
     */
    void remove(String group, String key);

    /**
     * 移除缓存key数组对应的缓存内容
     *
     * @param keys 缓存key数组
     */
    void remove(String[] keys);

    /**
     * 移除指定组下缓存key数组对应的缓存内容
     *
     * @param group 缓存分组
     * @param keys  缓存key数组
     */
    void remove(String group, String[] keys);

    /**
     * 返回次数缓存的状态快照
     *
     * @return 次数缓存状态组装成的字符串信息
     */
    String getStats();

    /**
     * 释放指定数目的缓存内容，各个第三方缓存实现机制可能不一样
     *
     * @param numberToFree 要释放的缓存数目
     * @return 真正被释放的缓存数目
     */
    int freeMemoryElements(int numberToFree);

    /**
     * 销毁缓存实例相关的资源
     */
    void destroy();

    /**
     * 设置此缓存关联的缓存管理器
     *
     * @param manager 缓存管理器
     */
    void setCacheManager(CacheManager manager);
}
