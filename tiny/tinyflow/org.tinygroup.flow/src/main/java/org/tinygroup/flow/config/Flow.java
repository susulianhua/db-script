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
package org.tinygroup.flow.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.apache.commons.lang.StringUtils;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.event.Parameter;
import org.tinygroup.flow.FlowExecutor;
import org.tinygroup.flow.exception.FlowRuntimeException;
import org.tinygroup.flow.exception.errorcode.FlowExceptionErrorCode;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程，如果节点的名称叫eAxception，则表示是整个流程的异常处理节点，里面只能添加异常类的nextNode
 * 在执行时，如果节点没有处理，则异常会由本部分进行处理，本部分没有处理，则由异常管理器进行处理。
 *
 * @author luoguo
 */

@XStreamAlias("flow")
public class Flow implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -7228372373320970405L;
    private static Logger LOGGER = LoggerFactory.getLogger(Flow.class);
    @XStreamAsAttribute
    @XStreamAlias("extend-flow-id")
    private String extendFlowId;// 继承的flowID
    @XStreamAsAttribute
    private String id;// flow的唯一ID
    @XStreamAsAttribute
    private String name;// flow的名字
    @XStreamAsAttribute
    private String title;// 流程名称
    private String description;// 流程说明
    private List<Node> nodes;// 流程节点
    private transient Map<String, Node> nodeMap;
    private transient FlowExecutor flowExecutor;
    private transient boolean assembled = false;// 是否已经组装完毕
    @XStreamAsAttribute
    @XStreamAlias("private-context")
    private boolean privateContext = false;
    @XStreamAsAttribute
    private boolean enable;// 是否可用
    @XStreamAsAttribute
    private String category;
    @XStreamAlias("parameters")
    private List<Parameter> parameters;// 流程的参数

    @SuppressWarnings("rawtypes")
    @XStreamOmitField
    private List positions;

    @SuppressWarnings("rawtypes")
    public List getPositions() {
        return positions;
    }

    @SuppressWarnings("rawtypes")
    public void setPositions(List positions) {
        this.positions = positions;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Parameter> getInputParameters() {
        if (parameters == null) {
            return null;
        }
        List<Parameter> result = new ArrayList<Parameter>();
        for (Parameter parameter : parameters) {
            if (parameter.getScope() == null
                    || parameter.getScope().equalsIgnoreCase(Parameter.BOTH)
                    || parameter.getScope().equalsIgnoreCase(Parameter.INPUT)) {
                result.add(parameter);
            }
        }
        return result;
    }

    public List<Parameter> getOutputParameters() {
        if (parameters == null) {
            return parameters;
        }
        List<Parameter> result = new ArrayList<Parameter>();
        for (Parameter parameter : parameters) {
            if (parameter.getScope() == null
                    || parameter.getScope().equalsIgnoreCase(Parameter.BOTH)
                    || parameter.getScope().equalsIgnoreCase(Parameter.OUTPUT)) {
                result.add(parameter);
            }
        }
        return result;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isPrivateContext() {
        return privateContext;
    }

    public void setPrivateContext(boolean privateContext) {
        this.privateContext = privateContext;
    }

    public void validate() {
        for (Node node : nodes) {
            // 节点名称非空校验
            validateNode(node);
            // 流程节点有挂组件，则校验组件必传的参数是否有赋值
            if (node.getComponent() != null
                    && !FlowExecutor.EXCEPTION_DEAL_NODE.equals(node.getName())) {
                validateParameter(node);
            }
        }
    }

    private void validateNode(Node node) {
        if (StringUtils.isBlank(node.getName())) {
            LOGGER.logMessage(LogLevel.ERROR, "流程：{0}的节点：{1}的名称不能为空", id,
                    node.getId());
            throw new FlowRuntimeException(
                    FlowExceptionErrorCode.FLOW_NODE_NAME_VALIDATE_EXCEPTION,
                    id, node.getId());
        }
    }

    private void validateParameter(Node node) {
        if (!StringUtil.isBlank(node.getSubFlowId())) {
            return;
        }
        if (StringUtil.isBlank(node.getComponent().getName())) {
            LOGGER.logMessage(LogLevel.ERROR, "流程:{0}的节点：{1}的组件名称为空", id,
                    node.getId());
            throw new FlowRuntimeException(
                    FlowExceptionErrorCode.FLOW_NODE_COMPONENT_NAME_IS_NULL,
                    id, node.getId());
        }
        if (flowExecutor.getComponentDefine(node.getComponent().getName()) == null) {
            LOGGER.logMessage(LogLevel.ERROR, "流程:{0}的节点：{1}的组件未找到", id,
                    node.getId());
            throw new FlowRuntimeException(
                    FlowExceptionErrorCode.FLOW_NODE_COMPONENT_NAME_IS_NULL,
                    id, node.getId());
        }
        List<Parameter> componentParameters = flowExecutor.getComponentDefine(
                node.getComponent().getName()).getParameters();
        for (Parameter p : componentParameters) {
            if (p.isRequired()) {
                List<FlowProperty> flowProperties = node.getComponent()
                        .getProperties();
                if (flowProperties.isEmpty()) {
                    LOGGER.logMessage(LogLevel.ERROR,
                            "流程：{0}的节点：{1}所需要的参数：{2}校验不通过", id, node.getId(),
                            name);
                    throw new FlowRuntimeException(
                            FlowExceptionErrorCode.FLOW_PROPERTY_VALIDATE_EXCEPTION,
                            id, node.getId(), name);
                } else {
                    compareParameter(flowProperties, p.getName(), node.getId());
                }
            }
        }
    }

    private void compareParameter(List<FlowProperty> flowProperties,
                                  String name, String nodeId) {
        for (FlowProperty fp : flowProperties) {
            if (name.equals(fp.getName()) && StringUtil.isBlank(fp.getValue())) {
                LOGGER.logMessage(LogLevel.ERROR,
                        "流程：{0}的节点：{1}所需要的参数：{2}校验不通过", id, nodeId, name);
                throw new FlowRuntimeException(
                        FlowExceptionErrorCode.FLOW_PROPERTY_VALIDATE_EXCEPTION,
                        id, nodeId, name);
            }
        }
    }

    public void assemble() {
        if (assembled) {
            return;
        }
        if (extendFlowId != null) {
            Flow parentFlow = flowExecutor.getFlowIdMap().get(extendFlowId);
            if (parentFlow == null) {
                LOGGER.logMessage(LogLevel.ERROR, "流程{0}不存在", extendFlowId);
                throw new FlowRuntimeException(
                        FlowExceptionErrorCode.FLOW_NOT_EXIST, extendFlowId);
            } else {
                parentFlow.assemble();
                copyFlow(parentFlow);
            }
        }
        nodeMap = new HashMap<String, Node>();
        for (Node node : nodes) {
            if (node.getComponent() != null) {
                node.getComponent().getPropertyMap();
            }
            nodeMap.put(node.getId(), node);
            List<NextNode> nextNodes = node.getNextNodes();
            if (nextNodes != null && nextNodes.size() > 1) {
                NextNode nullElNode = null;
                for (NextNode nextNode : nextNodes) {
                    // 如果表达式为空或者未配，则放最后执行
                    if (StringUtil.isBlank(nextNode.getEl())) {
                        nullElNode = nextNode;
                        break;
                    }
                }
                if (nullElNode != null) {
                    int index = nextNodes.indexOf(nullElNode);
                    nextNodes.remove(index);
                    nextNodes.add(nextNodes.size(), nullElNode);
                }
            }
        }
        assembled = true;
    }

    private void copyFlow(Flow parentFlow) {
        List<Node> tmpNodes = nodes;
        nodes = new ArrayList<Node>();
        nodes.addAll(parentFlow.getNodes());// 首先把父亲所有节点复制过来
        for (Node node : tmpNodes) {
            Node parentNode = parentFlow.getNodeMap().get(node.getId());
            if (parentNode == null) {
                // 如果父节点在子节点中不存在，则直接拿过来
//				getNodeMap().put(node.getId(), node);
                nodes.add(node);
            } else {
                // 否则进行合并
                int index = nodes.indexOf(parentNode);
                nodes.remove(index);
                node.combine(parentNode);
                nodes.add(index, node);
            }
        }
    }

    public FlowExecutor getFlowExecutor() {
        return flowExecutor;
    }

    public void setFlowExecutor(FlowExecutor flowExecutor) {
        this.flowExecutor = flowExecutor;
    }

    public String getExtendFlowId() {
        return extendFlowId;
    }

    public void setExtendFlowId(String extendFlowId) {
        this.extendFlowId = extendFlowId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Node> getNodeMap() {
        return nodeMap;
    }

    public List<Node> getNodes() {
        if (nodes == null) {
            nodes = new ArrayList<Node>();
        }
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServiceName() {
        return id;
    }

    public List<Parameter> getParameters() {
        List<Parameter> p = getInputParameters();
        if (p != null) {
            return p;
        }
        return new ArrayList<Parameter>();
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

}
