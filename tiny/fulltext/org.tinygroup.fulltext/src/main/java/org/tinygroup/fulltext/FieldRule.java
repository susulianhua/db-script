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
 * 指定字段的自定义搜索规则
 *
 * @author yancheng11334
 */
public class FieldRule extends DefaultRule {

    private String name;

    public FieldRule() {
        super();
    }

    public FieldRule(String name, String rule, QueryRelation relation, QueryType type) {
        super();
        this.name = name;
        setRule(rule);
        setRelation(relation);
        setType(type);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "FieldRule [name=" + name + ", rule=" + getRule()
                + ", relation=" + getRelation() + ", type="
                + getType() + "]";
    }

}
