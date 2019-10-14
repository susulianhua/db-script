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
package org.tinygroup.fulltext.impl;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.fulltext.*;
import org.tinygroup.fulltext.document.Document;
import org.tinygroup.fulltext.exception.FullTextException;
import org.tinygroup.fulltext.field.Field;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractFullText implements FullText {

    /**
     * 批量文档处理器列表
     */
    private List<DocumentListCreator> documentListCreators;

    /**
     * 具体的索引管理器
     */
    private IndexOperator indexOperator;

    public List<DocumentListCreator> getDocumentListCreators() {
        return documentListCreators;
    }

    public void setDocumentListCreators(
            List<DocumentListCreator> documentListCreators) {
        this.documentListCreators = documentListCreators;
    }

    public IndexOperator getIndexOperator() {
        return indexOperator;
    }

    public void setIndexOperator(IndexOperator indexOperator) {
        this.indexOperator = indexOperator;
    }

    /**
     * 检查类型
     *
     * @param type
     */
    protected void checkType(String type) {
        if (StringUtil.isEmpty(type)) {
            throw new FullTextException("类型参数不能为空!");
        }
    }

    /**
     * 检查索引数据
     *
     * @param data
     */
    protected <T> void checkData(T data) {
        if (data == null) {
            throw new FullTextException("索引数据不能为空!");
        }
    }

    protected <T> boolean matchData(DocumentListCreator creator, T data) {
        try {
            return creator.isMatch(data);
        } catch (ClassCastException e) {
            //忽略类型不配
            return false;
        }
    }

    public <T> void createIndex(String type, T data, Object... arguments) {
        checkType(type);
        checkData(data);
        for (DocumentListCreator creator : this.documentListCreators) {
            if (matchData(creator, data)) {
                List<Document> docs = creator.getDocument(type, data, arguments);
                this.indexOperator.createIndex(docs);
                return;
            }
        }
        throw new FullTextException("没有找到合适的索引处理器创建索引!");
    }

    public <T> void deleteIndex(String type, T data, Object... arguments) {
        checkType(type);
        checkData(data);

        for (DocumentListCreator creator : this.documentListCreators) {
            if (matchData(creator, data)) {
                List<Document> docs = creator.getDocument(type, data, arguments);
                List<Field> docIds = new ArrayList<Field>();
                for (Document doc : docs) {
                    docIds.add(doc.getId());
                }
                this.indexOperator.deleteIndex(docIds);
                return;
            }
        }
        throw new FullTextException("没有找到合适的索引处理器删除索引!");
    }

    public void deleteAllIndexes() {
        this.indexOperator.deleteAllIndexes();
    }

    public Pager<Document> search(String searchCondition) {
        return search(searchCondition, 0, 10);
    }

    public Pager<Document> search(String searchCondition, int start, int limit) {
        return search(searchCondition, null, start, limit);
    }

    public Pager<Document> search(String searchCondition, DocumentFilter filter, int start, int limit) {
        return indexOperator.search(searchCondition, filter, start, limit);
    }

    public Pager<Document> search(SearchRule searchRule, int start, int limit) {
        return search(searchRule, null, start, limit);
    }

    public Pager<Document> search(SearchRule searchRule, DocumentFilter filter, int start, int limit) {
        return indexOperator.search(searchRule, filter, start, limit);
    }

    public String escape(String queryRule) {
        //默认不处理
        return queryRule;
    }

}
