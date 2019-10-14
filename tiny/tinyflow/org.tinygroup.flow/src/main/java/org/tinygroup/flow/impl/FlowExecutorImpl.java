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

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flow.config.Component;
import org.tinygroup.flow.config.Flow;
import org.tinygroup.flow.config.Node;
import org.tinygroup.flow.config.SubFlow;
import org.tinygroup.flow.exception.FlowRuntimeException;
import org.tinygroup.flow.exception.errorcode.FlowExceptionErrorCode;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 默认流程执行器
 *
 * @author luoguo
 */
public class FlowExecutorImpl extends AbstractFlowExecutorImpl {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(FlowExecutorImpl.class);
    private static Map<String, Class<?>> exceptionMap = new HashMap<String, Class<?>>();

    private static Class<?> getExceptionType(String name) {
        Class<?> exceptionType = exceptionMap.get(name);
        if (exceptionType == null) {
            try {
                exceptionType = Class.forName(name);// TODO:通过Loader进行获取
                exceptionMap.put(name, exceptionType);
            } catch (ClassNotFoundException e) {
                throw new FlowRuntimeException(e);
            }
        }
        return exceptionType;
    }

    public void execute(String flowId, Context context) {
        execute(flowId, null, context);
    }

    public void execute(String flowId, String nodeId, Context context) {
        LOGGER.logMessage(LogLevel.INFO, "开始执行流程[flowId:{0},nodeId:{1}]执行",
                flowId, nodeId);
        if (!getFlowIdMap().containsKey(flowId)) {
            LOGGER.logMessage(LogLevel.ERROR, "流程{0}不存在", flowId);
            throw new FlowRuntimeException(
                    FlowExceptionErrorCode.FLOW_NOT_EXIST, flowId);
        }
        Flow flow = getFlowIdMap().get(flowId);
        Node node = getStartNode(flow, nodeId);
        if (node != null) {
            logContext(context);
            checkInputParameter(flow, context);
            execute(flow, node, context);
            checkOutputParameter(flow, context);
            logContext(context);
        }
        LOGGER.logMessage(LogLevel.INFO, "流程[flowId:{0},nodeId:{1}]执行完毕",
                flowId, nodeId);
    }


    /**
     * 如果nodeId为空，则为其赋值Begin 如果begin不存在,分两种情况 1、节点数大于0，则执行第一个节点 2、节点数等于0，无需执行
     * 如果nodeId为end，则无需执行 无需执行的两种情况下，返回的结果集为null 除以上情况外,若节点不存在，则抛出节点不存在的异常
     *
     * @param flow
     * @param nodeId
     * @return
     */
    private Node getStartNode(Flow flow, String nodeId) {
        if (DEFAULT_END_NODE.equals(nodeId)) { // 如果执行的是end，则无需执行
            LOGGER.logMessage(LogLevel.INFO,
                    "流程[flowId:{0},nodeId:{1}]为结束节点,无需执行", flow.getId(),
                    DEFAULT_END_NODE);
            return null;
        }
        if (nodeId == null) { // 如果节点为空，则赋值为begin
            nodeId = DEFAULT_BEGIN_NODE;
        }
        if (DEFAULT_BEGIN_NODE.equals(nodeId) && flow.getNodes().size() == 0) {
            LOGGER.logMessage(LogLevel.INFO,
                    "流程无节点,流程[flowId:{0},nodeId:{1}]执行完毕。", flow.getId(),
                    DEFAULT_BEGIN_NODE);
            return null;
        }
        Node node = flow.getNodeMap().get(nodeId);
        // 如果begin节点不存在，且节点数大于0，则从第一个节点开始执行
        if (node == null && DEFAULT_BEGIN_NODE.equals(nodeId)) {
            node = flow.getNodes().get(0);
            LOGGER.logMessage(LogLevel.WARN, "流程{0}-节点{1}不存在,从节点{}开始执行",
                    flow.getId(), nodeId, node.getName());
        } else if (node == null) {// 节点不存在，且节点名不为begin
            LOGGER.logMessage(LogLevel.ERROR, "流程{0}-节点{1}不存在", flow.getId(),
                    nodeId);
            throw new FlowRuntimeException(
                    FlowExceptionErrorCode.FLOW_NODE_NOT_EXIST, flow.getId(),
                    nodeId);
        }
        return node;
    }


    private void execute(Flow flow, Node node, Context context) {
        Context flowContext = checkprivateContext(flow, context);
        execute(flow, null, node, flowContext);
    }

    private String getNodeIdPath(Flow flow, SubFlow subFlow, Node node) {
        StringBuilder sb = new StringBuilder();
        if (subFlow == null) {
            sb.append("flow:").append(flow.getId()).append(",");
            sb.append("node:").append(node.getId());
        } else {
            sb.append("flow:").append(flow.getId()).append(",");
            sb.append("subflow:").append(subFlow.getId()).append(",");
            sb.append("node:").append(node.getId());
        }
        return sb.toString();
    }

    private String getNodeIdPath(ExceptionDealNodeInfo dealInfo) {
        return getNodeIdPath(dealInfo.getFlow(), dealInfo.getSubFlow(), dealInfo.getNode());
    }

    private String getNodeIdPath(NodeExceptionInfo info) {
        return getNodeIdPath(info.getFlow(), info.getSubFlow(), info.getNode());
    }

    /**
     * 执行具体节点，并继续往下执行
     *
     * @param flow
     *            当前执行的流程
     * @param node
     *            非end节点的任意合法节点
     * @param context
     */
    private void execute(Flow flow, SubFlow subFlow, Node node, Context context) {

        String nodePath = getNodeIdPath(flow, subFlow, node);
        try {
            reallyExecute(flow, subFlow, node, context);
        } catch (RuntimeException exception) {
            //異常發生信息
            NodeExceptionInfo exceptionInfo = new NodeExceptionInfo(flow, subFlow, node, exception);
            LOGGER.errorMessage("流程执行[{}]发生异常", exception, nodePath);
            if (exceptionNodeProcess(exceptionInfo, new ExceptionDealNodeInfo(flow, subFlow, node), context)) {
                return;
            }
            // 如果节点中没有处理掉，则由流程的异常处理节点进行处理
            Node exceptionNode = flow.getNodeMap().get(EXCEPTION_DEAL_NODE);
            if (exceptionNode != null
                    && exceptionNodeProcess(exceptionInfo, new ExceptionDealNodeInfo(flow, null, exceptionNode), context)) {
                return;
            }
            // 如果还是没有被处理掉，则交由异常处理流程进行管理
            Flow exceptionProcessFlow = this.getFlow(EXCEPTION_DEAL_FLOW);
            if (exceptionProcessFlow != null) {
                exceptionNode = exceptionProcessFlow.getNodeMap().get(
                        EXCEPTION_DEAL_NODE);
                if (exceptionNode != null
                        && exceptionNodeProcess(exceptionInfo,
                        new ExceptionDealNodeInfo(exceptionProcessFlow, null, exceptionNode)
                        , context)) {
                    return;
                }
            }
            throw exception;
        }
        String nodeId = node.getId(); // 当前节点id
        if (nodeId != null && !nodeId.equals(DEFAULT_END_NODE)) {
            String nextNodeId = findNextNode(flow, subFlow, node, context);
            if (nextNodeId == null) {
                return;//为Null表明是子流程的最终节点，这里返回
            }
            executeNextNode(flow, subFlow, context, nextNodeId);
        }
    }

    private String findNextNode(Flow flow, SubFlow subFlow, Node node, Context context) {
        // 先直接取，如果取到就执行，如果取不到，则用正则去匹配，效率方面的考虑
        String nextNodeId = node.getNextNodeId(context);
        if (nextNodeId == null) {
            if (subFlow == null) { //主流程
                LOGGER.logMessage(LogLevel.ERROR, "流程：{0}的节点：{1}的后续节点未找到",
                        flow.getId(), node.getId());
                throw new FlowRuntimeException(
                        FlowExceptionErrorCode.FLOW_NEXT_NODE_NOT_FOUND_EXCEPTION,
                        flow.getId(), node.getId());
            } else { //子流程，如果是子流程，无后续节点就是子流程的最终节点了
                return nextNodeId;
            }
        }
        LOGGER.logMessage(LogLevel.INFO, "下一节点:{}", nextNodeId);
        return nextNodeId;
    }

    private void reallyExecute(Flow flow, SubFlow subFlow, Node node,
                               Context flowContext) {
        String nodePath = getNodeIdPath(flow, subFlow, node);
        LOGGER.logMessage(LogLevel.INFO, "开始执行节点:{}", nodePath);
        Component component = node.getComponent();
        String subFlowId = node.getSubFlowId();
        if (component != null) {
            ComponentInterface componentInstance = getComponentInstance(component
                    .getName());
            setProperties(node, componentInstance, flowContext);
            String nodeId = node.getId();
            if (!nodeId.equals(DEFAULT_END_NODE)) { // 如果当前节点不是最终节点
                componentInstance.execute(flowContext);
            }
            LOGGER.logMessage(LogLevel.INFO, "节点:{}执行完毕", nodePath);
        } else if (!StringUtil.isBlank(subFlowId)) {
            executeSubFlow(flow, subFlowId, flowContext);
        } else {
            LOGGER.logMessage(LogLevel.INFO, "节点:{}未配置组件，无需执行", nodePath);
        }
    }

    private Context checkprivateContext(Flow flow, Context context) {
        if (flow.isPrivateContext()) { // 是否context私有
            Context flowContext = getNewContext(flow, context);
            if (flowContext == null) {
                flowContext = new ContextImpl();
                context.putSubContext(flow.getId(), flowContext);
            }
            return flowContext;
        }
        return context;
    }


    private boolean exceptionNodeProcess(NodeExceptionInfo nodeExceptionInfo, ExceptionDealNodeInfo dealNodeInfo, Context context) {
        List<String> nextExceptionList = dealNodeInfo.getNode().getNextExceptionList();
        Exception exception = nodeExceptionInfo.getException();
        // 20130524调整为顺序取异常进行匹配
        for (int i = 0; i < nextExceptionList.size(); i++) {
            String exceptionName = nextExceptionList.get(i);
            if (dealException(exception, exceptionName, nodeExceptionInfo, dealNodeInfo, context
            )) {
                return true;
            }
            Throwable t = exception.getCause();
            while (t != null) {
                if (dealException(t, exceptionName, nodeExceptionInfo, dealNodeInfo, context)) {
                    return true;
                }
                t = t.getCause();
            }

        }
        return false;
    }


    /**
     * 处理异常，如果找到合适的后续节点分支，则执行该分支节点，并返回true
     * 否则返回false
     */
    private boolean dealException(Throwable exception, String exceptionName, NodeExceptionInfo info, ExceptionDealNodeInfo dealInfo, Context context) {
        if (getExceptionType(exceptionName).isInstance(exception)) {// 如果异常匹配
            String nextNode = dealInfo.getNode().getNextExceptionNodeMap().get(exceptionName);
            context.put(EXCEPTION_DEAL_FLOW, dealInfo.getFlow());
            context.put(EXCEPTION_DEAL_SUBFLOW, dealInfo.getSubFlow());
            context.put(EXCEPTION_DEAL_NODE_KEY, dealInfo.getNode());
            context.put(EXCEPTION_KEY, exception);
            executeNextNode(dealInfo.getFlow(), dealInfo.getSubFlow(), context, nextNode);
            LOGGER.infoMessage("处理流程异常,处理节点:{}，异常节点:{},nextNode:{}", getNodeIdPath(dealInfo),
                    getNodeIdPath(info), nextNode);
            return true;
        }
        return false;
    }

    private void executeNextNode(Flow flow, SubFlow subFlow, Context context, String nextNodeId) {
        String nextExecuteNodeId = nextNodeId;
        int index = nextNodeId.indexOf(':');
        if (index > 0) { // newflowId:newnodeId
            String[] str = nextNodeId.split(":");
            if (str.length > 1) {
                nextExecuteNodeId = str[1];
            } else {
                nextExecuteNodeId = DEFAULT_BEGIN_NODE;
            }
            // 从另一个流程的节点开始执行
            execute(str[0], nextExecuteNodeId, context);
        } else if (!DEFAULT_END_NODE.equals(nextNodeId)) {
            Node nextNode = flow.getNodeMap().get(nextNodeId);
            if (nextNode == null) {
                nextNode = subFlow.getNode(nextNodeId);
            }
            if (nextNode == null) {
                //TODO：改成标准异常
                throw new RuntimeException("未找到节点:" + nextNodeId
                );
            }
            execute(flow, subFlow, nextNode, context);
        }
    }


    private void executeSubFlow(Flow flow, String subFlowId, Context context) {
        SubFlow subFlow = getSubFlow(subFlowId);
        if (subFlow == null) {
            //TODO:throw 异常
        }
        executeSubFlow(flow, subFlow, context);

    }

    private void executeSubFlow(Flow flow, SubFlow subFlow, Context context) {
        String startNode = subFlow.getStartWith();
        Node node = subFlow.getNode(startNode);
        execute(flow, subFlow, node, context);
    }

}
