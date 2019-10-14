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

@XStreamAlias("cache-action")
public abstract class CacheAction {

    @XStreamAsAttribute
    private String group;
    @XStreamAsAttribute
    private String keys;
    @XStreamAsAttribute
    @XStreamAlias("parameter-names")
    private String parameterNames;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * 配置关联的aop缓存处理器
     *
     * @return
     */
    public abstract Class<? extends AopCacheProcessor> bindAopProcessType();

    public CacheMetadata createMetadata() {
        CacheMetadata metadata = new CacheMetadata();
        metadata.setGroup(group);
        return metadata;
    }

}
