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

public class NewFlowExceptionTest extends AbstractFlowComponent {

    /**
     * a<0
     */
    public void testExceptionEl1() {
        Context context = new ContextImpl();
        context.put("b", -2);
        context.put("a", 1);
        Event e = Event.createEvent("flowExceptionElTest", context);
        cepcore.process(e);
        assertEquals(2, Integer.valueOf(e.getServiceRequest().getContext().get("result").toString()).intValue());
    }

    /**
     * a>0
     */
    public void testExceptionEl2() {
        Context context2 = new ContextImpl();
        context2.put("b", 2);
        context2.put("a", 1);
        Event e2 = Event.createEvent("flowExceptionElTest", context2);
        cepcore.process(e2);
        assertEquals(1, Integer.valueOf(e2.getServiceRequest().getContext().get("result").toString()).intValue());
    }

    /**
     * 异常，走的分支跟a>0是同一个
     */
    public void testExceptionEl3() {
        Context context3 = new ContextImpl();
        context3.put("b", 0);
        context3.put("a", 1);
        Event e3 = Event.createEvent("flowExceptionElTest", context3);
        cepcore.process(e3);
        assertEquals(1, Integer.valueOf(e3.getServiceRequest().getContext().get("result").toString()).intValue());
    }

    /**
     * 异常1
     */
    public void testExceptionSame1() {
        Context context1 = new ContextImpl();
        context1.put("exceptionNo", 1);
        Event e1 = Event.createEvent("flowExceptionSameTest", context1);
        cepcore.process(e1);
        assertEquals(1, Integer.valueOf(e1.getServiceRequest().getContext().get("result").toString()).intValue());
    }

    /**
     * 异常2
     */
    public void testExceptionSame2() {
        Context context2 = new ContextImpl();
        context2.put("exceptionNo", 2);
        Event e2 = Event.createEvent("flowExceptionSameTest", context2);
        cepcore.process(e2);
        assertEquals(2, Integer.valueOf(e2.getServiceRequest().getContext().get("result").toString()).intValue());
    }

    /**
     * 异常3
     */
    public void testExceptionSame3() {
        Context context3 = new ContextImpl();
        context3.put("exceptionNo", 3);
        Event e3 = Event.createEvent("flowExceptionSameTest", context3);
        cepcore.process(e3);
        assertEquals(3, Integer.valueOf(e3.getServiceRequest().getContext().get("result").toString()).intValue());
    }
}
