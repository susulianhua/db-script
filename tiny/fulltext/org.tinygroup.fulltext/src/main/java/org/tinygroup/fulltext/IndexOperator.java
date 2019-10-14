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
import org.tinygroup.fulltext.field.Field;

import java.util.List;

/**
 * 底层操作接口(屏蔽上层的数据来源)
 *
 * @author yancheng11334
 */
public interface IndexOperator {

    /**
     * 创建索引接口
     *
     * @param docs
     */
    void createIndex(List<Document> docs);

    /**
     * 删除索引接口
     *
     * @param docIds
     */
    @SuppressWarnings("rawtypes")
    void deleteIndex(List<Field> docIds);

    /**
     * 删除全部索引
     */
    void deleteAllIndexes();

    /**
     * 查询并过滤带分页的索引
     * @param searchCondition
     * @param filter
     * @param start
     * @param limit
     * @return
     */
    public Pager<Document> search(String searchCondition, DocumentFilter filter, int start, int limit);

    /**
     * 查询并过滤自定义分页的索引
     * @param searchRule
     * @param filter
     * @param start
     * @param limit
     * @return
     */
    public Pager<Document> search(SearchRule searchRule, DocumentFilter filter, int start, int limit);
}
