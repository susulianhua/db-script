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
package org.tinygroup.tinydb.operator;

import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.exception.TinyDbException;

import java.util.List;

/**
 * DB相关的操作
 *
 * @author luoguo
 */
public interface DbSingleOperator<K> {


    Bean insert(Bean bean) throws TinyDbException;

    int update(Bean bean) throws TinyDbException;

    /**
     * @param bean             bean对象
     * @param conditionColumns 条件字段- 数据库字段大写名称的列表集合
     * @return
     */
    int update(Bean bean, List<String> conditionColumns) throws TinyDbException;

    int delete(Bean bean) throws TinyDbException;

    Bean getBean(K beanId, String beanType) throws TinyDbException;

    int deleteById(K beanId, String beanType) throws TinyDbException;

}
