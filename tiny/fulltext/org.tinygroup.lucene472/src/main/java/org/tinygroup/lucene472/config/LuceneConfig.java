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
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 单条检索配置
 *
 * @author yancheng11334
 */
@XStreamAlias("lucene-config")
public class LuceneConfig implements Cloneable {

    //主键
    @XStreamAsAttribute
    private String id;

    //中文名称
    @XStreamAsAttribute
    private String title;

    //索引目录
    private String directory;

    //索引版本
    @XStreamAlias("index-version")
    @XStreamAsAttribute
    private String indexVersion;

    //是否允许重复记录
    @XStreamAlias("allow-duplication")
    @XStreamAsAttribute
    private boolean allowDuplication;

    //查询索引数目最大上限
    @XStreamAlias("search-max-limit")
    @XStreamAsAttribute
    private int searchMaxLimit;

    //查询的字段域，如果有多个采用英文逗号分隔
    @XStreamAlias("search-fields")
    private String searchFields;

    //高亮前缀
    @XStreamAlias("highlight-perfix")
    private String highLightPrefix;

    //高亮后缀
    @XStreamAlias("highlight-suffix")
    private String highLightSuffix;

    //是否启用HTML标签过滤
    @XStreamAlias("filter-html")
    private boolean filterHtml;

    @XStreamAlias("analyzer-bean-name")
    private String analyzerBeanName;

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getIndexVersion() {
        return indexVersion;
    }

    public void setIndexVersion(String indexVersion) {
        this.indexVersion = indexVersion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnalyzerBeanName() {
        return analyzerBeanName;
    }

    public void setAnalyzerBeanName(String analyzerBeanName) {
        this.analyzerBeanName = analyzerBeanName;
    }

    public boolean isAllowDuplication() {
        return allowDuplication;
    }

    public void setAllowDuplication(boolean allowDuplication) {
        this.allowDuplication = allowDuplication;
    }

    public int getSearchMaxLimit() {
        return searchMaxLimit;
    }

    public void setSearchMaxLimit(int searchMaxLimit) {
        this.searchMaxLimit = searchMaxLimit;
    }

    public String getSearchFields() {
        return searchFields;
    }

    public void setSearchFields(String searchFields) {
        this.searchFields = searchFields;
    }

    public String getHighLightPrefix() {
        return highLightPrefix;
    }

    public void setHighLightPrefix(String highLightPrefix) {
        this.highLightPrefix = highLightPrefix;
    }

    public String getHighLightSuffix() {
        return highLightSuffix;
    }

    public void setHighLightSuffix(String highLightSuffix) {
        this.highLightSuffix = highLightSuffix;
    }

    public boolean isFilterHtml() {
        return filterHtml;
    }

    public void setFilterHtml(boolean filterHtml) {
        this.filterHtml = filterHtml;
    }

    public Object clone() {
        LuceneConfig o = null;
        try {
            o = (LuceneConfig) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }
}
