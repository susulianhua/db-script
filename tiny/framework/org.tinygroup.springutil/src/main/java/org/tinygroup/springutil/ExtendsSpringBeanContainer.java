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
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.util.StringUtils;
import org.tinygroup.beancontainer.BeanContainer;
import org.tinygroup.beanwrapper.BeanWrapperHolder;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.vfs.FileObject;

import java.util.*;

public class ExtendsSpringBeanContainer extends SpringBeanContainer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExtendsSpringBeanContainer.class);
    private AbstractRefreshableConfigApplicationContext applicationContext = null;
    private List<String> configs = new ArrayList<String>();
    private Map<ClassLoader, BeanContainer<?>> subs = new HashMap<ClassLoader, BeanContainer<?>>();
    private boolean inited = false;
    private BeanContainer<?> parent;
    private boolean initedByDefault = false;


    public ExtendsSpringBeanContainer() {
        if (inited == true)
            return;
        inited = true;
        initedByDefault = true;
        applicationContext = new FileSystemXmlApplicationContext();
        applicationContext.setAllowBeanDefinitionOverriding(true);
        applicationContext.refresh();
    }


    public ExtendsSpringBeanContainer(ExtendsSpringBeanContainer parent, ClassLoader loader) {
        this(parent, loader, FileSystemXmlApplicationContext.class);
        // initNoBeanCaseInfo();
    }

    public ExtendsSpringBeanContainer(ExtendsSpringBeanContainer parent, ClassLoader loader,
                                      Class<? extends AbstractRefreshableConfigApplicationContext> clazz) {
        if (inited == true)
            return;
        inited = true;
        try {
            applicationContext = clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        applicationContext.setParent(parent.getBeanContainerPrototype());
        applicationContext.setAllowBeanDefinitionOverriding(true);
        applicationContext.setClassLoader(loader);
        applicationContext.refresh();
    }

    public ExtendsSpringBeanContainer(ExtendsSpringBeanContainer parent, List<FileObject> files, ClassLoader loader,
                                      Class<? extends AbstractRefreshableConfigApplicationContext> clazz) {
        if (inited == true)
            return;
        inited = true;
        List<String> configLocations = new ArrayList<String>();
        for (FileObject fileObject : files) {
            String urlString = fileObject.getURL().toString();
            if (!configLocations.contains(urlString)) {
                configLocations.add(urlString);
                LOGGER.logMessage(LogLevel.INFO, "添加Spring配置文件:{urlString}", urlString);
            }
        }
        try {
            applicationContext = clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        applicationContext.setConfigLocations(listToArray(configLocations));
        applicationContext.setParent(parent.getBeanContainerPrototype());
        applicationContext.setAllowBeanDefinitionOverriding(true);
        applicationContext.setClassLoader(loader);
        applicationContext.refresh();
        // initNoBeanCaseInfo();
    }

    public ExtendsSpringBeanContainer(ExtendsSpringBeanContainer parent, List<FileObject> files, ClassLoader loader) {
        this(parent, files, loader, FileSystemXmlApplicationContext.class);
        // initNoBeanCaseInfo();
    }

    private static String[] listToArray(List<String> list) {
        String[] a = new String[0];
        return list.toArray(a);
    }

    public ApplicationContext getBeanContainerPrototype() {
        return applicationContext;
    }

    /**
     * 当调用无参构造函数时，会默认初始化为FileSystemXmlApplicationContext实例
     * 可通过此方法改变ApplicationContext的类型
     *
     * @param clazz
     */
    public void setApplicationType(Class<? extends AbstractRefreshableConfigApplicationContext> clazz) {

        if (initedByDefault == false) {
            throw new RuntimeException("该方法仅允许执行无参构造函数时调用");
        }
        if (applicationContext != null) {
            applicationContext.destroy();
        }
        try {
            applicationContext = clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        applicationContext.setAllowBeanDefinitionOverriding(true);
        applicationContext.refresh();

    }

    /**
     * 当调用无参构造函数时，会默认初始化为FileSystemXmlApplicationContext实例
     * 可通过此方法改变ApplicationContext的实例
     *
     * @param clazz
     */
    public void setApplicationContext(AbstractRefreshableConfigApplicationContext context) {
        applicationContext = context;
    }

    public BeanContainer<?> getSubBeanContainer(List<FileObject> files, ClassLoader loader) {
        ExtendsSpringBeanContainer b = new ExtendsSpringBeanContainer(this, files, loader);
        subs.put(loader, b);
        b.setParent(this);
        return b;
    }

    public BeanContainer<?> getSubBeanContainer(ClassLoader loader) {
        return subs.get(loader);
    }

    public Map<ClassLoader, BeanContainer<?>> getSubBeanContainers() {
        return subs;
    }

    public <T> Collection<T> getBeans(Class<T> type) {
        Collection<T> collection = applicationContext.getBeansOfType(type).values();
        if (collection.size() == 0 && parent != null) {
            collection = parent.getBeans(type);
        }
        return collection;
    }

    public <T> T getBean(String name) {
        try {
            return (T) applicationContext.getBean(name);
        } catch (NoSuchBeanDefinitionException e) {
            // String message = e.getMessage();
            // if (message.equals(noBeanCaseInfo.replace(hashCode() + "",
            // name))) {
            if (parent != null) {
                return (T) parent.getBean(name);
            }
            // }
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
            throw new NoSuchBeanDefinitionException(clazz, "expected single bean but found " + beanNames.length + ": "
                    + StringUtils.arrayToCommaDelimitedString(beanNames));
        }
    }

    public <T> T getBean(String name, Class<T> clazz) {
        try {
            return (T) applicationContext.getBean(name, clazz);
        } catch (NoSuchBeanDefinitionException e) {
            // String message = e.getMessage();
            // if (message.equals(noBeanCaseInfo.replace(hashCode() + "",
            // name))) {
            if (parent != null) {
                return parent.getBean(name, clazz);
            }
            // }
            throw e;
        }

    }

    public void regSpringConfigXml(List<FileObject> files) {
        for (FileObject fileObject : files) {
            LOGGER.logMessage(LogLevel.INFO, "添加文件:{}", fileObject.getPath());
            String urlString = fileObject.getURL().toString();
            addUrl(urlString);
        }
    }

    public void regSpringConfigXml(String files) {
        String urlString = ExtendsSpringBeanContainer.class.getResource(files).toString();
        addUrl(urlString);
    }

    private void addUrl(String urlString) {
        if (!configs.contains(urlString)) {
            configs.add(urlString);
            LOGGER.logMessage(LogLevel.INFO, "添加Spring配置文件:{urlString}", urlString);
        }
    }

    public void removeUrl(String urlString) {
        if (configs.contains(urlString)) {
            configs.remove(urlString);
            LOGGER.logMessage(LogLevel.INFO, "删除Spring配置文件:{urlString}", urlString);
        }
    }

    public void refresh() {
        applicationContext.setConfigLocations(listToArray(configs));
        applicationContext.refresh();
    }

    public void setParent(BeanContainer<?> container) {
        this.parent = container;
    }

    public void removeSubBeanContainer(ClassLoader loader) {
        subs.remove(loader);
    }

    public void clear() {
        if (applicationContext instanceof ConfigurableApplicationContext) {
            ((ConfigurableApplicationContext) applicationContext).close();
        }
        applicationContext = null;
        BeanWrapperHolder.clear();
    }

}
