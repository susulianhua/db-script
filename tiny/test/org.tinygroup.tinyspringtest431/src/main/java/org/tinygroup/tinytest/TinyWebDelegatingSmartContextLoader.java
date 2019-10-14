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
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.SmartContextLoader;
import org.springframework.test.context.support.AbstractDelegatingSmartContextLoader;
import org.springframework.test.context.web.WebMergedContextConfiguration;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.springutil.ExtendsSpringBeanContainer;

import javax.servlet.ServletContext;

public class TinyWebDelegatingSmartContextLoader extends
        AbstractDelegatingSmartContextLoader {

    private final SmartContextLoader smartContextLoader;

    public TinyWebDelegatingSmartContextLoader(
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

    protected void configureWebResources(GenericWebApplicationContext context,
                                         WebMergedContextConfiguration webMergedConfig) {

        ApplicationContext parent = context.getParent();

        // if the WAC has no parent or the parent is not a WAC, set the WAC as
        // the Root WAC:
        if (parent == null || (!(parent instanceof WebApplicationContext))) {
            String resourceBasePath = webMergedConfig.getResourceBasePath();
            ResourceLoader resourceLoader = resourceBasePath
                    .startsWith(ResourceLoader.CLASSPATH_URL_PREFIX) ? new DefaultResourceLoader()
                    : new FileSystemResourceLoader();

            ServletContext servletContext = new MockServletContext(
                    resourceBasePath, resourceLoader);
            servletContext
                    .setAttribute(
                            WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
                            context);
            context.setServletContext(servletContext);
        } else {
            ServletContext servletContext = null;
            // find the Root WAC
            while (parent != null) {
                if (parent instanceof WebApplicationContext
                        && !(parent.getParent() instanceof WebApplicationContext)) {
                    servletContext = ((WebApplicationContext) parent)
                            .getServletContext();
                    break;
                }
                parent = parent.getParent();
            }
            Assert.state(servletContext != null,
                    "Failed to find Root WebApplicationContext in the context hierarchy");
            context.setServletContext(servletContext);
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
