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
package org.tinygroup.tinytest.contextloader;

import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.springutil.ExtendsSpringBeanContainer;
import org.tinygroup.tinyrunner.Runner;

import java.util.ArrayList;

/**
 * @author renhui
 */
public class TinyGenericXmlContextLoader extends AbstractGenericContextLoader {

    /**
     * Create a new {@link XmlBeanDefinitionReader}.
     *
     * @return a new XmlBeanDefinitionReader.
     * @see XmlBeanDefinitionReader
     */
    @Override
    protected BeanDefinitionReader createBeanDefinitionReader(
            final GenericApplicationContext context) {
        return new XmlBeanDefinitionReader(context);
    }

    /**
     * Returns &quot;<code>-context.xml</code>&quot;.
     */
    @Override
    public String getResourceSuffix() {
        return "-context.xml";
    }

    @Override
    protected void prepareContext(GenericApplicationContext context) {
        // 启动tiny框架
        FileSystemXmlApplicationContext applicationContext = new FileSystemXmlApplicationContext();
        BeanContainerFactory.initBeanContainer(ExtendsSpringBeanContainer.class
                .getName());
        ExtendsSpringBeanContainer beanContainer = (ExtendsSpringBeanContainer) BeanContainerFactory
                .getBeanContainer(getClass().getClassLoader());
        beanContainer.setApplicationContext(applicationContext);
        Runner.init(ExtendsSpringBeanContainer.class, null,
                new ArrayList<String>());
        context.setParent(applicationContext);
    }

    protected void postRefresh(GenericApplicationContext context) {
    }

}