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

import org.tinygroup.context.Context;

/**
 * 字典加载器
 *
 * @author luoguo
 */
public interface DictLoader {
    /**
     * 返回字段加载器关联的语言信息
     *
     * @return
     */
    String getLanguage();

    /**
     * 设置语言
     *
     * @param language
     */
    void setLanguage(String language);

    /**
     * 载入字典内容到字典管理器中
     *
     * @param dictManager 字典管理器
     */
    void load(DictManager dictManager);

    /**
     * 从字典管理器中清除该字典加载器相关的字典内容
     *
     * @param dictManager
     */
    void clear(DictManager dictManager);

    /**
     * 返回字典对象，参考dictManager。getDict(String lang,String dictTypeName, Context context);
     *
     * @param dictTypeName 字典类型名称
     * @param dictManager  字典管理器
     * @param context      上下文对象
     * @return
     */
    Dict getDict(String dictTypeName, DictManager dictManager, Context context);

    /**
     * 返回分组名称
     *
     * @return
     */
    String getGroupName();

    /**
     * 设置分组名称
     *
     * @param groupName
     */
    void setGroupName(String groupName);

    /**
     * 设置字典过滤器
     *
     * @param dictFilter 字典过滤器
     */
    void setDictFilter(DictFilter dictFilter);
}
