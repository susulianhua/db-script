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
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * lucene查询条件解析器，解决MultiFieldQueryParser无法区分字段条件与公用条件。
 *
 * @author yancheng11334
 */
public class NewMultiFieldQueryParser {

    private Version version;
    private Analyzer analyzer;
    private String[] fields;

    public NewMultiFieldQueryParser(Version version, String[] fields, Analyzer analyzer) {
        this.version = version;
        this.analyzer = analyzer;
        this.fields = fields;
    }


    private Query parseField(ParseResult result) throws ParseException {
        String[] fields = new String[result.fieldQueryMaps.size()];
        result.fieldQueryMaps.keySet().toArray(fields);
        String[] queries = new String[result.fieldQueryMaps.size()];
        result.fieldQueryMaps.values().toArray(queries);
        BooleanClause.Occur[] flags = new BooleanClause.Occur[result.fieldQueryMaps.size()];
        Arrays.fill(flags, BooleanClause.Occur.MUST);
        return MultiFieldQueryParser.parse(version, queries, fields, flags, analyzer);
    }

    private Query parsePublic(ParseResult result) throws ParseException {
        String[] queries = new String[fields.length];
        Arrays.fill(queries, result.publicQuery);
        return MultiFieldQueryParser.parse(version, queries, fields, analyzer);
    }

    private Query parseBoth(ParseResult result) throws ParseException {
        BooleanQuery bQuery = new BooleanQuery();
        BooleanQuery shouldQuery = new BooleanQuery();
        for (int i = 0; i < fields.length; i++) {
            QueryParser qp = new QueryParser(version, fields[i], analyzer);
            if (result.fieldQueryMaps.containsKey(fields[i])) {
                //处理字段查询
                Query q = qp.parse(result.fieldQueryMaps.get(fields[i]));
                if (q != null && // q never null, just being defensive
                        (!(q instanceof BooleanQuery) || ((BooleanQuery) q).getClauses().length > 0)) {
                    bQuery.add(q, BooleanClause.Occur.MUST);
                }
            } else {
                //处理公共查询
                Query q = qp.parse(result.publicQuery);
                if (q != null && // q never null, just being defensive
                        (!(q instanceof BooleanQuery) || ((BooleanQuery) q).getClauses().length > 0)) {
                    shouldQuery.add(q, BooleanClause.Occur.SHOULD);
                }
            }
        }
        bQuery.add(shouldQuery, BooleanClause.Occur.MUST);
        return bQuery;
    }

    public Query parse(String query) throws ParseException {

        //执行解析条件
        MultiFieldParser multiFieldParser = new MultiFieldParser(query);
        ParseResult result = multiFieldParser.parser();

        //初始化查询结构
        if (result.publicQuery == null || "".equals(result.publicQuery)) {
            //只有字段查询
            return parseField(result);
        } else if (result.fieldQueryMaps.isEmpty()) {
            //只有公共查询
            return parsePublic(result);
        } else {
            //同时包含公共查询和字段查询
            return parseBoth(result);
        }

    }


    class MultiFieldParser {
        String context;
        int p = -1;
        boolean inQuota = false;
        String fieldName = null;
        StringBuffer sb = new StringBuffer();

        public MultiFieldParser(String query) {
            this.context = query;
        }

        public ParseResult parser() {
            ParseResult result = new ParseResult();
            int parenNum = 0;

            while (p < context.length() - 1) {
                p++;
                String s = context.substring(p, p + 1);
                if (":".equals(s)) {
                    String field = findField();
                    if (field != null) {
                        fieldName = field;
                    }
                    sb.append(s);
                } else if ("(".equals(s)) {
                    parenNum++;
                    sb.append(s);
                } else if (")".equals(s)) {
                    parenNum--;
                    sb.append(s);
                } else if ("\"".equals(s)) {
                    inQuota = !inQuota;
                    sb.append(s);
                } else if (" ".equals(s) || "\t".equals(s) || "\r".equals(s) || "\n".equals(s)) {
                    if (!inQuota && parenNum == 0) {
                        if (fieldName != null) {
                            String v = sb.toString();
                            v = v.substring(v.indexOf(":") + 1);
                            result.fieldQueryMaps.put(fieldName, v);
                            fieldName = null;
                            sb = new StringBuffer();
                        } else {
                            sb.append(s);
                        }
                    } else {
                        sb.append(s);
                    }
                } else {
                    sb.append(s);
                    if (!inQuota && parenNum == 0) {
                        String v = sb.toString().trim().toLowerCase();
                        if (v.startsWith("and") || v.startsWith("or")) {
                            sb = new StringBuffer();
                        }
                    }
                }
            }

            if (sb.length() > 0) {
                if (fieldName != null) {
                    String v = sb.toString();
                    v = v.substring(v.indexOf(":") + 1);
                    result.fieldQueryMaps.put(fieldName, v);
                    fieldName = null;
                } else {
                    result.publicQuery += sb.toString();
                }
            }
            result.escape();
            return result;
        }

        private String findField() {
            String f = sb.toString();
            for (String filed : fields) {
                if (f.equals(filed)) {
                    return f;
                }
            }
            return null;
        }
    }


    class ParseResult {
        Map<String, String> fieldQueryMaps = new HashMap<String, String>();
        String publicQuery = "";

        public void escape() {
            for (String key : fieldQueryMaps.keySet()) {
                String value = QueryParser.escape(fieldQueryMaps.get(key));
                fieldQueryMaps.put(key, value);
            }
            publicQuery = QueryParser.escape(publicQuery);
        }
    }

}
