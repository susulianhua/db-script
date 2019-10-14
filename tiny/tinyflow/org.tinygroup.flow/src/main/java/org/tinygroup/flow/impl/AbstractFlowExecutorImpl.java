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
package org.tinygroup.flow.impl;

import org.apache.commons.beanutils.PropertyUtils;
import org.tinygroup.commons.match.SimpleTypeMatcher;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.commons.tools.ValueUtil;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.context2object.util.Context2ObjectUtil;
import org.tinygroup.event.Parameter;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flow.config.Flow;
import org.tinygroup.flow.config.FlowProperty;
import org.tinygroup.flow.config.Node;
import org.tinygroup.flow.exception.FlowRuntimeException;
import org.tinygroup.flow.exception.errorcode.FlowExceptionErrorCode;
import org.tinygroup.flow.util.FlowElUtil;
import org.tinygroup.format.Formater;
import org.tinygroup.format.impl.ContextFormater;
import org.tinygroup.format.impl.FormaterImpl;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.util.List;
import java.util.Map;

public abstract class AbstractFlowExecutorImpl extends FlowManagerImpl {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractFlowExecutorImpl.class);
    private static transient Formater formater = new FormaterImpl();

    static {
        formater.addFormatProvider("", new ContextFormater());
        formater.addFormatProvider(Parameter.INPUT, new ContextFormater());
        formater.addFormatProvider(Parameter.OUTPUT, new ContextFormater());
        formater.addFormatProvider(Parameter.BOTH, new ContextFormater());
    }

    private boolean change;

    protected void logContext(Context context) {
        if (LOGGER.isEnabled(LogLevel.DEBUG)) {
            LOGGER.logMessage(LogLevel.DEBUG, "环境内容开始：");
            logItemMap(context.getItemMap());
            logSubContext(context.getSubContextMap());
            LOGGER.logMessage(LogLevel.DEBUG, "环境内容结束");
        }
    }

    private void logSubContext(Map<String, Context> subContextMap) {
        LOGGER.logMessage(LogLevel.DEBUG, "子环境的内容开始：");
        if (subContextMap != null) {
            for (String key : subContextMap.keySet()) {
                logContext(subContextMap.get(key));
            }
        }
        LOGGER.logMessage(LogLevel.DEBUG, "子环境的内容结束：");
    }

    private void logItemMap(Map<String, Object> itemMap) {
        for (String key : itemMap.keySet()) {
            LOGGER.logMessage(LogLevel.DEBUG, "key: {0}, value: {1}", key,
                    itemMap.get(key));
        }
    }

    /**
     * 获取一个新环境
     *
     * @param flow
     * @param context
     * @return
     */
    protected Context getNewContext(Flow flow, Context context) {
        Context flowContext = null;
        if (context == null) {
            return null;
        }
        flowContext = context.getSubContextMap().get(flow.getId());
        if (flowContext == null) {
            return getNewContext(flow, context.getParent());
        }
        return flowContext;
    }


    protected void checkInputParameter(Flow flow, Context context) {
        StringBuffer buf = new StringBuffer();
        if (flow.getInputParameters() != null) {
            for (Parameter parameter : flow.getInputParameters()) {
                if (parameter.isRequired()) {// 如果是必须
                    // =============20130619修改begin================
                    // Object value = context.get(parameter.getName());
                    // if (value == null) {//
                    // 如果从上下文直接拿没有拿到，则通过ClassNameObjectGenerator去解析
                    // value = getObjectByGenerator(parameter, context);
                    // if (value != null) {// 如果解析出来不为空，则放入上下文
                    // context.put(parameter.getName(), value);
                    // continue;
                    // }
                    // }
                    if (context.exist(parameter.getName())) {
                        continue;
                    }
                    Object value = Context2ObjectUtil.getObject(parameter,
                            context, this.getClass().getClassLoader());
                    if (value != null) {// 如果解析出来不为空，则放入上下文
                        context.put(parameter.getName(), value);
                        continue;
                    }
                    // =============20130619修改end================
                    if (value == null) {
                        buf.append("参数<");
                        buf.append(parameter.getName());
                        buf.append(">在环境中不存在；");
                    }
                }
            }
            if (buf.length() > 0) {
                // buf.insert(0, "流程<" + flow.getId() + ">需要的参数不足：");
                // throw new FlowRuntimeException(buf.toString());
                LOGGER.logMessage(LogLevel.ERROR, "流程<{0}>需要的参数不足,{1}",
                        flow.getId(), buf.toString());
                throw new FlowRuntimeException(
                        FlowExceptionErrorCode.FLOW_IN_PARAM_NOT_EXIST,
                        flow.getId(), buf.toString());
            }

        }
    }

    protected void checkOutputParameter(Flow flow, Context context) {
        StringBuffer buf = new StringBuffer();
        if (flow.getOutputParameters() != null) {
            for (Parameter parameter : flow.getOutputParameters()) {
                if (parameter.isRequired()) {// 如果是必须
                    if (!context.exist(parameter.getName())) {
                        buf.append("参数<");
                        buf.append(parameter.getName());
                        buf.append(">在环境中不存在；");
                    }
                }
            }
            if (buf.length() > 0) {
                // buf.insert(0, "流程<" + flow.getId() + ">需要输出的参数不足：");
                // throw new FlowRuntimeException(buf.toString());
                LOGGER.logMessage(LogLevel.ERROR, "流程<{0}>需要的输出参数不足,{1}",
                        flow.getId(), buf.toString());
                throw new FlowRuntimeException(
                        FlowExceptionErrorCode.FLOW_OUT_PARAM_NOT_EXIST,
                        flow.getId(), buf.toString());
            }
        }
    }


    /**
     * 把配置的参数注入进去
     *
     * @param node
     * @param componentInstance
     */
    protected void setProperties(Node node, ComponentInterface componentInstance,
                                 Context context) {
        Map<String, FlowProperty> properties = node.getComponent()
                .getPropertyMap();
        if (properties != null) {
            for (String name : properties.keySet()) {
                FlowProperty property = properties.get(name);
                String value = property.getValue();
                Object object = null;
                // 如果是el表达式，则通过el表达式处理
                if (FlowProperty.EL_TYPE.equals(property.getType())) {
                    object = FlowElUtil.execute(value, context, this.getClass()
                            .getClassLoader());
                } else {// 否则采用原有处理方式
                    object = getObject(property.getType(), value, context);
                }

                try {
                    PropertyUtils.setProperty(componentInstance, name, object);
                } catch (Exception e) {
                    throw new FlowRuntimeException(e);
                }
            }
        }
    }

    private Object getObject(String type, String value, Context context) {
        String str = value;
        if (str instanceof String) {
            str = formater.format(context, str);
        }

        // 所有的都不是，说明是对象或表达式,此时返回null
        Object o = null;
        if (str != null) {
            str = str.trim();
            // type为空，按原先逻辑处理
            if (StringUtil.isEmpty(type)) {
                o = SimpleTypeMatcher.matchType(str);
            } else {
                // type不为空，则根据设置的type进行处理。可以避免数值型结果和参数类型不一致的问题。
                o = ValueUtil.getValue(str, type);
            }

        }
        return o;
    }

    public Context getInputContext(Flow flow, Context context) {
        return getContext(flow.getInputParameters(), context);
    }

    private Context getContext(List<Parameter> parameters, Context context) {
        Context result = new ContextImpl();
        if (parameters != null) {
            for (Parameter parameter : parameters) {
                result.put(parameter.getName(),
                        context.get(parameter.getName()));
            }
        }
        return result;
    }

    public Context getOutputContext(Flow flow, Context context) {
        return getContext(flow.getOutputParameters(), context);
    }

    public boolean isChange() {
        return change;
    }

    public void setChange(boolean change) {
        this.change = change;
    }

}


