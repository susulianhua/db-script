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
package org.tinygroup.lucene472.builder;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.sandbox.queries.regex.RegexQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.tinygroup.beancontainer.BeanContainer;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.fulltext.*;
import org.tinygroup.fulltext.document.Document;
import org.tinygroup.fulltext.exception.FullTextException;
import org.tinygroup.fulltext.field.Field;
import org.tinygroup.fulltext.field.StoreField;
import org.tinygroup.lucene472.config.LuceneConfig;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 基础的lucene对象构建器
 *
 * @author yancheng11334
 */
public abstract class BaseLuceneBuilder {

    protected IndexableFieldBuilder indexableFieldBuilder = new IndexableFieldBuilder();

    protected BeanContainer<?> beanContainer = null;

    public BaseLuceneBuilder() {
        super();
        beanContainer = BeanContainerFactory.getBeanContainer(this.getClass()
                .getClassLoader());
    }

    /**
     * 获得lucene配置
     *
     * @return
     */
    public abstract LuceneConfig buildConfig();

    public String getPerfix() {
        return StringUtil.defaultIfEmpty(buildConfig().getHighLightPrefix(), "@LUCENE_PERFIX");
    }

    public String getSuffix() {
        return StringUtil.defaultIfEmpty(buildConfig().getHighLightSuffix(), "@LUCENE_SUFFIX");
    }

    /**
     * 构建org.apache.lucene.document.Document对象
     *
     * @param doc
     * @return
     */
    @SuppressWarnings("rawtypes")
    public org.apache.lucene.document.Document buildDocument(Document doc) {
        org.apache.lucene.document.Document document = new org.apache.lucene.document.Document();
        Iterator<Field> it = doc.iterator();
        LuceneConfig config = buildConfig();
        boolean tag = config.isFilterHtml();
        while (it.hasNext()) {
            Field field = it.next();
            if (field instanceof StoreField) {
                document.add(indexableFieldBuilder.build((StoreField) field, tag));
            } else {
                throw new FullTextException(String.format("ID为[%s]的文档存在非StoreField的字段,该字段为[%s]", doc.getId().getValue(), field.getName()));
            }

        }
        return document;
    }

    /**
     * 构建org.apache.lucene.store.Directory对象
     *
     * @return
     */
    public org.apache.lucene.store.Directory buildDirectory() {
        LuceneConfig config = buildConfig();
        try {
            return FSDirectory.open(new File(config.getDirectory()));
        } catch (IOException e) {
            throw new FullTextException(String.format(
                    "创建索引目录[%s]的FSDirectory失败:", config.getDirectory()), e);
        }
    }

    /**
     * 构建org.apache.lucene.index.IndexWriterConfig对象
     *
     * @return
     */
    public org.apache.lucene.index.IndexWriterConfig buildIndexWriterConfig() {
        LuceneConfig config = buildConfig();
        Version version = buildVersion(config);
        Analyzer analyzer = buildAnalyzer(config, version);
        return new org.apache.lucene.index.IndexWriterConfig(version, analyzer);
    }

    protected String[] buildDefaultQueryFields() {
        List<String> fields = new ArrayList<String>();
        LuceneConfig config = buildConfig();

        fields.add(FullTextHelper.getStoreId());
        fields.add(FullTextHelper.getStoreType());
        fields.add(FullTextHelper.getStoreTitle());
        fields.add(FullTextHelper.getStoreAbstract());
        if (!StringUtil.isEmpty(config.getSearchFields())) {
            if (config.getSearchFields().indexOf(",") > -1) {
                String[] ss = config.getSearchFields().trim().split(",");
                for (String s : ss) {
                    if (!fields.contains(s)) {
                        fields.add(s);
                    }
                }
            } else {
                String s = config.getSearchFields().trim();
                if (!fields.contains(s)) {
                    fields.add(s);
                }
            }
        }

        return fields.toArray(new String[fields.size()]);
    }

    /**
     * 构建搜索字段域
     *
     * @return
     */
    public abstract String[] buildQueryFields();

    public Set<String> buildQuerySet() {
        Set<String> sets = new HashSet<String>();
        String[] fields = buildQueryFields();
        for (String field : fields) {
            sets.add(field);
        }
        return sets;
    }

    public Query buildQuery(SearchRule searchRule) throws Exception {

        if (searchRule == null || (searchRule.getFieldRuleList().isEmpty() && searchRule.getDefaultRule() == null)) {
            throw new FullTextException("SearchRule对象为空或尚未定义规则");
        }

        LuceneConfig config = buildConfig();
        Version version = buildVersion(config);
        Analyzer analyzer = buildAnalyzer(config, version);
        BooleanQuery query = new BooleanQuery();
        Query q = null;
        //走简单逻辑
        if (searchRule.getFieldRuleList().isEmpty()) {
            String[] fields = buildQueryFields();

            DefaultRule defaultRule = searchRule.getDefaultRule();
            for (String field : fields) {
                switch (defaultRule.getType()) {
                    case DEFAULT: {
                        q = buildDefaultQuery(version, field, defaultRule.getRule(), analyzer);
                        break;
                    }
                    case TERM: {
                        q = buildTermQuery(field, defaultRule.getRule());
                        break;
                    }
                    case WILDCARD: {
                        q = buildWildcardQuery(field, defaultRule.getRule());
                        break;
                    }
                    case REGEX: {
                        q = buildRegexQuery(field, defaultRule.getRule());
                        break;
                    }
                    default: {
                        q = buildDefaultQuery(version, field, defaultRule.getRule(), analyzer);
                    }
                }
                query.add(q, getOccur(defaultRule.getRelation()));
            }
            return query;
        }


        //走自定义规则
        Map<String, String> rules = new HashMap<String, String>();

        for (FieldRule fieldRule : searchRule.getFieldRuleList()) {
            rules.put(fieldRule.getName(), fieldRule.getRule());
            switch (fieldRule.getType()) {
                case DEFAULT: {
                    q = buildDefaultQuery(version, fieldRule.getName(), fieldRule.getRule(), analyzer);
                    break;
                }
                case TERM: {
                    q = buildTermQuery(fieldRule.getName(), fieldRule.getRule());
                    break;
                }
                case WILDCARD: {
                    q = buildWildcardQuery(fieldRule.getName(), fieldRule.getRule());
                    break;
                }
                case REGEX: {
                    q = buildRegexQuery(fieldRule.getName(), fieldRule.getRule());
                    break;
                }
                default: {
                    q = buildDefaultQuery(version, fieldRule.getName(), fieldRule.getRule(), analyzer);
                }
            }
            query.add(q, getOccur(fieldRule.getRelation()));
        }

        //如果用户还配置默认规则，对其他未定义的字段需要应用该规则
        if (searchRule.getDefaultRule() != null) {
            String[] fields = buildQueryFields();
            DefaultRule defaultRule = searchRule.getDefaultRule();
            BooleanQuery otherQuery = new BooleanQuery();
            q = null;
            for (String field : fields) {
                //排除自定义字段
                if (!rules.containsKey(field)) {
                    switch (defaultRule.getType()) {
                        case DEFAULT: {
                            q = buildDefaultQuery(version, field, defaultRule.getRule(), analyzer);
                            break;
                        }
                        case TERM: {
                            q = buildTermQuery(field, defaultRule.getRule());
                            break;
                        }
                        case WILDCARD: {
                            q = buildWildcardQuery(field, defaultRule.getRule());
                            break;
                        }
                        case REGEX: {
                            q = buildRegexQuery(field, defaultRule.getRule());
                            break;
                        }
                        default: {
                            q = buildDefaultQuery(version, field, defaultRule.getRule(), analyzer);
                        }
                    }
                    otherQuery.add(q, getOccur(defaultRule.getRelation()));
                }
            }
            //设置自定义规则与默认规则的查询关系
            if (otherQuery.getClauses() != null && otherQuery.getClauses().length > 0) {
                query.add(otherQuery, Occur.MUST);
            }
        }


        return query;
    }

    private Occur getOccur(QueryRelation relation) {
        switch (relation) {
            case AND: {
                return Occur.MUST;
            }
            case OR: {
                return Occur.SHOULD;
            }
            case NOT: {
                return Occur.MUST_NOT;
            }
        }
        return Occur.MUST;
    }

    private Query buildDefaultQuery(Version version, String fieldName, String rule, Analyzer analyzer) throws Exception {
        QueryParser parser = new QueryParser(version, fieldName, analyzer);
        return parser.parse(rule);
    }

    private Query buildTermQuery(String fieldName, String rule) {
        return new TermQuery(new Term(fieldName, rule));
    }

    private Query buildWildcardQuery(String fieldName, String rule) {
        return new WildcardQuery(new Term(fieldName, rule));
    }

    private Query buildRegexQuery(String fieldName, String rule) {
        return new RegexQuery(new Term(fieldName, rule));
    }

    public Query buildQuery(String searchCondition) throws Exception {
        String condition = QueryParser.escape(searchCondition);
        //判断是否包含特殊关键字
        boolean keywordTag = !condition.equals(searchCondition);

        String[] words = condition.trim().split("\\s+");
        boolean spaceTag = (words != null && words.length > 1) ? true : false;

        LuceneConfig config = buildConfig();
        Version version = buildVersion(config);
        Analyzer analyzer = buildAnalyzer(config, version);
        String[] fields = buildQueryFields();

        if (keywordTag || spaceTag) {
            NewMultiFieldQueryParser newMultiFieldQueryParser = new NewMultiFieldQueryParser(version, fields, analyzer);
            return newMultiFieldQueryParser.parse(searchCondition);
        } else {
            //不包含特殊关键字，走模糊匹配
            BooleanQuery query = new BooleanQuery();
            for (String field : fields) {
                WildcardQuery fieldQuery = new WildcardQuery(new Term(field, "*" + condition + "*"));
                query.add(fieldQuery, Occur.SHOULD);
            }
            return query;
        }

    }

    /**
     * 构建org.apache.lucene.search.highlight.Highlighter对象
     *
     * @return
     */
    public org.apache.lucene.search.highlight.Highlighter buildHighlighter(Query query) {
        String perfix = getPerfix();
        String suffix = getSuffix();
        SimpleHTMLFormatter formatter = new SimpleHTMLFormatter(perfix, suffix);
        Highlighter highlighter = new Highlighter(formatter, new QueryScorer(query));
        return highlighter;
    }


    public Version buildVersion() {
        return buildVersion(buildConfig());
    }

    @SuppressWarnings("deprecation")
    private Version buildVersion(LuceneConfig config) {
        return StringUtil.isEmpty(config.getIndexVersion()) ? Version.LUCENE_CURRENT
                : Version.valueOf(config.getIndexVersion());
    }

    public Analyzer buildAnalyzer() {
        LuceneConfig config = buildConfig();
        Version version = buildVersion(config);
        return buildAnalyzer(config, version);
    }

    private Analyzer buildAnalyzer(LuceneConfig config, Version version) {
        return StringUtil.isEmpty(config.getAnalyzerBeanName()) ? new StandardAnalyzer(version)
                : (Analyzer) beanContainer
                .getBean(config.getAnalyzerBeanName());
    }

}
