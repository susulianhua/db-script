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
package org.tinygroup.tinytest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextLoader;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.SmartContextLoader;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.DefaultTestContext;
import org.springframework.test.context.web.WebTestContextBootstrapper;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.springutil.ExtendsSpringBeanContainer;
import org.tinygroup.tinyrunner.Runner;

import java.util.ArrayList;

/**
 * 重新构建MergedContextConfiguration,使测试框架启动的spring容器以tiny框架的容器为父容器
 *
 * @author renhui
 */
public class TinyWebTestContextBootstrapper extends WebTestContextBootstrapper {

    private final Log logger = LogFactory.getLog(getClass());

    public TestContext buildTestContext() {
        return new DefaultTestContext(getBootstrapContext().getTestClass(),
                buildTinyMergedContextConfiguration(),
                getCacheAwareContextLoaderDelegate());
    }

    protected MergedContextConfiguration buildTinyMergedContextConfiguration() {
        MergedContextConfiguration mergedContextConfiguration = super
                .buildMergedContextConfiguration();
        // 获取contextLoader
        ContextLoader contextLoader = mergedContextConfiguration
                .getContextLoader();
        if (contextLoader == null) {
            contextLoader = BeanUtils.instantiateClass(
                    getDefaultContextLoaderClass(getBootstrapContext()
                            .getTestClass()), ContextLoader.class);
        }
        ContextLoader tinyContextLoader = new TinyDelegatingSmartContextLoader(
                (SmartContextLoader) contextLoader);
        if (logger.isDebugEnabled()) {
            logger.debug(String
                    .format("TinyWebTestContextBootstrapper really ContextLoader: [%s]",
                            tinyContextLoader.getClass().getName()));
        }

        // 启动tiny框架
        ExtendsSpringBeanContainer beanContainer = (ExtendsSpringBeanContainer) BeanContainerFactory
                .getBeanContainer(getClass().getClassLoader());
        if (beanContainer == null) {
            BeanContainerFactory
                    .initBeanContainer(ExtendsSpringBeanContainer.class
                            .getName());
            beanContainer = (ExtendsSpringBeanContainer) BeanContainerFactory
                    .getBeanContainer(getClass().getClassLoader());
            XmlWebApplicationContext xmlWebApplicationContext = (XmlWebApplicationContext) BeanUtils
                    .instantiateClass(XmlWebApplicationContext.class);
            beanContainer.setApplicationContext(xmlWebApplicationContext);
            Runner.init(ExtendsSpringBeanContainer.class, null,
                    new ArrayList<String>());
        }
        ApplicationContext context = beanContainer.getBeanContainerPrototype();
        TinyCacheAwareContextLoaderDelegate cacheAwareContextLoaderDelegate = (TinyCacheAwareContextLoaderDelegate) getCacheAwareContextLoaderDelegate();
        MergedContextConfiguration newParentMergedContextConfiguration = new MergedContextConfiguration(
                null, null, null, null, tinyContextLoader);
        cacheAwareContextLoaderDelegate.getPublicContextCache().put(
                newParentMergedContextConfiguration, context);
        MergedContextConfiguration newMergedContextConfiguration = new TinyMergedContextConfiguration(
                tinyContextLoader, cacheAwareContextLoaderDelegate,
                mergedContextConfiguration, newParentMergedContextConfiguration);
        if (logger.isDebugEnabled()) {
            logger.debug(String
                    .format("TinyWebTestContextBootstrapper build MergedContextConfiguration: [%s]",
                            newMergedContextConfiguration.getClass().getName()));
        }
        return newMergedContextConfiguration;
    }

}
