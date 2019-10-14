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
import org.tinygroup.aopcache.processor.AopCacheRemoveProcessor;

@XStreamAlias("cache-remove")
public class CacheRemove extends CacheAction {

    @XStreamAlias("remove-keys")
    @XStreamAsAttribute
    private String removeKeys;

    @XStreamAlias("remove-groups")
    @XStreamAsAttribute
    private String removeGroups;

    @Override
    public Class<? extends AopCacheProcessor> bindAopProcessType() {
        return AopCacheRemoveProcessor.class;
    }

    @Override
    public CacheMetadata createMetadata() {
        CacheMetadata metadata = super.createMetadata();
        metadata.setRemoveKeys(removeKeys);
        metadata.setRemoveGroups(removeGroups);
        return metadata;
    }

    public String getRemoveKeys() {
        return removeKeys;
    }

    public void setRemoveKeys(String removeKeys) {
        this.removeKeys = removeKeys;
    }

    public String getRemoveGroups() {
        return removeGroups;
    }

    public void setRemoveGroups(String removeGroups) {
        this.removeGroups = removeGroups;
    }

}
