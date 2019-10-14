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
package org.tinygroup.springmerge.beanfactory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.tinygroup.springmerge.BeanDefinitionProxy;

import java.io.IOException;

/**
 * 具有bean配置merge功能的spring容器
 *
 * @author renhui
 */
public class SpringMergeApplicationContext extends XmlWebApplicationContext {
    private BeanDefinitionRegistry mergedBeanDefinitionRegistry;

    public SpringMergeApplicationContext(
            BeanDefinitionRegistry mergedBeanDefinitionRegistry) {
        super();
        this.mergedBeanDefinitionRegistry = mergedBeanDefinitionRegistry;
    }

    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory)
            throws BeansException, IOException {
        super.loadBeanDefinitions(beanFactory);
        // 将不存在的bean定义注入到当前上下文中
        String[] beanDefinitionNames = mergedBeanDefinitionRegistry
                .getBeanDefinitionNames();
        if (beanDefinitionNames != null && beanDefinitionNames.length > 0) {
            logger.info("========================开始合并spring配置 ========================");
            for (String beanName : beanDefinitionNames) {
                BeanDefinition mergedBd = mergedBeanDefinitionRegistry
                        .getBeanDefinition(beanName);
                BeanDefinition currentBd;
                currentBd = getCurrentBdIfExisted(beanFactory, beanName);
                if (currentBd != null) {
                    logger.info(beanName + "在当前spring中存在,需要替换");
                    currentBd = new BeanDefinitionProxy(mergedBd, currentBd);
                } else {
                    logger.info(beanName + "在当前spring中不存在，直接注册到当前上下文");
                    currentBd = mergedBd;
                }
                beanFactory.registerBeanDefinition(beanName, currentBd);
            }
            logger.info("========================合并spring配置结束 ========================");
        }
    }

    /**
     * 递归，包括父容器中的 运行时公共组件的beanDefinition，也可以被合并覆盖。
     *
     * @param currentBeanFactory
     * @param beanName
     * @return
     */
    private BeanDefinition getCurrentBdIfExisted(
            DefaultListableBeanFactory currentBeanFactory, String beanName) {
        if (currentBeanFactory != null) {
            if (!currentBeanFactory.containsBeanDefinition(beanName)) {
                BeanFactory beanFactory = currentBeanFactory
                        .getParentBeanFactory();
                // 取得父容器，运行时公共组件的bd
                if (beanFactory instanceof DefaultListableBeanFactory) {
                    return getCurrentBdIfExisted(
                            (DefaultListableBeanFactory) beanFactory, beanName);
                }
                return null;
            } else {
                return currentBeanFactory.getMergedBeanDefinition(beanName);
            }
        }
        return null;
    }

}
