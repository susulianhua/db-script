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
package org.tinygroup.springutil;

import org.springframework.beans.factory.FactoryBean;
import org.tinygroup.beancontainer.BeanContainerFactory;

/**
 * 通过FactoryBean方式获取tiny容器加载的bean对象
 *
 * @author renhui
 */
public class TinyBeanFactoryBean implements FactoryBean {

    private Class objectType;

    private boolean isSingleton = true;

    public Object getObject() throws Exception {
        return BeanContainerFactory.getBeanContainer(
                getClass().getClassLoader()).getBean(objectType);
    }

    public Class getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        try {
            this.objectType = Class.forName(objectType);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isSingleton() {
        return isSingleton;
    }

    public void setSingleton(boolean isSingleton) {
        this.isSingleton = isSingleton;
    }
}
