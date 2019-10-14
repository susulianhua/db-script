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
package org.tinygroup.validate.test.scene.testcase;

import java.lang.reflect.Field;

public class TrySupperClass {
    public static void main(String[] args) {
        trySupperClass(TrySupperClass.class);
        getFiled("name", TrySupperClass.class, true);
    }

    public static void trySupperClass(Class clazz) {
        Class superclass = clazz.getSuperclass();
        System.out.println(superclass);
        if (superclass != null) {
            trySupperClass(superclass);
        }
    }

    private static Field getFiled(String fieldName, Class clazz,
                                  boolean includeParentClass) {
        try {
            Field field = clazz.getField(fieldName);
            return field;
        } catch (Exception e) {
            if (clazz.getSuperclass() == null)
                throw new RuntimeException(new NoSuchFieldException(fieldName));
            return getFiled(fieldName, clazz.getSuperclass(), true);
        }
    }
}
