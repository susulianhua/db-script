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

/**
 * 默认搜索规则
 *
 * @author yancheng11334
 */
public class DefaultRule {

    private String rule;

    private QueryRelation relation;

    private QueryType type;

    public DefaultRule() {
        super();
    }

    public DefaultRule(String rule, QueryRelation relation, QueryType type) {
        super();
        this.rule = rule;
        this.relation = relation;
        this.type = type;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public QueryRelation getRelation() {
        return relation;
    }

    public void setRelation(QueryRelation relation) {
        this.relation = relation;
    }

    public QueryType getType() {
        return type;
    }

    public void setType(QueryType type) {
        this.type = type;
    }

    public String toString() {
        return "DefaultRule [rule=" + rule + ", relation=" + relation
                + ", type=" + type + "]";
    }


}
