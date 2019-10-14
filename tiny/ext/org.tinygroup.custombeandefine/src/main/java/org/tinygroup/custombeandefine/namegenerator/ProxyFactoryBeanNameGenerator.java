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
package org.tinygroup.custombeandefine.namegenerator;

import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.tinygroup.commons.tools.StringUtil;

/**
 * ProxyFactoryBean 名称生成器
 *
 * @author renhui
 */
public class ProxyFactoryBeanNameGenerator implements BeanNameGenerator {

    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        PropertyValues propertyValues = definition.getPropertyValues();
        String interfaceName = StringUtil.substringAfterLast((String) propertyValues.getPropertyValue("proxyInterfaces").getValue(), ".");
        if (registry.containsBeanDefinition(interfaceName)) {
            throw new BeanDefinitionStoreException("beanName:" + interfaceName + "已经在容器中存在,请确保生成的bean名称唯一");
        }
        return StringUtil.uncapitalize(interfaceName);
    }


}
