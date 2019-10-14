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
package org.tinygroup.flow.test.newtestcase.exception;

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.event.Event;
import org.tinygroup.flow.component.AbstractFlowComponent;
import org.tinygroup.flow.exception.FlowRuntimeException;
import org.tinygroup.flow.exception.errorcode.FlowExceptionErrorCode;

/**
 * @description：节点未配置后续节点
 * @author: qiucn
 * @version: 2016年5月10日 上午11:05:45
 */
public class FlowNextNodeExceptionTest extends AbstractFlowComponent {

    /**
     * @description：节点未配置后续节点
     * @author: qiucn
     * @version: 2016年5月10日 上午11:05:45
     */
    public void testflowProperty() {
        try {
            Context context = new ContextImpl();
            context.put("el", "");
            context.put("str", "");
            flowExecutor.execute("flowNextNodeTestFlow", context);
        } catch (FlowRuntimeException e) {
            assertEquals(
                    FlowExceptionErrorCode.FLOW_NEXT_NODE_NOT_FOUND_EXCEPTION,
                    e.getErrorCode().toString());
        }
        try {
            Context context2 = new ContextImpl();
            context2.put("el", "");
            context2.put("str", "");
            Event e = Event.createEvent("flowNextNodeTestFlow", context2);
            cepcore.process(e);
        } catch (FlowRuntimeException e) {
            assertEquals(
                    FlowExceptionErrorCode.FLOW_NEXT_NODE_NOT_FOUND_EXCEPTION,
                    e.getErrorCode().toString());
        }
    }
}
