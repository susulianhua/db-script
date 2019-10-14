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

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.tinygroup.beancontainer.BeanContainer;
import org.tinygroup.vfs.FileObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpringBootBeanContainer implements
        BeanContainer<ApplicationContext> {

    private ApplicationContext applicationContext;

    private Map<ClassLoader, BeanContainer<?>> subs = new HashMap<ClassLoader, BeanContainer<?>>();
    private BeanContainer<?> parent;

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public ApplicationContext getBeanContainerPrototype() {
        return applicationContext;
    }

    @Override
    public BeanContainer<?> getSubBeanContainer(List<FileObject> files,
                                                ClassLoader loader) {
        return null;
    }

    @Override
    public BeanContainer<?> getSubBeanContainer(ClassLoader loader) {
        return subs.get(loader);
    }

    @Override
    public void removeSubBeanContainer(ClassLoader loader) {
        subs.remove(loader);
    }

    @Override
    public void setParent(BeanContainer<?> container) {
        this.parent = container;
    }

    @Override
    public Map<ClassLoader, BeanContainer<?>> getSubBeanContainers() {
        return subs;
    }

    public <T> Collection<T> getBeans(Class<T> type) {
        Collection<T> collection = applicationContext.getBeansOfType(type)
                .values();
        if (collection.size() == 0 && parent != null) {
            collection = parent.getBeans(type);
        }
        return collection;
    }

    public <T> T getBean(String name) {
        try {
            return (T) applicationContext.getBean(name);
        } catch (NoSuchBeanDefinitionException e) {
            if (parent != null) {
                return (T) parent.getBean(name);
            }
            throw e;
        }
    }

    public <T> T getBean(Class<T> clazz) {

        String[] beanNames = applicationContext.getBeanNamesForType(clazz);
        if (beanNames.length == 1) {
            return (T) applicationContext.getBean(beanNames[0]);
        } else if (beanNames.length == 0 && parent != null) {
            return parent.getBean(clazz);
        } else {
            throw new NoSuchBeanDefinitionException(clazz,
                    "expected single bean but found "
                            + beanNames.length
                            + ": "
                            + StringUtils
                            .arrayToCommaDelimitedString(beanNames));
        }
    }

    public <T> T getBean(String name, Class<T> clazz) {
        try {
            return (T) applicationContext.getBean(name, clazz);
        } catch (NoSuchBeanDefinitionException e) {
            if (parent != null) {
                return parent.getBean(name, clazz);
            }
            throw e;
        }

    }

}
