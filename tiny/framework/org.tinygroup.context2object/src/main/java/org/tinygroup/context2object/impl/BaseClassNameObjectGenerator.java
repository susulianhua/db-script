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
package org.tinygroup.context2object.impl;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.context.Context;
import org.tinygroup.context2object.ObjectAssembly;
import org.tinygroup.context2object.TypeCreator;
import org.tinygroup.context2object.util.Context2ObjectUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseClassNameObjectGenerator {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(BaseClassNameObjectGenerator.class);
    private List<ObjectAssembly<?>> assemblies = new ArrayList<ObjectAssembly<?>>();
    private List<TypeCreator<?>> typeCreatorList = new ArrayList<TypeCreator<?>>();

    public void addTypeCreator(TypeCreator<?> typeCreator) {
        typeCreatorList.add(typeCreator);

    }

    public void removeTypeCreator(TypeCreator<?> typeCreator) {
        typeCreatorList.remove(typeCreator);

    }

    /**
     * 根据clazz从creators中获取其实例
     *
     * @param clazz
     * @return 若找到则返回对象实例，否则返回null
     */
    protected Object getIntanceByCreator(Class<?> clazz) {
        for (TypeCreator<?> creator : typeCreatorList) {
            if (clazz.equals(creator.getType())) { // clazz是否继承自getClass
                return creator.getInstance();
            }
        }
        return null;
    }

    protected ObjectAssembly<?> getObjectAssembly(Class<?> type) {
        for (ObjectAssembly<?> assembly : assemblies) {
            if (assembly.isMatch(type)) {
                return assembly;
            }
        }
        return null;
    }

    protected Class<?> getClazz(String className, ClassLoader loader) {
        try {
            return loader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void addObjectAssembly(ObjectAssembly<?> objectAssembly) {
        assemblies.add(objectAssembly);
    }

    public void removeObjectAssembly(ObjectAssembly<?> objectAssembly) {
        assemblies.remove(objectAssembly);
    }

    protected String getObjName(Object object) {
        String className = object.getClass().getSimpleName();
        if (className.length() == 1)
            return className.toLowerCase();
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }

    protected String getPreName(String preName, String varName) {
        if (StringUtil.isBlank(preName) && StringUtil.isBlank(varName)) {
            return null;
        } else if (StringUtil.isBlank(varName)) {
            return preName;
        } else if (StringUtil.isBlank(preName)) {
            return varName;
        }
        return String.format("%s.%s", preName, varName);
    }


    protected String getObjName(Class clazz) {
        String className = clazz.getSimpleName();
        if (className.length() == 1)
            return className.toLowerCase();
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }

    protected Field getDeclaredFieldWithParent(Class<?> clazz, String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            if (clazz.getSuperclass() != null) {
                return getDeclaredFieldWithParent(clazz.getSuperclass(), name);
            }
        }
        return null;

    }

    protected Object getPerpertyValue(String reallyName, Context context) {
        Object value = context.get(reallyName);
        if (value == null) {
            value = context.get(reallyName.replace(".", "_"));
        }
        if (value == null) {
            int index = reallyName.indexOf('.') + 1;
            if (index != 0) {
                value = getPerpertyValue(reallyName.substring(index), context);
            }
        }
        return value;
    }

    protected String getReallyPropertyName(String preName, String objName,
                                           String propertyName) {
        if (StringUtil.isBlank(preName)) {
            return String.format("%s.%s", objName, propertyName);
        }

        return String.format("%s.%s.%s", preName, objName, propertyName);
    }

    protected boolean isSimpleType(Class<?> clazz) {
        return Context2ObjectUtil.isSimpleType(clazz);
    }

    protected Object getPerpertyValue(String preName, String objName,
                                      String propertyName, Context context) {
        String reallyName = getReallyPropertyName(preName, objName,
                propertyName);
        return getPerpertyValue(reallyName, context);
    }

    /**
     * 判断clazz是否实现了interfaceClazz
     *
     * @param clazz
     * @param interfaceClazz
     * @return
     */
    protected boolean implmentInterface(Class<?> clazz, Class<?> interfaceClazz) {
        return interfaceClazz.isAssignableFrom(clazz);
    }

    protected boolean isNull(String str) {
        return StringUtil.isBlank(str);
    }

    /**
     * 根据clazz获取对象 先从creator中获取，若找不到，则去springbean中获取
     *
     * @param clazz
     * @return
     */
    protected Object getObjectInstance(Class<?> clazz) {
        Object o = getIntanceByCreator(clazz);
        if (o != null) {
            return o;
        }
        return getInstanceBySpringBean(clazz);
    }


    protected Object getInstanceBySpringBean(String bean) {
        if (bean == null || "".equals(bean)) {
            return null;
        }
        try {
            return BeanContainerFactory.getBeanContainer(
                    this.getClass().getClassLoader()).getBean(bean);
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.WARN, e.getMessage());
            return null;
        }

    }

    private Object getInstanceBySpringBean(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        try {
            return BeanContainerFactory.getBeanContainer(
                    this.getClass().getClassLoader()).getBean(clazz);
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.WARN, e.getMessage());
            try {
                return clazz.newInstance();
            } catch (Exception e1) {
                throw new RuntimeException(e1);
            }
        }
    }
}
