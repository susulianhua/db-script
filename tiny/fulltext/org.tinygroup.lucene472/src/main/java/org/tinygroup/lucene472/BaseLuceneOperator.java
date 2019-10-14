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
package org.tinygroup.lucene472;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.store.Directory;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.fulltext.DocumentFilter;
import org.tinygroup.fulltext.Pager;
import org.tinygroup.fulltext.SearchRule;
import org.tinygroup.fulltext.document.Document;
import org.tinygroup.fulltext.exception.FullTextException;
import org.tinygroup.fulltext.field.Field;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.lucene472.builder.BaseLuceneBuilder;
import org.tinygroup.lucene472.wrapper.DocumentWrapper;
import org.tinygroup.lucene472.wrapper.FieldWrapper;
import org.tinygroup.lucene472.wrapper.HighlightFieldWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * lucene操作基类
 *
 * @author yancheng11334
 */
public class BaseLuceneOperator {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(BaseLuceneOperator.class);

    protected BaseLuceneBuilder luceneBuilder = null;

    public void createIndex(List<Document> docs) {
        if (CollectionUtil.isEmpty(docs)) {
            LOGGER.logMessage(LogLevel.WARN, "创建索引失败:文档集为空!");
            return;
        }
        IndexWriter indexWriter = null;
        try {
            //获得操作对象
            indexWriter = create(luceneBuilder.buildDirectory(),
                    luceneBuilder.buildIndexWriterConfig());
            boolean allowDuplication = luceneBuilder.buildConfig().isAllowDuplication();
            if (allowDuplication) {
                for (Document doc : docs) {
                    //允许记录重复的话，直接添加即可
                    if (doc != null) {
                        addDocument(indexWriter, doc);
                    }
                }
            } else {
                for (Document doc : docs) {
                    //不允许重复，以主键为key更新
                    if (doc != null) {
                        updateDocument(indexWriter, doc);
                    }

                }
            }
            commit(indexWriter);
        } catch (Exception e) {
            rollback(indexWriter);
            throw new FullTextException("创建索引失败:", e);
        } finally {
            close(indexWriter);
        }

    }

    public void deleteIndex(List<Field> docIds) {
        if (CollectionUtil.isEmpty(docIds)) {
            LOGGER.logMessage(LogLevel.WARN, "删除索引失败:ID集合为空!");
            return;
        }

        IndexWriter indexWriter = null;
        try {
            //获得操作对象
            indexWriter = create(luceneBuilder.buildDirectory(),
                    luceneBuilder.buildIndexWriterConfig());
            for (Field field : docIds) {
                removeDocument(indexWriter, field);
            }
            commit(indexWriter);
        } catch (Exception e) {
            rollback(indexWriter);
            throw new FullTextException("删除索引失败:", e);
        } finally {
            close(indexWriter);
        }
    }

    public void deleteAllIndexes() {
        IndexWriter indexWriter = null;
        try {
            //获得操作对象
            indexWriter = create(luceneBuilder.buildDirectory(),
                    luceneBuilder.buildIndexWriterConfig());
            indexWriter.deleteAll();
            commit(indexWriter);
        } catch (Exception e) {
            rollback(indexWriter);
            throw new FullTextException("删除全部索引失败:", e);
        } finally {
            close(indexWriter);
        }
    }

    public Pager<Document> search(String searchCondition, DocumentFilter filter, int start, int limit) {
        IndexReader indexReader = null;
        try {
            if (limit <= 0) {
                throw new FullTextException("查询索引失败:limit不能小于等于0");
            }
            indexReader = create(luceneBuilder.buildDirectory());
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            Query query = luceneBuilder.buildQuery(searchCondition);

            return search(indexSearcher, query, filter, start, limit);
        } catch (Exception e) {
            throw new FullTextException(String.format("查询索引失败:查询条件[%s]", searchCondition), e);
        } finally {
            close(indexReader);
        }
    }

    public Pager<Document> search(SearchRule searchRule, DocumentFilter filter, int start, int limit) {
        IndexReader indexReader = null;
        try {
            if (limit <= 0) {
                throw new FullTextException("查询索引失败:limit不能小于等于0");
            }
            indexReader = create(luceneBuilder.buildDirectory());
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            Query query = luceneBuilder.buildQuery(searchRule);

            return search(indexSearcher, query, filter, start, limit);
        } catch (Exception e) {
            throw new FullTextException(String.format("查询索引失败:查询条件[%s]", searchRule), e);
        } finally {
            close(indexReader);
        }
    }

    private Pager<Document> search(IndexSearcher indexSearcher, Query query, DocumentFilter filter, int start, int limit) throws Exception {
        TopDocs topDocs = indexSearcher.search(query, getSearchLimit());
        if (topDocs != null && topDocs.totalHits > 0) {
            if (topDocs.totalHits <= start) {
                return new Pager<Document>(topDocs.totalHits, start, limit, new ArrayList<Document>());
            }
            if (filter != null) {
                return wrapperTopDocsWtihFilter(indexSearcher, query, topDocs, filter, start, limit);
            } else {
                return wrapperTopDocs(indexSearcher, query, topDocs, start, limit);
            }
        } else {
            return new Pager<Document>(0, start, limit, new ArrayList<Document>());
        }
    }

    private Pager<Document> wrapperTopDocsWtihFilter(IndexSearcher indexSearcher, Query query, TopDocs topDocs, DocumentFilter filter, int start, int limit) throws Exception {
        List<Document> records = new ArrayList<Document>();

        //构建高亮相关对象
        Set<String> fields = luceneBuilder.buildQuerySet();
        Highlighter highlighter = luceneBuilder.buildHighlighter(query);
        Analyzer analyzer = luceneBuilder.buildAnalyzer();

        //开始遍历(必须全部遍历一遍，保证总数是正确的)
        for (int i = 0; i < topDocs.totalHits; i++) {

            Document document = wrapperDocument(indexSearcher, fields, highlighter, analyzer, topDocs.scoreDocs[i]);
            if (filter.accept(document)) {
                records.add(document);
            }
        }

        int forStart = Math.min(start, records.size() - 1);
        int forEnd = Math.min(start + limit - 1, records.size() - 1);
        //返回分页对象
        return new Pager<Document>(records.size(), start, limit, records.subList(forStart, forEnd));
    }

    private Pager<Document> wrapperTopDocs(IndexSearcher indexSearcher, Query query, TopDocs topDocs, int start, int limit) throws Exception {

        List<Document> records = new ArrayList<Document>();
        int forStart = Math.min(start, topDocs.scoreDocs.length - 1);
        int forEnd = Math.min(start + limit - 1, topDocs.scoreDocs.length - 1);

        //构建高亮相关对象
        Set<String> fields = luceneBuilder.buildQuerySet();
        Highlighter highlighter = luceneBuilder.buildHighlighter(query);
        Analyzer analyzer = luceneBuilder.buildAnalyzer();

        //开始遍历
        for (int i = forStart; i <= forEnd; i++) {

            Document document = wrapperDocument(indexSearcher, fields, highlighter, analyzer, topDocs.scoreDocs[i]);
            //添加结果文档
            records.add(document);
        }

        //返回分页对象
        return new Pager<Document>(topDocs.totalHits, start, limit, records);
    }

    private Document wrapperDocument(IndexSearcher indexSearcher, Set<String> fields, Highlighter highlighter, Analyzer analyzer, ScoreDoc scoreDoc) throws Exception {
        org.apache.lucene.document.Document searchDoc = indexSearcher.doc(scoreDoc.doc);

        List<Field> newFields = new ArrayList<Field>();
        List<IndexableField> indexableFields = searchDoc.getFields();
        for (IndexableField indexableField : indexableFields) {
            String fieldName = indexableField.name();
            String template = null;
            if (fields.contains(fieldName)) {
                String fieldText = searchDoc.get(fieldName);
                if (fieldText != null) {
                    template = highlighter.getBestFragment(analyzer, fieldName, fieldText);
                }
            }

            if (template != null) {
                //高亮模板存在，执行高亮包装
                newFields.add(new HighlightFieldWrapper(indexableField, luceneBuilder.getPerfix(), luceneBuilder.getSuffix(), template));
            } else {
                //不存在，按普通字段包装
                newFields.add(new FieldWrapper(indexableField));
            }
        }
        return new DocumentWrapper(newFields);
    }

    private void removeDocument(IndexWriter indexWriter, Field field)
            throws IOException, Exception {
        if (field == null || field.getName() == null || field.getValue() == null) {
            throw new Exception(String.format("删除文档操作失败:文档主键[%s]不合法", field));
        }
        LOGGER.logMessage(LogLevel.DEBUG, "IndexWriter对象开始删除主键名为{0},主键值为{1}的文档...",
                field.getName(), field.getValue());
        indexWriter.deleteDocuments(new Term(field.getName(), field.getValue()
                .toString()));
        LOGGER.logMessage(LogLevel.DEBUG, "IndexWriter对象删除主键名为{0},主键值为{1}的文档成功!", field.getName(), field.getValue());
    }

    /**
     * 获得查询的记录数的限制
     *
     * @return
     */
    protected int getSearchLimit() {
        int num = luceneBuilder.buildConfig().getSearchMaxLimit();
        return num > 0 ? num : 10000;
    }

    /**
     * 添加文档的操作
     *
     * @param indexWriter
     * @param doc
     * @throws IOException
     */
    private void addDocument(IndexWriter indexWriter, Document doc)
            throws IOException, Exception {
        Field pk = doc.getId();
        if (pk == null || pk.getName() == null || pk.getValue() == null) {
            throw new Exception(String.format("添加文档操作失败:文档主键[%s]不合法", pk));
        }
        LOGGER.logMessage(LogLevel.DEBUG, "IndexWriter对象开始添加主键名为{0},主键值为{1}的文档...",
                pk.getName(), pk.getValue());
        indexWriter.addDocument(luceneBuilder.buildDocument(doc));
        LOGGER.logMessage(LogLevel.DEBUG, "IndexWriter对象添加主键名为{0},主键值为{1}的文档成功!",
                pk.getName(), pk.getValue());
    }

    /**
     * 更新文档的操作
     *
     * @param indexWriter
     * @param doc
     * @throws IOException
     */
    private void updateDocument(IndexWriter indexWriter, Document doc)
            throws IOException, Exception {
        Field pk = doc.getId();
        if (pk == null || pk.getName() == null || pk.getValue() == null) {
            throw new Exception(String.format("更新文档操作失败:文档主键[%s]不合法", pk));
        }
        LOGGER.logMessage(LogLevel.DEBUG, "IndexWriter对象开始更新主键名为{0},主键值为{1}的文档...",
                pk.getName(), pk.getValue());
        indexWriter.updateDocument(new Term(pk.getName(), pk.getValue().toString()), luceneBuilder.buildDocument(doc));
        LOGGER.logMessage(LogLevel.DEBUG, "IndexWriter对象更新主键名为{0},主键值为{1}的文档成功!",
                pk.getName(), pk.getValue());
    }

    /**
     * 创建操作
     *
     * @param directory
     * @param indexWriterConfig
     * @return
     * @throws IOException
     */
    private IndexWriter create(Directory directory,
                               IndexWriterConfig indexWriterConfig) throws IOException {
        LOGGER.logMessage(LogLevel.DEBUG, "开始创建IndexWriter对象...");
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        LOGGER.logMessage(LogLevel.DEBUG, "创建IndexWriter对象成功!");
        return indexWriter;
    }

    /**
     * 回滚操作
     *
     * @param indexWriter
     */
    private void rollback(IndexWriter indexWriter) {
        // 回滚操作发生在异常处理，因此日志级别比较高
        try {
            LOGGER.logMessage(LogLevel.WARN, "IndexWriter执行回滚操作...");
            if (indexWriter != null) {
                indexWriter.rollback();
            }
            LOGGER.logMessage(LogLevel.WARN, "IndexWriter执行回滚成功!");
        } catch (IOException e) {
            LOGGER.logMessage(LogLevel.WARN, "IndexWriter执行回滚失败!", e);
        }

    }

    /**
     * 提交操作
     *
     * @param indexWriter
     * @throws IOException
     */
    private void commit(IndexWriter indexWriter) throws IOException {
        LOGGER.logMessage(LogLevel.DEBUG, "IndexWriter执行提交操作...");
        indexWriter.commit();
        LOGGER.logMessage(LogLevel.DEBUG, "IndexWriter执行提交成功!");
    }

    /**
     * 关闭操作
     *
     * @param indexWriter
     */
    private void close(IndexWriter indexWriter) {
        LOGGER.logMessage(LogLevel.DEBUG, "开始关闭IndexWriter...");
        if (indexWriter != null) {
            try {
                indexWriter.close();
            } catch (IOException e) {
                throw new FullTextException("关闭IndexWriter失败:", e);
            }
        }
        LOGGER.logMessage(LogLevel.DEBUG, "关闭IndexWriter成功!");
    }

    /**
     * 创建操作
     *
     * @param directory
     * @return
     * @throws IOException
     */
    private IndexReader create(Directory directory) throws IOException {
        LOGGER.logMessage(LogLevel.DEBUG, "开始创建IndexReader对象...");
        IndexReader indexReader = DirectoryReader.open(directory);
        LOGGER.logMessage(LogLevel.DEBUG, "创建IndexReader对象成功!");
        return indexReader;
    }


    /**
     * 关闭操作
     *
     * @param indexReader
     */
    private void close(IndexReader indexReader) {
        LOGGER.logMessage(LogLevel.DEBUG, "开始关闭IndexReader...");
        if (indexReader != null) {
            try {
                indexReader.close();
            } catch (IOException e) {
                throw new FullTextException("关闭IndexReader失败:", e);
            }
        }
        LOGGER.logMessage(LogLevel.DEBUG, "关闭IndexReader成功!");
    }


}
