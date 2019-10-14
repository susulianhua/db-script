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
package org.tinygroup.aopcache.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.tinygroup.aopcache.AopCacheProcessor;
import org.tinygroup.aopcache.base.CacheMetadata;
import org.tinygroup.aopcache.processor.AopCachePutProcessor;

/**
 * Created by renhui on 2015/9/23.
 */
@XStreamAlias("cache-put")
public class CachePut extends CacheAction {
    @XStreamAsAttribute
    private String keys;//多个key以逗号分隔
    @XStreamAsAttribute
    @XStreamAlias("parameter-names")
    private String parameterNames;//多个参数名称以逗号分隔

    @XStreamAlias("remove-keys")
    @XStreamAsAttribute
    private String removeKeys;
    @XStreamAsAttribute
    private long expire;
    @XStreamAsAttribute
    private boolean merge = false;

    @XStreamAlias("remove-groups")
    @XStreamAsAttribute
    private String removeGroups;

    @Override
    public Class<? extends AopCacheProcessor> bindAopProcessType() {
        return AopCachePutProcessor.class;
    }

    @Override
    public CacheMetadata createMetadata() {
        CacheMetadata metadata = super.createMetadata();
        metadata.setKeys(keys);
        metadata.setParameterNames(parameterNames);
        metadata.setRemoveKeys(removeKeys);
        metadata.setRemoveGroups(removeGroups);
        metadata.setExpire(expire);
        metadata.setMerge(merge);
        return metadata;
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

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
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
