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
package org.tinygroup.fulltext.document;

import org.tinygroup.fulltext.field.Field;

import java.util.List;


/**
 * 文档接口
 *
 * @author yancheng11334
 */
@SuppressWarnings("rawtypes")
public interface Document extends Iterable<Field> {

    /**
     * 获得主键字段
     *
     * @return
     */
    public Field getId();

    /**
     * 获得分类字段
     *
     * @return
     */
    public Field getType();

    /**
     * 获得标题字段
     *
     * @return
     */
    public Field getTitle();

    /**
     * 获得摘要字段
     *
     * @return
     */
    public Field getAbstract();

    /**
     * 获得匹配的字段
     *
     * @param name
     * @return
     */
    public Field getField(String name);

    /**
     * 获得一组匹配的字段组
     *
     * @param name
     * @return
     */
    public List<Field> getFields(String name);
}
