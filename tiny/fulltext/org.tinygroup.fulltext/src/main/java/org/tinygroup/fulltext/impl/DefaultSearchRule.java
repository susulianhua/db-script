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

import org.tinygroup.fulltext.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认的搜索定义实现
 *
 * @author yancheng11334
 */
public class DefaultSearchRule implements SearchRule {

    private List<FieldRule> fieldRuleList = new ArrayList<FieldRule>();
    private DefaultRule defaultRule = null;

    public List<FieldRule> getFieldRuleList() {
        return fieldRuleList;
    }

    public DefaultRule getDefaultRule() {
        return defaultRule;
    }

    public DefaultSearchRule addField(FieldRule rule) {
        fieldRuleList.add(rule);
        return this;
    }

    public DefaultSearchRule addField(String name, String rule, QueryRelation relation, QueryType type) {
        return addField(new FieldRule(name, rule, relation, type));
    }

    public DefaultSearchRule addField(String name, String rule, QueryRelation relation) {
        return addField(name, rule, relation, QueryType.DEFAULT);
    }

    public DefaultSearchRule addField(String name, String rule, QueryType type) {
        return addField(name, rule, QueryRelation.AND, type);
    }

    public DefaultSearchRule addField(String name, String rule) {
        return addField(name, rule, QueryRelation.AND, QueryType.DEFAULT);
    }

    public DefaultSearchRule setDefault(DefaultRule rule) {
        defaultRule = rule;
        return this;
    }

    public DefaultSearchRule setDefault(String rule, QueryRelation relation, QueryType type) {
        return setDefault(new DefaultRule(rule, relation, type));
    }

    public DefaultSearchRule setDefault(String rule, QueryRelation relation) {
        return setDefault(rule, relation, QueryType.DEFAULT);
    }

    public DefaultSearchRule setDefault(String rule, QueryType type) {
        return setDefault(rule, QueryRelation.OR, type);
    }

    public DefaultSearchRule setDefault(String rule) {
        return setDefault(rule, QueryRelation.OR, QueryType.DEFAULT);
    }

    public String toString() {
        return "DefaultSearchRule [fieldRuleList=" + fieldRuleList
                + ", defaultRule=" + defaultRule + "]";
    }


}
