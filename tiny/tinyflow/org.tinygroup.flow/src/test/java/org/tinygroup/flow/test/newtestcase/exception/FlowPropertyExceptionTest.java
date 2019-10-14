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

import org.tinygroup.flow.component.AbstractFlowComponent;
import org.tinygroup.flow.config.Flow;
import org.tinygroup.flow.exception.FlowRuntimeException;
import org.tinygroup.flow.exception.errorcode.FlowExceptionErrorCode;

/**
 * @description：系统启动时对流程属性校验,校验不通过则系统启动失败
 * @author: qiucn
 * @version: 2016年4月27日 下午5:15:05
 */
public class FlowPropertyExceptionTest extends AbstractFlowComponent {

    /**
     * @description：流程flowPropertyTest1Flow的参数校验失败
     * @author: qiucn
     * @version: 2016年4月27日下午6:01:59
     */
    public void testflowProperty() {
        try {
            Flow flow = flowExecutor.getFlow("flowPropertyTestFlow");
            flow.getNodes().get(1).getComponent().getProperties().get(0).setValue("");
            flow.validate();
        } catch (FlowRuntimeException e) {
            assertEquals(FlowExceptionErrorCode.FLOW_PROPERTY_VALIDATE_EXCEPTION, e.getErrorCode().toString());
        } finally {
            //测试完毕将流程参数修改正确，防止其它测试用例执行失败
            Flow flow = flowExecutor.getFlow("flowPropertyTestFlow");
            flow.getNodes().get(1).getComponent().getProperties().get(0).setValue("a=1");
            flow.validate();
        }
    }
}
