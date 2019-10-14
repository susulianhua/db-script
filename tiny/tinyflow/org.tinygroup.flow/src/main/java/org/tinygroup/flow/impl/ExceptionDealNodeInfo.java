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

import org.tinygroup.flow.config.Flow;
import org.tinygroup.flow.config.Node;
import org.tinygroup.flow.config.SubFlow;

public class ExceptionDealNodeInfo {
    private Node node;
    private SubFlow subFlow;
    private Flow flow;

    public ExceptionDealNodeInfo(Flow flow, SubFlow subFlow, Node node) {
        super();
        this.node = node;
        this.subFlow = subFlow;
        this.flow = flow;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public SubFlow getSubFlow() {
        return subFlow;
    }

    public void setSubFlow(SubFlow subFlow) {
        this.subFlow = subFlow;
    }

    public Flow getFlow() {
        return flow;
    }

    public void setFlow(Flow flow) {
        this.flow = flow;
    }


}
