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
package org.tinygroup.custombeandefine.convention;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionDefaults;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationScopeMetadataResolver;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.custombeandefine.BeanDefineCreate;
import org.tinygroup.custombeandefine.identifier.ConventionComponentIdentifier;
import org.tinygroup.custombeandefine.scan.CustomBeanDefinitionScanner;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.springutil.PathMatchingInJarResourcePatternResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 自动注册符合合约规范的类到容器中
 *
 * @author renhui
 */
public class ConventionCustomBeanDefinitionRegistryPostProcessor extends
        ApplicationObjectSupport implements BeanFactoryPostProcessor,
        InitializingBean {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ConventionCustomBeanDefinitionRegistryPostProcessor.class);

    private List<ConventionComponentIdentifier> conventionComponentIdentifierComposite;

    private BeanDefineCreate beanDefineCreate;

    private BeanNameGenerator beanNameGenerator = new DefaultBeanNameGenerator();

    public BeanDefineCreate getBeanDefineCreate() {
        return beanDefineCreate;
    }

    public void setBeanDefineCreate(BeanDefineCreate beanDefineCreate) {
        this.beanDefineCreate = beanDefineCreate;
    }

    public BeanNameGenerator getBeanNameGenerator() {
        return beanNameGenerator;
    }

    public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator) {
        this.beanNameGenerator = beanNameGenerator;
    }

    public void setConventionComponentIdentifierComposite(
            List<ConventionComponentIdentifier> conventionComponentIdentifierComposite) {
        this.conventionComponentIdentifierComposite = conventionComponentIdentifierComposite;
    }


    public void postProcessBeanFactory(
            ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if (beanFactory instanceof BeanDefinitionRegistry) {
            postProcessBeanDefinitionRegistry((BeanDefinitionRegistry) beanFactory);
        }
    }

    public void postProcessBeanDefinitionRegistry(
            BeanDefinitionRegistry registry) throws BeansException {
        List<ConventionComponentIdentifier> identifiers = conventionComponentIdentifierComposite;
        if (CollectionUtil.isEmpty(identifiers)) {
            return;
        }
        List<String> patterns = new ArrayList<String>();
        for (ConventionComponentIdentifier identifier : identifiers) {
            patterns.addAll(identifier.getPackagePatterns());
        }
        if (CollectionUtil.isEmpty(patterns)) {
            return;
        }
        // 扫描，排重，加入约定扫入的beanDefinition
        String[] patternArray = new String[patterns.size()];
        long start = System.currentTimeMillis();
        int count = this.createScanner(registry).scan(
                patterns.toArray(patternArray));
        LOGGER.logMessage(
                LogLevel.INFO,
                "detect {0} conventional components from  project  duration: {1}ms",
                count, (System.currentTimeMillis() - start));
    }

    protected ClassPathBeanDefinitionScanner createScanner(
            BeanDefinitionRegistry registry) {
        CustomBeanDefinitionScanner scaner = new CustomBeanDefinitionScanner(
                registry, false);
        scaner.setBeanDefineCreate(beanDefineCreate);
        BeanDefinitionDefaults defaults = new BeanDefinitionDefaults();
        defaults.setAutowireMode(Autowire.BY_NAME.value());
        scaner.setBeanDefinitionDefaults(defaults);
        scaner.setScopeMetadataResolver(new AnnotationScopeMetadataResolver());
        scaner.setResourceLoader(new PathMatchingInJarResourcePatternResolver());
        scaner.setBeanNameGenerator(beanNameGenerator);
        addTypeFilters(scaner);
        return scaner;
    }

    private void addTypeFilters(ClassPathBeanDefinitionScanner scaner) {
        List<ConventionComponentIdentifier> identifiers = conventionComponentIdentifierComposite;
        if (CollectionUtil.isEmpty(identifiers)) {
            return;
        }
        for (final ConventionComponentIdentifier identifier : identifiers) {
            scaner.addIncludeFilter(new TypeFilter() {

                public boolean match(MetadataReader metadataReader,
                                     MetadataReaderFactory metadataReaderFactory)
                        throws IOException {
                    return identifier.isComponent(metadataReader
                            .getClassMetadata().getClassName());
                }

            });

        }

    }

    public void afterPropertiesSet() throws Exception {
        if (CollectionUtil.isEmpty(conventionComponentIdentifierComposite)) {
            Map<String, ConventionComponentIdentifier> map = BeanFactoryUtils
                    .beansOfTypeIncludingAncestors(
                            this.getListableBeanFactory(),
                            ConventionComponentIdentifier.class,true,false);
            conventionComponentIdentifierComposite = new ArrayList<ConventionComponentIdentifier>();
            conventionComponentIdentifierComposite.addAll(map.values());
        }

    }

    private ListableBeanFactory getListableBeanFactory() {
        if (getApplicationContext() instanceof ConfigurableApplicationContext) {
            return ((ConfigurableApplicationContext) getApplicationContext())
                    .getBeanFactory();
        }
        return getApplicationContext();
    }

}
