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
package org.tinygroup.flow.test.newtestcase.subflow;

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.event.Event;
import org.tinygroup.flow.component.AbstractFlowComponent;
import org.tinygroup.flow.test.newtestcase.exception.component.ComponentException5;

/**
 * 子流程测试用例
 *
 * @author zhangliang08072
 * @version $Id: FlowExceptionTest.java, v 0.1 2016年12月6日 上午12:00:03 zhangliang08072 Exp $
 */
public class SubflowExceptionTest extends AbstractFlowComponent {


    /**
     * el表达式测试组件给exceptionNo赋值
     * 分支处理异常
     * 异常生成switch组件抛出org.tinygroup.flow.test.newtestcase.exception.component.ComponentException1
     * 然后被“测试流程异常1”节点处理
     */
    public void testException1() {
        Context context = new ContextImpl();
        context.put("el", "exceptionNo=1");
        flowExecutor.execute("subflowExceptionTest", "begin", context);
        assertEquals(1, Integer.valueOf(context.get("result").toString()).intValue());
        assertEquals(2, Integer.valueOf(context.get("c").toString()).intValue());


        Context context2 = new ContextImpl();
        context2.put("el", "exceptionNo=1");
        Event e = Event.createEvent("subflowExceptionTest", context2);
        cepcore.process(e);
        assertEquals(1, Integer.valueOf(e.getServiceRequest().getContext().get("result").toString()).intValue());
        assertEquals(2, Integer.valueOf(e.getServiceRequest().getContext().get("c").toString()).intValue());
    }

    /**
     * 分支处理异常
     * 异常生成switch组件抛出org.tinygroup.flow.test.newtestcase.exception.component.ComponentException2
     * 然后被“测试流程异常2”节点处理
     */
    public void testException2() {
        Context context = new ContextImpl();
        context.put("el", "exceptionNo=2");
        flowExecutor.execute("subflowExceptionTest", "begin", context);
        assertEquals(2, Integer.valueOf(context.get("result").toString()).intValue());
        assertEquals(3, Integer.valueOf(context.get("c").toString()).intValue());

        Context context2 = new ContextImpl();
        context2.put("el", "exceptionNo=2");
        Event e = Event.createEvent("subflowExceptionTest", context2);
        cepcore.process(e);
        assertEquals(2, Integer.valueOf(e.getServiceRequest().getContext().get("result").toString()).intValue());
        assertEquals(3, Integer.valueOf(e.getServiceRequest().getContext().get("c").toString()).intValue());
    }

    /**
     * 流程内异常节点处理异常
     * 异常生成switch组件抛出org.tinygroup.flow.test.newtestcase.exception.component.ComponentException3
     * 然后被“测试流程异常3”节点处理
     */
    public void testException3() {
        Context context = new ContextImpl();
        context.put("el", "exceptionNo=3");
        flowExecutor.execute("subflowExceptionTest", "begin", context);
        assertEquals(3, Integer.valueOf(context.get("result").toString()).intValue());

        Context context2 = new ContextImpl();
        context2.put("el", "exceptionNo=3");
        Event e = Event.createEvent("subflowExceptionTest", context2);
        cepcore.process(e);
        assertEquals(3, Integer.valueOf(e.getServiceRequest().getContext().get("result").toString()).intValue());
    }

    /**
     * 异常流程处理异常
     * 异常生成switch组件抛出org.tinygroup.flow.test.newtestcase.exception.component.ComponentException4
     * 然后被流程ID为"exceptionProcessFlow"的流程的“测试流程异常4”节点处理
     */
    public void testException4() {
        Context context = new ContextImpl();
        context.put("el", "exceptionNo=4");
        flowExecutor.execute("subflowExceptionTest", "begin", context);
        assertEquals(4, Integer.valueOf(context.get("result").toString()).intValue());

        Context context2 = new ContextImpl();
        context2.put("el", "exceptionNo=4");
        Event e = Event.createEvent("subflowExceptionTest", context2);
        cepcore.process(e);
        assertEquals(4, Integer.valueOf(e.getServiceRequest().getContext().get("result").toString()).intValue());
    }

    /**
     * 异常处理未定义
     * 异常生成switch组件抛出org.tinygroup.flow.test.newtestcase.exception.component.ComponentException5
     * 没有任何流程处理该异常，直接向外层调用抛出
     */
    public void testException5() {
        Context context = new ContextImpl();
        context.put("el", "exceptionNo=5");
        try {
            flowExecutor.execute("subflowExceptionTest", "begin", context);
        } catch (ComponentException5 e) {
            assertTrue(true);
        }

        Context context2 = new ContextImpl();
        context2.put("el", "exceptionNo=5");
        try {
            Event e = Event.createEvent("subflowExceptionTest", context2);
            cepcore.process(e);
        } catch (ComponentException5 e) {
            assertTrue(true);
        }
    }
}
