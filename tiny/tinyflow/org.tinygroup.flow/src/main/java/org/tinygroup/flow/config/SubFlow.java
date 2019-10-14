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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XStreamAlias("subflow")
public class SubFlow implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -5467875047887794167L;
    @XStreamAsAttribute
    private String id;
    @XStreamAsAttribute
    private String title;
    private List<Node> nodes;// 子流程节点
    @XStreamAsAttribute
    @XStreamAlias("start-with")
    private String startWith;
    @XStreamAsAttribute
    @XStreamAlias("end-with")
    private String endWith;
    @XStreamAsAttribute
    private String description;

    private transient Map<String, Node> nodeMap = new HashMap<String, Node>();

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        if (nodes == null) {
            this.nodes = new ArrayList<Node>();
        } else {
            this.nodes = nodes;
        }
        initNodeMap();
    }

    public void assemble() {
        initNodeMap();
    }

    private void initNodeMap() {
        if (nodeMap == null) {
            nodeMap = new HashMap<String, Node>();
        }
        for (Node node : nodes) {
            nodeMap.put(node.getId(), node);
        }
    }

    public Node getNode(String nodeId) {
        return nodeMap.get(nodeId);
    }

    public String getStartWith() {
        return startWith;
    }

    public void setStartWith(String startWith) {
        this.startWith = startWith;
    }

    public String getEndWith() {
        return endWith;
    }

    public void setEndWith(String endWith) {
        this.endWith = endWith;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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


}
