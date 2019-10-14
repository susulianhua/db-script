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
package org.tinygroup.springutil;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.SimpleTypeConverter;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.util.Collection;

/**
 * 功能说明:类型转换的工具类
 * <p>
 * 开发人员: renhui <br>
 * 开发时间: 2014-1-17 <br>
 * <br>
 */
public class TypeConverterUtil {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(TypeConverterUtil.class);
    private static SimpleTypeConverter converter = new SimpleTypeConverter();

    static {
        Collection<PropertyEditorRegistrar> editorRegistrars = BeanContainerFactory
                .getBeanContainer(TypeConverterUtil.class.getClassLoader())
                .getBeans(PropertyEditorRegistrar.class);
        for (PropertyEditorRegistrar registrar : editorRegistrars) {
            registrar.registerCustomEditors(converter);
        }
    }

    public static Object typeConverter(Object value, String className) {

        try {
            Class clazz = Class.forName(className);
            return converter.convertIfNecessary(value, clazz);
        } catch (ClassNotFoundException e) {
            LOGGER.errorMessage("get Class error with className:" + className,
                    e);
            throw new RuntimeException(e);
        }
    }

    public static Object typeConverter(Object value, Class type) {
        return converter.convertIfNecessary(value, type);
    }

    public static void registerCustomEditors(PropertyEditorRegistrar registrar) {
        if (registrar != null) {
            registrar.registerCustomEditors(converter);
        }
    }

}
