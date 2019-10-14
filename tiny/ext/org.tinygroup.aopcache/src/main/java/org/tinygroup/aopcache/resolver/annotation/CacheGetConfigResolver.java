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
package org.tinygroup.aopcache.resolver.annotation;

import org.springframework.core.annotation.AnnotationUtils;
import org.tinygroup.aopcache.annotation.CacheGet;
import org.tinygroup.aopcache.config.CacheAction;
import org.tinygroup.aopcache.exception.AopCacheException;
import org.tinygroup.commons.tools.StringUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class CacheGetConfigResolver extends AbstractAnnotationConfigResolver {

    public boolean annotationMatch(Annotation annotation) {
        return annotation.annotationType().equals(CacheGet.class);
    }

    public CacheAction resolve(Method method) {
        CacheGet cacheGet = AnnotationUtils.findAnnotation(method,
                CacheGet.class);
        if (cacheGet == null) {
            throw new AopCacheException(String.format("方法:%s,不存在类型为:%s的注解",
                    method.getName(), CacheGet.class.getName()));
        }
        org.tinygroup.aopcache.config.CacheGet cacheGetAction = new org.tinygroup.aopcache.config.CacheGet();
        cacheGetAction.setKey(StringUtil.join(cacheGet.key(), SPLIT_KEY));
        cacheGetAction.setGroup(cacheGet.group());
        return cacheGetAction;
    }

}
