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
package org.tinygroup.xstream;

import com.thoughtworks.xstream.converters.enums.EnumSingleValueConverter;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class CustomEnumSingleNameConverter extends EnumSingleValueConverter {

    private static final String CUSTOM_ENUM_NAME_METHOD = "getName";

    private static final String CUSTOM_ENUM_VALUE_OF_METHOD = "toEnum";

    private Class<? extends Enum<?>> enumType;

    public CustomEnumSingleNameConverter(Class<? extends Enum<?>> type) {

        super(type);

        this.enumType = type;

    }

    @Override
    public String toString(Object obj) {

        Method method = getCustomEnumNameMethod();

        if (method == null) {

            return super.toString(obj);

        } else {

            try {

                return (String) method.invoke(obj, (Object[]) null);

            } catch (Exception ex) {

                return super.toString(obj);

            }

        }

    }

    @Override

    public Object fromString(String str) {

        Method method = getCustomEnumStaticValueOfMethod();

        if (method == null) {

            return enhancedFromString(str);

        }

        try {

            return method.invoke(null, str);

        } catch (Exception ex) {

            return enhancedFromString(str);

        }

    }

    private Method getCustomEnumNameMethod() {

        try {

            return enumType.getMethod(CUSTOM_ENUM_NAME_METHOD, (Class<?>[]) null);

        } catch (Exception ex) {

            return null;

        }

    }

    private Method getCustomEnumStaticValueOfMethod() {

        try {

            Method method = enumType.getMethod(CUSTOM_ENUM_VALUE_OF_METHOD, new Class<?>[]{String.class});

            if (Modifier.isStatic(method.getModifiers())) {
                return method;
            }

            return null;

        } catch (Exception ex) {

            return null;

        }

    }

    private Object enhancedFromString(String str) {

        try {

            return super.fromString(str);

        } catch (Exception ex) {

            for (Enum<?> item : enumType.getEnumConstants()) {

                if (item.name().equalsIgnoreCase(str)) {

                    return item;

                }

            }
            throw new IllegalStateException("Cannot converter <" + str + "> to enum <" + enumType + ">");

        }

    }

}
