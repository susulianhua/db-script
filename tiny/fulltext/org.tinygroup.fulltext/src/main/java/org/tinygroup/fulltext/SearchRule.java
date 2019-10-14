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

import java.util.List;

/**
 * 搜索规则定义
 *
 * @author yancheng11334
 */
public interface SearchRule {

    /**
     * 得到自定义的字段搜索规则
     *
     * @return
     */
    List<FieldRule> getFieldRuleList();

    /**
     * 得到默认字段搜索规则
     *
     * @return
     */
    DefaultRule getDefaultRule();

    /**
     * 增加自定义字段规则
     *
     * @param rule
     * @return
     */
    SearchRule addField(FieldRule rule);

    /**
     * 设置默认字段规则
     *
     * @param rule
     * @return
     */
    SearchRule setDefault(DefaultRule rule);
}
