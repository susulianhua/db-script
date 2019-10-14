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
package org.tinygroup.annotation.impl;

import org.tinygroup.annotation.AnnotationClassAction;
import org.tinygroup.annotation.AnnotationExecuteManager;
import org.tinygroup.annotation.AnnotationMethodAction;
import org.tinygroup.annotation.AnnotationPropertyAction;
import org.tinygroup.annotation.config.*;
import org.tinygroup.annotation.fileresolver.AnnotationFileProcessor;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.config.Configuration;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.loader.LoaderManager;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xmlparser.node.XmlNode;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 注解执行管理器实现
 *
 * @author luoguo
 */
public class AnnotationExecuteManagerImpl implements AnnotationExecuteManager,
        Configuration {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AnnotationFileProcessor.class);

    private static final int CLASS_SUFFIX_LENGTH = 6;
    private static final String ANNOTATION_NODE_PATH = "/application/annotation-configuration";
    private static final String MAP_VALUE = "value";
    private static final String MAP_ID = "id";
    protected XmlNode applicationConfig;
    protected XmlNode componentConfig;
    // 类匹配定义
    private List<AnnotationClassMatcher> classMatchers = new ArrayList<AnnotationClassMatcher>();

    public AnnotationExecuteManagerImpl() {

    }

    public void addAnnotationClassMatchers(
            AnnotationClassMatchers annotationClassMatchers) {
        annotationClassMatchers.initAnnotationClassMatchers();
        classMatchers.addAll(annotationClassMatchers
                .getAnnotationClassMatcherList());
    }

    public void removeAnnotationClassMatchers(
            AnnotationClassMatchers annotationClassMatchers) {
        classMatchers.removeAll(annotationClassMatchers
                .getAnnotationClassMatcherList());
    }

    /**
     * 判断class文件是否符合注解配置文件
     *
     * @param fileObject
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void processClassFileObject(FileObject fileObject) {

        for (AnnotationClassMatcher classMatcher : classMatchers) {
            String className = getFullClassName(fileObject);
            if (classMatcher.isClassMatch(className)) {
                LOGGER.logMessage(LogLevel.DEBUG, "找到匹配类名正则[{0}]的类:[{1}]",
                        classMatcher.getClassName(), className);
                try {
                    Class clazz = LoaderManager.getClass(className);
                    processClassProcessor(clazz, classMatcher);
                    processPropertyProcessor(clazz,
                            classMatcher.getAnnotationPropertyMatchers());
                    processMethodProcessor(clazz,
                            classMatcher.getAnnotationMethodMatchers());
                } catch (ClassNotFoundException e) {
                    LOGGER.errorMessage("加载器加载的类[{0}]不存在", e, className);
                } catch (Exception e) {
                    LOGGER.errorMessage("加载器加载的类[{0}]时出现错误：[{1}]", e,
                            className, e.getMessage());

                }
            }

        }
    }

    /**
     * class上的注解存在，并执行beanname相关处理
     */
    private <T> void processClassProcessor(Class<T> clazz,
                                           AnnotationClassMatcher classMatcher) {
        Annotation[] annotations = clazz.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (classMatcher.isAnnotationTypeMatch(annotation)) {
                LOGGER.logMessage(LogLevel.DEBUG, "类上存在符合正则[{0}]的注解",
                        classMatcher.getAnnotationType());
                processClassBean(clazz, annotation, new AnnotationMatcherDto(
                        classMatcher));
            }
        }
    }

    /**
     * method上的注解存在，并执行beanname相关处理
     */
    private <T> void processMethodProcessor(Class<T> clazz,
                                            List<AnnotationMethodMatcher> annotationMethodMatchers) {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (AnnotationMethodMatcher annotationMethodMatcher : annotationMethodMatchers) {
            for (Method method : declaredMethods) {
                if (annotationMethodMatcher.isMethodMatch(method.getName())) {
                    LOGGER.logMessage(LogLevel.DEBUG,
                            "找到匹配方法名正则[{0}]的方法:[{1}]",
                            annotationMethodMatcher.getMethodName(),
                            method.getName());
                    Annotation[] annotations = method.getDeclaredAnnotations();
                    for (Annotation annotation : annotations) {
                        if (annotationMethodMatcher
                                .isAnnotationTypeMatch(annotation)) {
                            LOGGER.logMessage(LogLevel.DEBUG,
                                    "方法上存在符合正则[{0}]的注解",
                                    annotationMethodMatcher.getAnnotationType());
                            processMethodBean(clazz, annotation,
                                    new AnnotationMatcherDto(
                                            annotationMethodMatcher, method));

                        }
                    }
                }
            }
        }
    }

    /**
     * property上的注解存在，并执行beanname相关处理
     */
    private <T> void processPropertyProcessor(Class<T> clazz,
                                              List<AnnotationPropertyMatcher> annotationPropertyMatchers) {
        Field[] declaredFields = clazz.getDeclaredFields();
        for (AnnotationPropertyMatcher annotationPropertyMatcher : annotationPropertyMatchers) {
            for (Field field : declaredFields) {
                if (annotationPropertyMatcher.isPropertyMatch(field.getName())) {
                    LOGGER.logMessage(LogLevel.DEBUG,
                            "找到匹配属性名正则[{0}]的属性:[{1}]",
                            annotationPropertyMatcher.getPropertyName(),
                            field.getName());
                    Annotation[] annotations = field.getDeclaredAnnotations();
                    for (Annotation annotation : annotations) {
                        if (annotationPropertyMatcher
                                .isAnnotationTypeMatch(annotation)) {
                            LOGGER.logMessage(LogLevel.DEBUG,
                                    "属性上存在符合正则[{0}]的注解",
                                    annotationPropertyMatcher
                                            .getAnnotationType());
                            processPropertyBean(clazz, annotation,
                                    new AnnotationMatcherDto(
                                            annotationPropertyMatcher, field));
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取类的全路径
     *
     * @param fileObject
     * @return
     */
    private String getFullClassName(FileObject fileObject) {
        String fileName = getFilePath(fileObject);
        if (fileName.startsWith("/")) {
            fileName = fileName.substring(1);
        }
        return fileName.replaceAll("/", ".");
    }

    private String getFilePath(FileObject fileObject) {
        String path = fileObject.getPath();
        return path.substring(0, path.length() - CLASS_SUFFIX_LENGTH);
    }

    private <T> void processClassBean(Class<T> clazz, Annotation annotation,
                                      AnnotationMatcherDto annotationMatcherDto) {
        List<ProcessorBean> processorBeans = annotationMatcherDto
                .getAnnotationClassMatcher().getProcessorBeans();
        for (ProcessorBean processorBean : processorBeans) {
            if (processorBean.getEnable()) {
                AnnotationClassAction classAction = (AnnotationClassAction) newInstance(processorBean);
                if (classAction != null) {
                    classAction.process(clazz, annotation);
                }
            }

        }
    }

    private <T> void processMethodBean(Class<T> clazz, Annotation annotation,
                                       AnnotationMatcherDto annotationMatcherDto) {
        List<ProcessorBean> processorBeans = annotationMatcherDto
                .getAnnotationMethodMatcher().getProcessorBeans();
        for (ProcessorBean processorBean : processorBeans) {
            if (processorBean.getEnable()) {
                AnnotationMethodAction methodAction = (AnnotationMethodAction) newInstance(processorBean);
                if (methodAction != null) {
                    methodAction.process(clazz, annotationMatcherDto.getMethod(),
                            annotation);
                }
            }
        }
    }

    private Object newInstance(ProcessorBean processorBean) {
        String beanName = processorBean.getName();
        if (!StringUtil.isBlank(beanName)) {
            return BeanContainerFactory
                    .getBeanContainer(this.getClass().getClassLoader())
                    .getBean(beanName);
        }
        try {
            Object instance = Class.forName(processorBean.getType()).newInstance();
            return instance;
        } catch (Exception e) {
            LOGGER.errorMessage("type:[{0}]实例化出现异常", e, processorBean.getType());
        }
        return null;
    }

    private <T> void processPropertyBean(Class<T> clazz, Annotation annotation,
                                         AnnotationMatcherDto annotationMatcherDto) {
        List<ProcessorBean> processorBeans = annotationMatcherDto
                .getAnnotationPropertyMatcher().getProcessorBeans();
        for (ProcessorBean processorBean : processorBeans) {
            if (processorBean.getEnable()) {
                AnnotationPropertyAction propertyAction = (AnnotationPropertyAction) newInstance(processorBean);
                if (propertyAction != null) {
                    propertyAction.process(clazz, annotationMatcherDto.getField(),
                            annotation);
                }
            }
        }
    }

    public void config(XmlNode applicationConfig, XmlNode componentConfig) {
        this.applicationConfig = applicationConfig;
        this.componentConfig = componentConfig;
        List<XmlNode> combineNodes = ConfigurationUtil.combineSubList(
                applicationConfig, componentConfig);
        for (XmlNode xmlNode : combineNodes) {
            AnnotationClassMap.putAnnotationMap(xmlNode.getAttribute(MAP_ID),
                    xmlNode.getAttribute(MAP_VALUE));
        }
    }

    public XmlNode getComponentConfig() {
        return componentConfig;
    }

    public XmlNode getApplicationConfig() {
        return applicationConfig;
    }

    public String getApplicationNodePath() {
        return ANNOTATION_NODE_PATH;
    }

    public String getComponentConfigPath() {
        return "/annotation.config.xml";
    }

}
