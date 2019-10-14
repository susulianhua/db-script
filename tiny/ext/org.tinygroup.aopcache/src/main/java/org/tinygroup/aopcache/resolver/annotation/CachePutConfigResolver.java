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
import org.tinygroup.aopcache.annotation.CachePut;
import org.tinygroup.aopcache.config.CacheAction;
import org.tinygroup.aopcache.exception.AopCacheException;
import org.tinygroup.commons.tools.StringUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class CachePutConfigResolver extends AbstractAnnotationConfigResolver {
    public boolean annotationMatch(Annotation annotation) {
        return annotation.annotationType().equals(CachePut.class);
    }

    public CacheAction resolve(Method method) {
        CachePut cachePut = AnnotationUtils.findAnnotation(method,
                CachePut.class);
        if (cachePut == null) {
            throw new AopCacheException(String.format("方法:%s,不存在类型为:%s的注解",
                    method.getName(), CachePut.class.getName()));
        }
        org.tinygroup.aopcache.config.CachePut cachePutAction = new org.tinygroup.aopcache.config.CachePut();
        cachePut.keys();
        cachePutAction.setKeys(StringUtil.join(cachePut.keys(), SPLIT_KEY));
        cachePutAction.setGroup(cachePut.group());
        cachePutAction.setExpire(cachePut.expire());
        cachePutAction.setMerge(cachePut.merge());
        cachePutAction.setParameterNames(StringUtil.join(cachePut.parameterNames(), SPLIT_KEY));
        cachePutAction.setRemoveKeys(StringUtil.join(cachePut.removeKeys(), SPLIT_KEY));
        cachePutAction.setRemoveGroups(StringUtil.join(cachePut.removeGroups(), SPLIT_KEY));
        return cachePutAction;
    }

}
