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
package org.tinygroup.tinyioc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Identifies qualifier annotations. Anyone can define a new qualifier. A
 * qualifier annotation:
 * <p>
 * <ul>
 * <li>is annotated with {@code @Qualifier}, {@code @Retention(RUNTIME)},
 * and typically {@code @Documented}.</li>
 * <li>can have attributes.</li>
 * <li>may be part of the public API, much like the dependency type, but
 * unlike implementation types which needn't be part of the public
 * API.</li>
 * <li>may have restricted usage if annotated with {@code @Target}. While
 * this specification covers applying qualifiers to fields and
 * parameters only, some injector configurations might use qualifier
 * annotations in other places (on methods or classes for example).</li>
 * </ul>
 * <p>
 * <p>For example:
 * <p>
 * <pre>
 *   &#064;java.lang.annotation.Documented
 *   &#064;java.lang.annotation.Retention(RUNTIME)
 *   &#064;org.tinygroup.tinyioc.annotation.Qualifier
 *   public @interface Leather {
 *     Color color() default Color.TAN;
 *     public enum Color { RED, BLACK, TAN }
 *   }</pre>
 *
 * @see Named @Named
 */
@Target(ANNOTATION_TYPE)
@Retention(RUNTIME)
@Documented
public @interface Qualifier {
}
