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

import org.tinygroup.fulltext.FullTextConfigManager;
import org.tinygroup.lucene472.config.LuceneConfig;
import org.tinygroup.lucene472.config.LuceneConfigs;
import org.tinygroup.templateindex.config.BaseIndexConfig;

import java.util.List;

/**
 * Lucene配置管理器
 *
 * @author yancheng11334
 */
public interface LuceneConfigManager extends FullTextConfigManager<LuceneConfig> {

    public static final String DEFAULT_BEAN_NAME = "luceneConfigManager";

    /**
     * 添加配置组
     *
     * @param configs
     */
    public void addLuceneConfigs(LuceneConfigs configs);

    /**
     * 删除配置组
     *
     * @param configs
     */
    public void removeLuceneConfigs(LuceneConfigs configs);

    /**
     * 获得索引配置项列表
     *
     * @return
     */
    public List<BaseIndexConfig> getIndexConfigList();
}
