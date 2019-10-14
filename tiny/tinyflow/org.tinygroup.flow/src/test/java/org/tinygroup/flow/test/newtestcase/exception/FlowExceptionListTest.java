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

public class FlowExceptionListTest extends AbstractFlowComponent {

    public void testException1() {
        Context context = new ContextImpl();
        context.put("exceptionNo", 1);
        Event e = Event.createEvent("flowExceptionListTest", context);
        cepcore.process(e);
        assertEquals(1, Integer.valueOf(e.getServiceRequest().getContext().get("result").toString()).intValue());
    }

    public void testException2() {
        Context context = new ContextImpl();
        context.put("exceptionNo", 2);
        Event e = Event.createEvent("flowExceptionListTest", context);
        cepcore.process(e);
        assertEquals(1, Integer.valueOf(e.getServiceRequest().getContext().get("result").toString()).intValue());
    }

    public void testException3() {
        Context context = new ContextImpl();
        context.put("exceptionNo", 3);
        Event e = Event.createEvent("flowExceptionListTest", context);
        cepcore.process(e);
        assertEquals(1, Integer.valueOf(e.getServiceRequest().getContext().get("result").toString()).intValue());
    }

    public void testException4() {
        Context context = new ContextImpl();
        context.put("exceptionNo", 4);
        Event e = Event.createEvent("flowExceptionListTest", context);
        cepcore.process(e);
        assertEquals(2, Integer.valueOf(e.getServiceRequest().getContext().get("result").toString()).intValue());
    }

    public void testException5() {
        Context context = new ContextImpl();
        context.put("exceptionNo", 5);
        Event e = Event.createEvent("flowExceptionListTest", context);
        cepcore.process(e);
        assertEquals(2, Integer.valueOf(e.getServiceRequest().getContext().get("result").toString()).intValue());
    }
}
