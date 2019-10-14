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

import org.tinygroup.aopcache.config.CacheAction;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface AnnotationConfigResolver {

    /**
     * 解析器匹配的注解
     *
     * @param annotation
     * @return
     */
    boolean annotationMatch(Annotation annotation);

    /**
     * 解析匹配注解的内容，组装成CacheAction实例返回
     *
     * @param method
     * @return
     */
    CacheAction resolve(Method method);

}
