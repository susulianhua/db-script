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

import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.fulltext.FullTextConfigManager;
import org.tinygroup.lucene472.LuceneConfigManager;
import org.tinygroup.lucene472.config.LuceneConfig;
import org.tinygroup.templateindex.config.BaseIndexConfig;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 静态配置的lucene构建器
 *
 * @author yancheng11334
 */
public class StaticLuceneBuilder extends BaseLuceneBuilder {

    private LuceneConfigManager luceneConfigManager = null;

    public LuceneConfigManager getLuceneConfigManager() {
        if (luceneConfigManager == null) {
            luceneConfigManager = beanContainer.getBean(getBeanName());
        }
        return luceneConfigManager;
    }

    public void setLuceneConfigManager(LuceneConfigManager luceneConfigManager) {
        this.luceneConfigManager = luceneConfigManager;
    }

    private String getBeanName() {
        // 先加载全局配置的配置参数
        String beanName = ConfigurationUtil
                .getConfigurationManager()
                .getConfiguration(FullTextConfigManager.FULLTEXT_CONFIG_MANAGER);
        return beanName == null ? LuceneConfigManager.DEFAULT_BEAN_NAME
                : beanName;
    }

    public LuceneConfig buildConfig() {
        return getLuceneConfigManager().getFullTextConfig();
    }

    public String[] buildQueryFields() {
        Set<String> indexFields = new HashSet<String>();
        List<BaseIndexConfig> indexConfigList = getLuceneConfigManager().getIndexConfigList();
        if (indexConfigList != null) {
            for (BaseIndexConfig indexConfig : indexConfigList) {
                indexFields.addAll(indexConfig.getQueryFields());
            }
        }
        if (!indexFields.isEmpty()) {
            return indexFields.toArray(new String[indexFields.size()]);
        } else {
            return buildDefaultQueryFields();
        }
    }

}
