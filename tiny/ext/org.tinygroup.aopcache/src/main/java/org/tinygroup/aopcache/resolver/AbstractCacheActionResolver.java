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
package org.tinygroup.aopcache.resolver;

import org.springframework.beans.factory.InitializingBean;
import org.tinygroup.aopcache.CacheActionResolver;
import org.tinygroup.aopcache.interceptor.AopCacheInterceptor;
import org.tinygroup.commons.tools.Assert;

public abstract class AbstractCacheActionResolver implements CacheActionResolver,
        InitializingBean {

    private int order;
    private AopCacheInterceptor interceptor;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setInterceptor(AopCacheInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.assertNotNull(interceptor, "AopCacheInterceptor must not be null");
        interceptor.addResolver(this);
    }


}
