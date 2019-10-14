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
package org.tinygroup.annotation;

import java.lang.annotation.Annotation;

/**
 * 符合类注解定义的处理器
 *
 * @param <T>
 * @author luoguo
 */
public interface AnnotationClassAction {
    /**
     * @param clazz      处理的对象的类型
     * @param annotation 处理的注解
     */
    <T> void process(Class<T> clazz, Annotation annotation);
}
