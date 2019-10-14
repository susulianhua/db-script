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
package org.tinygroup.dict;

import org.tinygroup.cache.Cache;
import org.tinygroup.context.Context;

/**
 * 字符管理器
 *
 * @author luoguo
 */
public interface DictManager {
    String DICT_MANAGER_BEAN_NAME = "dictManager";
    String XSTEAM_PACKAGE_NAME = "dict";

    /**
     * 获得缓冲对象
     *
     * @return
     */
    Cache getCache();

    /**
     * 设置字典项保存的缓冲
     *
     * @param cache
     */
    void setCache(Cache cache);

    /**
     * 添加字典加载器
     *
     * @param dictLoader 字典加载器
     */
    void addDictLoader(DictLoader dictLoader);

    /**
     * 利用指定加载器载入字典
     *
     * @param dictLoader 字典加载器
     */
    void load(DictLoader dictLoader);

    /**
     * 清空所有字典项
     */
    void clear();

    /**
     * 清空某一加载器的字符项
     *
     * @param dictLoader 字典加载器
     */
    void clear(DictLoader dictLoader);

    /**
     * 载入字典项
     */
    void load();

    /**
     * 获取默认语言下字典类型名对应的字典对象
     *
     * @param dictTypeName 字段类型名
     * @param context      上下文参数
     * @return 字段对象
     */
    Dict getDict(String dictTypeName, Context context);

    /**
     * 获取指定语言下字典类型名称对应的字典对象
     *
     * @param dictTypeName 字段类型名
     * @param context      上下文参数
     * @return
     */
    Dict getDict(String lang, String dictTypeName, Context context);

}
