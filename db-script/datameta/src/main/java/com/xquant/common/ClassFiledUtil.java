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
package com.xquant.common;

import java.lang.reflect.Field;

public class ClassFiledUtil {
    /**
     * 判断clazz是否实现了interfaceClazz
     *
     * @param clazz
     * @param interfaceClazz
     * @return
     */
    public static boolean implmentInterface(Class<?> clazz, Class<?> interfaceClazz) {
        return interfaceClazz.isAssignableFrom(clazz);
    }


    /**
     * 从clazz及其父类中获取属性，如果找不到则返回null
     * @param clazz
     * @param name
     * @return
     */
    public static Field getDeclaredFieldWithParent(Class<?> clazz, String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            if (clazz.getSuperclass() != null) {
                return getDeclaredFieldWithParent(clazz.getSuperclass(), name);
            }
        }
        return null;

    }
}
