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
package org.tinygroup.service.annotation;

import org.tinygroup.annotation.AnnotationClassAction;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.service.loader.AnnotationServiceLoader;
import org.tinygroup.service.registry.ServiceRegistry;

import java.lang.annotation.Annotation;

/**
 * 符合@ServiceComponent注解配置的处理器
 *
 * @param <T>
 * @author renhui
 */
public class ServiceComponentClassAction implements AnnotationClassAction {
    private AnnotationServiceLoader annotationLoader;

    public <T> void process(Class<T> clazz, Annotation annotation) {
        if (annotationLoader != null) {
            ServiceRegistry reg = BeanContainerFactory.getBeanContainer(
                    this.getClass().getClassLoader()).getBean(
                    ServiceRegistry.BEAN_NAME);
            annotationLoader.loadService(clazz, annotation, reg);
        }
    }

    public AnnotationServiceLoader getAnnotationLoader() {
        return annotationLoader;
    }

    public void setAnnotationLoader(AnnotationServiceLoader annotationLoader) {
        this.annotationLoader = annotationLoader;
    }

}
