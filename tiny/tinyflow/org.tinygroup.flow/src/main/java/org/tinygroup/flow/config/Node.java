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
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.context.Context;
import org.tinygroup.flow.util.FlowElUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程节点
 *
 * @author luoguo
 */
@XStreamAlias("node")
public class Node implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6987481042476758848L;
    @XStreamAsAttribute
    private String id;// 唯一ID
    @XStreamAsAttribute
    private String name;// 英文名
    @XStreamAsAttribute
    private String title;// 中本名
    private String description;// 描述
    @XStreamAsAttribute
    @XStreamAlias("subflow-id")
    private String subFlowId;
    private Component component;// 组件
    @XStreamAlias("next-nodes")
    private List<NextNode> nextNodes;// 后续执行的节点列表
    @XStreamAlias("default-node-id")
    @XStreamAsAttribute
    private String defaultNodeId;

    private transient Map<String, String> nextExceptionNodeMap = new HashMap<String, String>();
    private transient List<String> nextExceptionList = new ArrayList<String>();
    private transient boolean exceptionMapComputed = false;

    public String getDefaultNodeId() {
        return defaultNodeId;
    }

    public void setDefaultNodeId(String defaultNodeId) {
        this.defaultNodeId = defaultNodeId;
    }

    public Map<String, String> getNextExceptionNodeMap() {
        if (exceptionMapComputed) {
            return nextExceptionNodeMap;
        } else {
            initNextExceptionInfo();
        }

        return nextExceptionNodeMap;

    }

    public List<String> getNextExceptionList() {
        if (exceptionMapComputed) {
            return nextExceptionList;
        } else {
            initNextExceptionInfo();
        }
        return nextExceptionList;
    }

    private void initNextExceptionInfo() {
        nextExceptionNodeMap = new HashMap<String, String>();
        nextExceptionList = new ArrayList<String>();
        if (nextNodes != null) {
            for (int i = 0; i < nextNodes.size(); i++) {
                NextNode nextNode = nextNodes.get(i);
                List<String> exceptionTypes = nextNode.getExceptionTypes();
                for (String exceptionType : exceptionTypes) {
                    if (exceptionType != null && exceptionType.length() > 0
                            && !nextExceptionNodeMap.containsKey(exceptionType)) {
                        nextExceptionNodeMap.put(exceptionType,
                                nextNode.getNextNodeId());
                        nextExceptionList.add(exceptionType);
                    }
                }
            }
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public List<NextNode> getNextNodes() {
        return nextNodes;
    }

    public void setNextNodes(List<NextNode> nextNodes) {
        this.nextNodes = nextNodes;
    }

    public String getNextNodeId(Context context) {
        if (nextNodes == null) {
            return getDefaultNodeId();
        }
        for (NextNode node : nextNodes) {
            String exceptions = node.getExceptionType();
            String el = node.getEl();
            if (!StringUtil.isBlank(exceptions) && StringUtil.isBlank(el)) {
                continue;
            }

            if (StringUtil.isBlank(el)) {// 如果表达式为空或者未配，则认为是true
                return node.getNextNodeId();
            }
            if (FlowElUtil.executeCondition(el, context, this.getClass()
                    .getClassLoader())) {
                return node.getNextNodeId();
            }
        }
        return getDefaultNodeId();
    }

    public void combine(Node parentNode) {
        if (name == null) {
            name = parentNode.getName();
        }
        if (defaultNodeId == null) {
            defaultNodeId = parentNode.getDefaultNodeId();
        }
        if (title == null) {
            title = parentNode.getTitle();
        }
        if (description == null) {
            description = parentNode.getDescription();
        }

        if (component == null) {
            component = parentNode.getComponent();
        } else {
            if (component.getName().equals(parentNode.getComponent().getName())) {
                component.combile(parentNode.getComponent());
            }
        }
        combineNextNode(parentNode);
    }

    public String getSubFlowId() {
        return subFlowId;
    }

    public void setSubFlowId(String subFlowId) {
        this.subFlowId = subFlowId;
    }

    private void combineNextNode(Node parentNode) {
        if (parentNode.getNextNodes() != null) {
            if (nextNodes == null || nextNodes.size() == 0) {
                nextNodes = parentNode.getNextNodes();
            }
//			else {
//				for (NextNode nextNode : parentNode.getNextNodes()) {
//					if (!nextNodes.contains(nextNode)) {
//						nextNodes.add(nextNode);
//					}
//				}
//			}
        }
    }
}
