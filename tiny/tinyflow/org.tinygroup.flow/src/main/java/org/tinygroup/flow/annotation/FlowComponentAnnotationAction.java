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
package org.tinygroup.flow.annotation;

import org.tinygroup.annotation.AnnotationClassAction;
import org.tinygroup.annotation.AnnotationPropertyAction;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.tools.AnnotationUtils;
import org.tinygroup.event.Parameter;
import org.tinygroup.flow.FlowExecutor;
import org.tinygroup.flow.annotation.config.ComponentDefine;
import org.tinygroup.flow.annotation.config.ComponentParameter;
import org.tinygroup.flow.annotation.config.ComponentResult;
import org.tinygroup.flow.annotation.config.ComponentType;
import org.tinygroup.flow.config.Result;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 流程组件注解处理器
 *
 * @author renhui
 */
public class FlowComponentAnnotationAction implements AnnotationClassAction,
        AnnotationPropertyAction {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(FlowComponentAnnotationAction.class);
    private FlowExecutor executor;
    private FlowExecutor pageflowExecutor;

    public <T> void process(Class<T> clazz, Annotation annotation) {
        if (executor == null) {
            executor = BeanContainerFactory.getBeanContainer(
                    this.getClass().getClassLoader()).getBean(
                    FlowExecutor.FLOW_BEAN);
        }
        if (pageflowExecutor == null) {
            pageflowExecutor = BeanContainerFactory.getBeanContainer(
                    this.getClass().getClassLoader()).getBean(
                    FlowExecutor.PAGE_FLOW_BEAN);
        }
        ComponentDefine annoDefine = AnnotationUtils.findAnnotation(clazz,
                ComponentDefine.class);
        org.tinygroup.flow.config.ComponentDefine componentDefine = new org.tinygroup.flow.config.ComponentDefine();
        setBasicProperty(annoDefine, componentDefine);
        ComponentType type = annoDefine.type();
        if (type.equals(ComponentType.BOTH)) {
            executor.addComponent(componentDefine);
            pageflowExecutor.addComponent(componentDefine);
        } else if (type.equals(ComponentType.FLOW)) {
            executor.addComponent(componentDefine);
        } else if (type.equals(ComponentType.PAGEFLOW)) {
            pageflowExecutor.addComponent(componentDefine);
        }
    }

    private void setBasicProperty(ComponentDefine annoDefine,
                                  org.tinygroup.flow.config.ComponentDefine componentDefine) {
        componentDefine.setName(annoDefine.name());
        componentDefine.setBean(annoDefine.bean());
        componentDefine.setCategory(annoDefine.category());
        componentDefine.setIcon(annoDefine.icon());
        componentDefine.setShortDescription(annoDefine.shortDescription());
        componentDefine.setLongDescription(annoDefine.longDescription());
        componentDefine.setTitle(annoDefine.title());
    }

    public <T> void process(Class<T> clazz, Field field, Annotation annotation) {
        ComponentDefine annoDefine = AnnotationUtils.findAnnotation(clazz,
                ComponentDefine.class);
        String componentName = annoDefine.name();
        org.tinygroup.flow.config.ComponentDefine componentDefine = executor
                .getComponentDefine(componentName);
        if (componentDefine != null) {
            propertiesProcess(componentDefine, annotation);
        }
        componentDefine = pageflowExecutor.getComponentDefine(componentName);
        if (componentDefine != null) {
            propertiesProcess(componentDefine, annotation);
        }
        if (componentDefine == null) {
            LOGGER.logMessage(LogLevel.WARN, "不存在组件名称为：[{0}]的组件信息",
                    componentName);
        }
    }

    public void propertiesProcess(
            org.tinygroup.flow.config.ComponentDefine componentDefine,
            Annotation annotation) {
        if (annotation.annotationType().isAssignableFrom(
                ComponentParameter.class)) {
            ComponentParameter annoParameter = (ComponentParameter) annotation;
            Parameter parameter = createParameter(annoParameter);
            componentDefine.addParamter(parameter);
        } else if (annotation.annotationType().isAssignableFrom(
                ComponentResult.class)) {
            ComponentResult componentResult = (ComponentResult) annotation;
            Result result = createResult(componentResult);
            componentDefine.addResult(result);
        }
    }

    private Result createResult(ComponentResult componentResult) {
        Result result = new Result();
        result.setArray(componentResult.array());
        result.setCollectionType(componentResult.collectionType());
        result.setDescription(componentResult.description());
        result.setName(componentResult.name());
        result.setRequired(componentResult.required());
        result.setTitle(componentResult.title());
        result.setType(componentResult.type());
        return result;
    }

    private Parameter createParameter(ComponentParameter annoParameter) {
        Parameter parameter = new Parameter();
        parameter.setArray(annoParameter.array());
        parameter.setCollectionType(annoParameter.collectionType());
        parameter.setDescription(annoParameter.description());
        parameter.setName(annoParameter.name());
        parameter.setRequired(annoParameter.required());
        parameter.setScope(annoParameter.scope());
        parameter.setTitle(annoParameter.title());
        parameter.setType(annoParameter.type());
        parameter.setValidatorSence(annoParameter.validatorScene());
        return parameter;
    }

}
