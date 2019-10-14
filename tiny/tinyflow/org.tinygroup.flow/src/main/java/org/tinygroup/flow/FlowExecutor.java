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
package org.tinygroup.flow;

import org.tinygroup.context.Context;
import org.tinygroup.flow.config.Flow;

/**
 * 流程执行器
 *
 * @author luoguo
 */
public interface FlowExecutor extends FlowManager {
    String FLOW_XSTREAM_PACKAGENAME = "flow";
    String FLOW_BEAN = "flowExecutor";
    String PAGE_FLOW_BEAN = "pageFlowExecutor";
    String DEFAULT_BEGIN_NODE = "begin";
    String DEFAULT_END_NODE = "end";
    String EXCEPTION_DEAL_NODE = "exception";
    String EXCEPTION_DEAL_NODE_KEY = "exceptionNode";
    String EXCEPTION_KEY = "throwableObject";
    String EXCEPTION_DEAL_FLOW = "exceptionProcessFlow";
    String EXCEPTION_DEAL_SUBFLOW = "exceptionProcessSubFlow";

    /**
     * 流程执行接口方法
     *
     * @param flowId
     *            要执行的流程
     * @param nodeId
     *            要执行的节点标识
     * @param context
     *            要执行的环境
     */
    void execute(String flowId, String nodeId, Context context);

    /**
     * 表示从开始节点开始执行
     *
     * @param flowId
     * @param context
     */
    void execute(String flowId, Context context);


    Context getInputContext(Flow flow, Context context);

    Context getOutputContext(Flow flow, Context context);

    boolean isChange();

    void setChange(boolean change);
}
