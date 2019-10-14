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
package org.tinygroup.aopcache.base;

/**
 * 缓存配置信息关联的元数据
 *
 * @author renhui
 */
public class CacheMetadata {

    private String keys;

    private String removeKeys;

    private String removeGroups;

    private long expire = Long.MAX_VALUE;//暂时无用

    private String parameterNames;

    private String group;

    private boolean merge;

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public String getRemoveKeys() {
        return removeKeys;
    }

    public void setRemoveKeys(String removeKeys) {
        this.removeKeys = removeKeys;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getParameterNames() {
        return parameterNames;
    }

    public void setParameterNames(String parameterNames) {
        this.parameterNames = parameterNames;
    }

    public String getRemoveGroups() {
        return removeGroups;
    }

    public void setRemoveGroups(String removeGroups) {
        this.removeGroups = removeGroups;
    }

    public boolean isMerge() {
        return merge;
    }

    public void setMerge(boolean merge) {
        this.merge = merge;
    }
}
