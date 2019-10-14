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
package org.tinygroup.service.loader;

import org.tinygroup.service.registry.ServiceRegistry;

import java.lang.annotation.Annotation;

/**
 * 功能说明:加载注解配置的serviceload
 * <p>
 * <p>
 * 开发人员: renhui <br>
 * 开发时间: 2013-5-2 <br>
 * <br>
 */
public interface AnnotationServiceLoader extends ServiceLoader {

    void loadService(Class<?> clazz, Annotation annotation,
                     ServiceRegistry serviceRegistry);

}
