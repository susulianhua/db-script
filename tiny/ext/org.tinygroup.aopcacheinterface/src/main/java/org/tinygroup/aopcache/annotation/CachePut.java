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
package org.tinygroup.aopcache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CachePut {

    /**
     * 要缓冲的对象参数名称,多个参数名称以逗号分隔
     *
     * @return
     */
    String[] parameterNames() default {};

    /**
     * 缓存的keys
     *
     * @return
     */
    String[] keys() default {};

    String group() default "";

    /**
     * 要从缓存移除的key,多个key以逗号分隔开
     *
     * @return
     */
    String[] removeKeys() default {};

    /**
     * 从缓存移除group,多个组以逗号分隔开
     *
     * @return
     */
    String[] removeGroups() default {};

    /**
     * 设置缓存过期时间，默认不过期
     *
     * @return
     */
    long expire() default Long.MAX_VALUE;

    /**
     * 更新的时候时候合并缓存数据
     * 如果为true，合并
     * 如果为false，后置覆盖前者
     *
     * @return
     */
    boolean merge() default false;

}
