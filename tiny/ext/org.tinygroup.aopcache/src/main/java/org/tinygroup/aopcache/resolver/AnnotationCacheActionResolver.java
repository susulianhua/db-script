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

import org.springframework.core.annotation.AnnotationUtils;
import org.tinygroup.aopcache.AnnotationConfigResolver;
import org.tinygroup.aopcache.base.MethodDescription;
import org.tinygroup.aopcache.config.CacheAction;
import org.tinygroup.commons.tools.CollectionUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注解方式缓存配置解析器 Created by renhui on 2015/9/24.
 */
public class AnnotationCacheActionResolver extends AbstractCacheActionResolver {

    private List<AnnotationConfigResolver> resolvers = new ArrayList<AnnotationConfigResolver>();

    private Map<MethodDescription, List<CacheAction>> methodActionMap = new HashMap<MethodDescription, List<CacheAction>>();

    public void setResolvers(List<AnnotationConfigResolver> resolvers) {
        this.resolvers = resolvers;
    }

    public void addConfigResolver(AnnotationConfigResolver resolver) {
        resolvers.add(resolver);
    }

    public List<CacheAction> resolve(Method method) {
        MethodDescription methodDescription = MethodDescription.createMethodDescription(method);
        List<CacheAction> cacheActions = methodActionMap.get(methodDescription);
        if (CollectionUtil.isEmpty(cacheActions)) {
            Annotation[] annotations = AnnotationUtils.getAnnotations(method);
            if (annotations != null) {
                cacheActions = new ArrayList<CacheAction>();
                for (Annotation annotation : annotations) {
                    for (AnnotationConfigResolver resolver : resolvers) {
                        if (resolver.annotationMatch(annotation)) {
                            cacheActions.add(resolver.resolve(method));
                        }
                    }
                }
            }
            methodActionMap.put(methodDescription, cacheActions);
        }
        return cacheActions;
    }

}
