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
package org.tinygroup.fulltext;

import org.tinygroup.fulltext.document.Document;


/**
 * 文档生成器，支持转换单节点数据,如txt文件、数据库的单条Result
 *
 * @author yancheng11334
 */
public interface DocumentCreator<T> {

    /**
     * 判断本文档生成器能否处理此数据
     *
     * @param data
     * @return
     */
    boolean isMatch(T data);

    /**
     * 将数据转换成索引文档
     *
     * @param type
     * @param data
     * @param arguments
     * @return
     */
    Document getDocument(String type, T data, Object... arguments);
}
