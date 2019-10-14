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
 * 全文检索的接口
 *
 * @author yancheng11334
 */
public interface FullText {

    /**
     * application.xml的全局配置参数名:<br>
     * 参数值:全文检索接口的bean名称(单例,用于静态配置场景)
     */
    public static final String FULLTEXT_BEAN_NAME = "FULLTEXT_BEAN_NAME";

    /**
     * application.xml的全局配置参数名:<br>
     * 参数值:全文检索接口的bean名称(注意要允许多例,用于动态配置多实例场景)
     */
    public static final String FULLTEXT_DYNAMIC_BEAN = "FULLTEXT_DYNAMIC_BEAN";

    /**
     * 创建索引
     *
     * @param type      索引项:相当于分类，便于查询
     * @param data
     * @param arguments
     */
    public <T> void createIndex(String type, T data, Object... arguments);

    /**
     * 删除索引
     *
     * @param type      索引项:相当于分类，便于查询
     * @param data
     * @param arguments
     */
    public <T> void deleteIndex(String type, T data, Object... arguments);

    /**
     * 删除全部索引
     */
    public void deleteAllIndexes();

    /**
     * 查询索引
     *
     * @param searchCondition
     * @return
     */
    public Pager<Document> search(String searchCondition);

    /**
     * 查询带分页的索引
     *
     * @param searchCondition
     * @param start
     * @param limit
     * @return
     */
    public Pager<Document> search(String searchCondition, int start, int limit);

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
     * 查询自定义条件的带分页的索引
     *
     * @param searchRule
     * @param start
     * @param limit
     * @return
     */
    public Pager<Document> search(SearchRule searchRule, int start, int limit);

    /**
     * 查询并过滤自定义条件的带分页的索引
     * @param searchRule
     * @param filter
     * @param start
     * @param limit
     * @return
     */
    public Pager<Document> search(SearchRule searchRule, DocumentFilter filter, int start, int limit);

    /**
     * 转义带特殊字符的查询条件
     * @param queryRule
     * @return
     */
    public String escape(String queryRule);
}
