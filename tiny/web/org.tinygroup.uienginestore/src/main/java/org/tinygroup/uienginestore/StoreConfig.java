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
package org.tinygroup.uienginestore;

import java.util.ArrayList;
import java.util.List;

/**
 * 定义保存属性
 * @author yancheng11334
 *
 */
public class StoreConfig {

    private boolean mergeTag;

    private boolean synStore;

    private int threadNum;

    private String mergeCssName;

    private String mergeJsName;

    private List<String> includePatterns = new ArrayList<String>();

    private List<String> excludePatterns = new ArrayList<String>();

    private String storePath;

    private long cssLimit;

    public boolean isMergeTag() {
        return mergeTag;
    }

    public void setMergeTag(boolean mergeTag) {
        this.mergeTag = mergeTag;
    }

    public boolean isSynStore() {
        return synStore;
    }

    public void setSynStore(boolean synStore) {
        this.synStore = synStore;
    }

    public long getCssLimit() {
        return cssLimit;
    }

    public void setCssLimit(long cssLimit) {
        this.cssLimit = cssLimit;
    }

    public int getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    public String getMergeCssName() {
        return mergeCssName;
    }

    public void setMergeCssName(String mergeCssName) {
        this.mergeCssName = mergeCssName;
    }

    public String getMergeJsName() {
        return mergeJsName;
    }

    public void setMergeJsName(String mergeJsName) {
        this.mergeJsName = mergeJsName;
    }

    public String getStorePath() {
        return storePath;
    }

    public void setStorePath(String storePath) {
        this.storePath = storePath;
    }

    public List<String> getIncludePatterns() {
        if (includePatterns == null) {
            includePatterns = new ArrayList<String>();
        }
        return includePatterns;
    }

    public void setIncludePatterns(List<String> includePatterns) {
        this.includePatterns = includePatterns;
    }

    public void addIncludePattern(String includePattern) {
        getIncludePatterns().add(includePattern);
    }

    public List<String> getExcludePatterns() {
        if (excludePatterns == null) {
            excludePatterns = new ArrayList<String>();
        }
        return excludePatterns;
    }

    public void setExcludePatterns(List<String> excludePatterns) {
        this.excludePatterns = excludePatterns;
    }

    public void addExcludePattern(String excludePattern) {
        getExcludePatterns().add(excludePattern);
    }

}
