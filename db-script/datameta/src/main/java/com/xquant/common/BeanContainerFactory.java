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


public class BeanContainerFactory {
    private static BeanContainer<?> container;

    /**
     * 请使用initBeanContainer方法
     *
     * @param beanClassName
     */
    @Deprecated
    public static void setBeanContainer(String beanClassName) {
        if (container != null
                && container.getClass().getName().equals(beanClassName)) {
            return;
        }
        try {
            container = (BeanContainer) Class.forName(beanClassName)
                    .newInstance();
        } catch (Exception e) {
            throw new RuntimeException("初始化beancontainer:" + beanClassName + "失败");
        }
    }

    public static void initBeanContainer(String beanClassName) {
        if (container != null
                && !container.getClass().getName().equals(beanClassName)) {
            throw new RuntimeException("container已存在,且类型与" + beanClassName
                    + "不匹配,请勿重复执行初始化");
        }
        if (container != null) {
            return;
        }
        try {
            container = (BeanContainer) Class.forName(beanClassName)
                    .newInstance();
        } catch (Exception e) {
            throw new RuntimeException("初始化beancontainer:" + beanClassName + "失败");
        }
    }

    public static <T extends BeanContainer<?>> void initBeanContainer(Class<T> clazz, Object applicationContext) {
        String beanClassName = clazz.getName();
        if (container != null
                && !container.getClass().getName().equals(beanClassName)) {
            throw new RuntimeException("container已存在,且类型与" + beanClassName
                    + "不匹配,请勿重复执行初始化");
        }
        if (container != null) {
            return;
        }
        try {
            Class<?> cls[] = new Class[]{Object.class};
            container = clazz
                    .getConstructor(cls).newInstance(applicationContext);
        } catch (Exception e) {
            throw new RuntimeException("初始化beancontainer:" + beanClassName + "失败", e);
        }
    }

    public static BeanContainer<?> getBeanContainer(ClassLoader loader) {
        if (loader == BeanContainerFactory.class.getClassLoader()) {
            return container;
        } else {
            return container.getSubBeanContainer(loader);
        }

    }

    public static void removeBeanContainer(ClassLoader loader) {
        container.removeSubBeanContainer(loader);

    }

    public static void destroy() {
        container = null;
    }
}
