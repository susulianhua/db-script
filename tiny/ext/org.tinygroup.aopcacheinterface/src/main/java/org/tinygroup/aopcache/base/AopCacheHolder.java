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

import org.tinygroup.aopcache.AopCacheProcessor;

/**
 * @author renhui
 */
public class AopCacheHolder {

    private AopCacheProcessor processor;

    private CacheMetadata metadata;


    public AopCacheHolder() {
        super();
    }

    public AopCacheHolder(AopCacheProcessor processor, CacheMetadata metadata) {
        super();
        this.processor = processor;
        this.metadata = metadata;
    }

    public CacheMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(CacheMetadata metadata) {
        this.metadata = metadata;
    }

    public AopCacheProcessor getProcessor() {
        return processor;
    }

    public void setProcessor(AopCacheProcessor processor) {
        this.processor = processor;
    }


}
