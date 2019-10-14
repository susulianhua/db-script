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
package org.tinygroup.lucene472.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.tinygroup.templateindex.config.BaseIndexConfig;

import java.util.List;

/**
 * 全文检索配置集合
 *
 * @author yancheng11334
 */
@XStreamAlias("lucene-configs")
public class LuceneConfigs {

    @XStreamImplicit
    private List<LuceneConfig> luceneConfigList;

    @XStreamImplicit
    private List<BaseIndexConfig> indexConfigList;

    public List<LuceneConfig> getLuceneConfigList() {
        return luceneConfigList;
    }

    public void setLuceneConfigList(List<LuceneConfig> luceneConfigList) {
        this.luceneConfigList = luceneConfigList;
    }

    public List<BaseIndexConfig> getIndexConfigList() {
        return indexConfigList;
    }

    public void setIndexConfigList(List<BaseIndexConfig> indexConfigList) {
        this.indexConfigList = indexConfigList;
    }

}
