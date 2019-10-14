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
package org.tinygroup.beancontainer;

import org.tinygroup.vfs.FileObject;

import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * Created by luoguo on 2014/5/15.
 */
public interface BeanContainer<C> {
    /**
     * 返回原生的Bean窗口类型
     *
     * @return
     */
    C getBeanContainerPrototype();

    /**
     * 获取子容器
     *
     * @param files
     * @param loader
     */
    BeanContainer<?> getSubBeanContainer(List<FileObject> files, ClassLoader loader);


    BeanContainer<?> getSubBeanContainer(ClassLoader loader);


    void removeSubBeanContainer(ClassLoader loader);

    void setParent(BeanContainer<?> container);

    /**
     * 返回子窗口列表
     *
     * @return
     */
    Map<ClassLoader, BeanContainer<?>> getSubBeanContainers();

    /**
     * 返回指定类型的bean列表
     *
     * @param type
     * @param <T>
     * @return
     */
    <T> Collection<T> getBeans(Class<T> type);

    /**
     * 获取指定名称的Bean
     *
     * @param name
     * @param <T>
     * @return
     */
    <T> T getBean(String name);

    /**
     * 获取指定类型的Bean
     *
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T getBean(Class<T> clazz);

    /**
     * 获取指定类型指定名称的Bean
     *
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T getBean(String name, Class<T> clazz);

}
