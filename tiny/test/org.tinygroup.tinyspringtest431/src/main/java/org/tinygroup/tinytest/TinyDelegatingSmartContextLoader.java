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

import org.springframework.context.ApplicationContext;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.SmartContextLoader;
import org.springframework.test.context.support.AbstractDelegatingSmartContextLoader;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.springutil.ExtendsSpringBeanContainer;

/**
 * 重写loadContext方法，捕捉IllegalStateException，返回GenericApplicationContext实例
 *
 * @author renhui
 */
public class TinyDelegatingSmartContextLoader extends
        AbstractDelegatingSmartContextLoader {

    private final SmartContextLoader smartContextLoader;

    public TinyDelegatingSmartContextLoader(
            SmartContextLoader smartContextLoader) {
        super();
        this.smartContextLoader = smartContextLoader;
    }

    @Override
    public ApplicationContext loadContext(
            MergedContextConfiguration mergedConfig) throws Exception {

        try {
            return smartContextLoader.loadContext(mergedConfig);
        } catch (IllegalStateException e) {
            ExtendsSpringBeanContainer beanContainer = (ExtendsSpringBeanContainer) BeanContainerFactory
                    .getBeanContainer(getClass().getClassLoader());
            return beanContainer.getBeanContainerPrototype();
        }
    }

    @Override
    protected SmartContextLoader getXmlLoader() {
        return null;
    }

    @Override
    protected SmartContextLoader getAnnotationConfigLoader() {
        return null;
    }

}
