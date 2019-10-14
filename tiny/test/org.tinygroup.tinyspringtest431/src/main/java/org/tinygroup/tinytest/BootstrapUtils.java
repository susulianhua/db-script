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
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.test.context.BootstrapContext;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.CacheAwareContextLoaderDelegate;
import org.springframework.test.context.TestContextBootstrapper;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Constructor;
import java.util.Set;

/**
 * {@code BootstrapUtils} is a collection of utility methods to assist with
 * bootstrapping the <em>Spring TestContext Framework</em>.
 *
 * @author Sam Brannen
 * @author Phillip Webb
 * @see BootstrapWith
 * @see BootstrapContext
 * @see TestContextBootstrapper
 * @since 4.1
 */
abstract class BootstrapUtils {

    private static final String DEFAULT_BOOTSTRAP_CONTEXT_CLASS_NAME =
            "org.springframework.test.context.support.DefaultBootstrapContext";

//	private static final String DEFAULT_CACHE_AWARE_CONTEXT_LOADER_DELEGATE_CLASS_NAME =
//			"org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate";

    private static final String DEFAULT_TINY_CACHE_AWARE_CONTEXT_LOADER_DELEGATE_CLASS_NAME =
            "org.tinygroup.tinytest.TinyCacheAwareContextLoaderDelegate";

    private static final String DEFAULT_TEST_CONTEXT_BOOTSTRAPPER_CLASS_NAME =
            "org.tinygroup.tinytest.TinyDefaultTestContextBootstrapper";

    private static final String DEFAULT_WEB_TEST_CONTEXT_BOOTSTRAPPER_CLASS_NAME =
            "org.tinygroup.tinytest.TinyWebTestContextBootstrapper";

    private static final String WEB_APP_CONFIGURATION_ANNOTATION_CLASS_NAME =
            "org.springframework.test.context.web.WebAppConfiguration";

    private static final Log logger = LogFactory.getLog(BootstrapUtils.class);


    /**
     * Create the {@code BootstrapContext} for the specified {@linkplain Class test class}.
     * <p>Uses reflection to create a {@link org.springframework.test.context.support.DefaultBootstrapContext}
     * that uses a {@link org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate}.
     *
     * @param testClass the test class for which the bootstrap context should be created
     * @return a new {@code BootstrapContext}; never {@code null}
     */
    @SuppressWarnings("unchecked")
    static BootstrapContext createBootstrapContext(Class<?> testClass) {
        CacheAwareContextLoaderDelegate cacheAwareContextLoaderDelegate = createCacheAwareContextLoaderDelegate();
        Class<? extends BootstrapContext> clazz = null;
        try {
            clazz = (Class<? extends BootstrapContext>) ClassUtils.forName(
                    DEFAULT_BOOTSTRAP_CONTEXT_CLASS_NAME, BootstrapUtils.class.getClassLoader());
            Constructor<? extends BootstrapContext> constructor = clazz.getConstructor(
                    Class.class, CacheAwareContextLoaderDelegate.class);
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("Instantiating BootstrapContext using constructor [%s]", constructor));
            }
            return BeanUtils.instantiateClass(constructor, testClass, cacheAwareContextLoaderDelegate);
        } catch (Throwable ex) {
            throw new IllegalStateException("Could not load BootstrapContext [" + clazz + "]", ex);
        }
    }

    @SuppressWarnings("unchecked")
    private static CacheAwareContextLoaderDelegate createCacheAwareContextLoaderDelegate() {
        Class<? extends CacheAwareContextLoaderDelegate> clazz = null;
        try {
            clazz = (Class<? extends CacheAwareContextLoaderDelegate>) ClassUtils.forName(
                    DEFAULT_TINY_CACHE_AWARE_CONTEXT_LOADER_DELEGATE_CLASS_NAME, BootstrapUtils.class.getClassLoader());

            if (logger.isDebugEnabled()) {
                logger.debug(String.format("Instantiating CacheAwareContextLoaderDelegate from class [%s]",
                        clazz.getName()));
            }
            return BeanUtils.instantiateClass(clazz, CacheAwareContextLoaderDelegate.class);
        } catch (Throwable ex) {
            throw new IllegalStateException("Could not load CacheAwareContextLoaderDelegate [" + clazz + "]", ex);
        }
    }

    /**
     * Resolve the {@link TestContextBootstrapper} type for the test class in the
     * supplied {@link BootstrapContext}, instantiate it, and provide it a reference
     * to the {@link BootstrapContext}.
     * <p>If the {@link BootstrapWith @BootstrapWith} annotation is present on
     * the test class, either directly or as a meta-annotation, then its
     * {@link BootstrapWith#value value} will be used as the bootstrapper type.
     * Otherwise, either the
     * {@link org.springframework.test.context.support.DefaultTestContextBootstrapper
     * DefaultTestContextBootstrapper} or the
     * {@link org.springframework.test.context.web.WebTestContextBootstrapper
     * WebTestContextBootstrapper} will be used, depending on the presence of
     * {@link org.springframework.test.context.web.WebAppConfiguration @WebAppConfiguration}.
     *
     * @param bootstrapContext the bootstrap context to use
     * @return a fully configured {@code TestContextBootstrapper}
     */
    static TestContextBootstrapper resolveTestContextBootstrapper(BootstrapContext bootstrapContext) {
        Class<?> testClass = bootstrapContext.getTestClass();

        Class<?> clazz = null;
        try {
            clazz = resolveExplicitTestContextBootstrapper(testClass);
            if (clazz == null) {
                clazz = resolveDefaultTestContextBootstrapper(testClass);
            }
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("Instantiating TestContextBootstrapper for test class [%s] from class [%s]",
                        testClass.getName(), clazz.getName()));
            }
            TestContextBootstrapper testContextBootstrapper =
                    BeanUtils.instantiateClass(clazz, TestContextBootstrapper.class);
            testContextBootstrapper.setBootstrapContext(bootstrapContext);
            return testContextBootstrapper;
        } catch (IllegalStateException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new IllegalStateException("Could not load TestContextBootstrapper [" + clazz +
                    "]. Specify @BootstrapWith's 'value' attribute or make the default bootstrapper class available.",
                    ex);
        }
    }

    private static Class<?> resolveExplicitTestContextBootstrapper(Class<?> testClass) {
        Set<BootstrapWith> annotations = AnnotatedElementUtils.findAllMergedAnnotations(testClass, BootstrapWith.class);
        if (annotations.size() < 1) {
            return null;
        }
        if (annotations.size() > 1) {
            throw new IllegalStateException(String.format(
                    "Configuration error: found multiple declarations of @BootstrapWith for test class [%s]: %s",
                    testClass.getName(), annotations));
        }
        return annotations.iterator().next().value();
    }

    private static Class<?> resolveDefaultTestContextBootstrapper(Class<?> testClass) throws Exception {
        ClassLoader classLoader = BootstrapUtils.class.getClassLoader();
        AnnotationAttributes attributes = AnnotatedElementUtils.findMergedAnnotationAttributes(testClass,
                WEB_APP_CONFIGURATION_ANNOTATION_CLASS_NAME, false, false);
        if (attributes != null) {
            return ClassUtils.forName(DEFAULT_WEB_TEST_CONTEXT_BOOTSTRAPPER_CLASS_NAME, classLoader);
        }
        return ClassUtils.forName(DEFAULT_TEST_CONTEXT_BOOTSTRAPPER_CLASS_NAME, classLoader);
    }

}
