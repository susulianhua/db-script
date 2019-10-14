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
package org.tinygroup.aopcache;

import org.springframework.util.CollectionUtils;
import org.tinygroup.aopcache.base.AopCacheHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * aop缓存执行链
 *
 * @author renhui
 */
public class AopCacheExecutionChain {

    private AopCacheHolder[] processors;
    private List<AopCacheHolder> processorList;

    /**
     *
     */
    public AopCacheExecutionChain() {
        this(null);
    }

    /**
     * 构造方法初始化processors
     *
     * @param processors
     */
    public AopCacheExecutionChain(AopCacheHolder[] processors) {
        this.processors = processors;
    }

    /**
     * 单个增加processor
     *
     * @param processor
     */
    public void addAopCacheProcessor(AopCacheHolder processor) {
        initAopCacheProcessor();
        this.processorList.add(processor);
    }

    /**
     * 增加多个processor
     *
     * @param processors
     */
    public void addAopCacheProcessors(AopCacheHolder[] processors) {
        if (processors != null) {
            initAopCacheProcessor();
            this.processorList.addAll(Arrays.asList(processors));
        }
    }

    public AopCacheHolder[] getAopCacheProcessors() {
        if (this.processors == null && this.processorList != null) {
            this.processors = this.processorList
                    .toArray(new AopCacheHolder[this.processorList.size()]);
        }
        return this.processors;
    }

    private void initAopCacheProcessor() {
        if (this.processorList == null) {
            this.processorList = new ArrayList<AopCacheHolder>();
        }
        if (this.processors != null) {
            this.processorList.addAll(Arrays.asList(this.processors));
            this.processors = null;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AopCacheExecutionChain with  AopCacheProcessor ");
        if (!CollectionUtils.isEmpty(this.processorList)) {
            sb.append(this.processorList.size()).append(" processor");
            if (this.processorList.size() > 1) {
                sb.append("s");
            }
        }
        return sb.toString();
    }

}
