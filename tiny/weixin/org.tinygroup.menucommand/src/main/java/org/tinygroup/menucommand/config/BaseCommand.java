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
package org.tinygroup.menucommand.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.menucommand.exception.MenuCommandException;

/**
 * 基础命令对象
 *
 * @author yancheng11334
 */
public class BaseCommand extends BaseObject {

    /**
     * 对应的处理类bean名称
     */
    @XStreamAlias("bean-name")
    @XStreamAsAttribute
    private String beanName;

    /**
     * 对应的处理类名称
     */
    @XStreamAlias("class-name")
    @XStreamAsAttribute
    private String className;

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * 创建实例
     *
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T createCommandObject() {
        // 先根据bean查找
        if (!StringUtil.isEmpty(getBeanName())) {
            try {
                return BeanContainerFactory.getBeanContainer(getClass()
                        .getClassLoader()).getBean(getBeanName());
            } catch (Exception e) {
                throw new MenuCommandException(String.format("bean名称为[%s],实例化bean失败",
                        getBeanName()), e);
            }
        }

        // 再根据class查找
        if (!StringUtil.isEmpty(getClassName())) {
            try {
                return (T) getClass().getClassLoader()
                        .loadClass(getClassName()).newInstance();
            } catch (Exception e) {
                throw new MenuCommandException(String.format("类名称为[%s],实例化类失败",
                        getClassName()), e);
            }
        }

        throw new MenuCommandException(String.format("名称为[%s]的BaseCommand没有匹配处理对象,实例化失败!", getName()));
    }
}
