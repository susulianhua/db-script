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
package org.tinygroup.custombeandefine.impl;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.tinygroup.custombeandefine.BeanDefineCreate;

import java.util.List;

public class ProxyFactoryBeanDefineCreate implements BeanDefineCreate {

    private static final String PROXY_FACTORY_BEAN = "org.springframework.aop.framework.ProxyFactoryBean";
    private List<String> interceptorNames;
    private boolean primary = true;

    public List<String> getInterceptorNames() {
        return interceptorNames;
    }

    public void setInterceptorNames(List<String> interceptorNames) {
        this.interceptorNames = interceptorNames;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public BeanDefinition createBeanDefinition(MetadataReader metadataReader) {
        ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(
                metadataReader);
        sbd.setBeanClassName(PROXY_FACTORY_BEAN);
        sbd.setPrimary(primary);
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        if (classMetadata.isInterface()) {
            propertyValues.addPropertyValue("proxyInterfaces",
                    classMetadata.getClassName());
            propertyValues.addPropertyValue("interceptorNames",
                    interceptorNames);
            sbd.setPropertyValues(propertyValues);
            return sbd;
        }
        return null;
    }

}
