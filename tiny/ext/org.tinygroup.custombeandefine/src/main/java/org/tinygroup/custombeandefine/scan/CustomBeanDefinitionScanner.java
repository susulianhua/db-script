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
package org.tinygroup.custombeandefine.scan;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.tinygroup.custombeandefine.BeanDefineCreate;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

public class CustomBeanDefinitionScanner extends
        ClassPathBeanDefinitionScanner {

    protected static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
    private BeanDefineCreate beanDefineCreate;

    public CustomBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public CustomBeanDefinitionScanner(BeanDefinitionRegistry registry,
                                       boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
    }

    public BeanDefineCreate getBeanDefineCreate() {
        return beanDefineCreate;
    }

    public void setBeanDefineCreate(BeanDefineCreate beanDefineCreate) {
        this.beanDefineCreate = beanDefineCreate;
    }

    @Override
    public Set<BeanDefinition> findCandidateComponents(String basePackage) {
        Set<BeanDefinition> candidates = new LinkedHashSet<BeanDefinition>();
        try {
            String resourcePattern = DEFAULT_RESOURCE_PATTERN;
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                    + resolveBasePackage(basePackage) + "/" + resourcePattern;
            ResourcePatternResolver resourcePatternResolver = (ResourcePatternResolver) getResourceLoader();
            MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(
                    resourcePatternResolver);
            Resource[] resources = resourcePatternResolver
                    .getResources(packageSearchPath);
            boolean traceEnabled = logger.isTraceEnabled();
            boolean debugEnabled = logger.isDebugEnabled();
            for (int i = 0; i < resources.length; i++) {
                Resource resource = resources[i];
                if (traceEnabled) {
                    logger.trace("Scanning " + resource);
                }
                if (resource.isReadable()) {
                    MetadataReader metadataReader = metadataReaderFactory
                            .getMetadataReader(resource);
                    if (isCandidateComponent(metadataReader)) {
                        ScannedGenericBeanDefinition sbd = (ScannedGenericBeanDefinition) beanDefineCreate.createBeanDefinition(metadataReader);
                        if (sbd != null) {
                            sbd.setResource(resource);
                            sbd.setSource(resource);
                            if (debugEnabled) {
                                logger.debug("Identified candidate component class: "
                                        + resource);
                            }
                            candidates.add(sbd);
                        }
                    } else {
                        if (traceEnabled) {
                            logger.trace("Ignored because not matching any filter: "
                                    + resource);
                        }
                    }
                } else {
                    if (traceEnabled) {
                        logger.trace("Ignored because not readable: "
                                + resource);
                    }
                }
            }
        } catch (IOException ex) {
            throw new BeanDefinitionStoreException(
                    "I/O failure during classpath scanning", ex);
        }
        return candidates;
    }

}
