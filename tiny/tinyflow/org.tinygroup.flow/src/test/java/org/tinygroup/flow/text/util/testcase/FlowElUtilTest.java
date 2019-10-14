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
package org.tinygroup.flow.text.util.testcase;

import org.tinygroup.context.Context;
import org.tinygroup.context.util.ContextFactory;
import org.tinygroup.event.Event;
import org.tinygroup.flow.component.AbstractFlowComponent;

public class FlowElUtilTest extends AbstractFlowComponent {

    public void testElUtil1() {
        Context context = ContextFactory.getContext();
        context.put("a", "");
        context.put("el", "b=StringUtil.isBlank(a)");
        Event e = Event.createEvent("elUtilTest", context);
        cepcore.process(e);
        assertEquals(true, e.getServiceRequest().getContext().get("b"));
    }

    public void testElUtil2() {
        Context context = ContextFactory.getContext();
        context.put("a", "上山打老虎");
        context.put("el", "FlowTestUtil.systemout(a)");
        Event e1 = Event.createEvent("elUtilTest", context);
        cepcore.process(e1);
    }

    public void testElUtil3() {
        Context context = ContextFactory.getContext();
        context.put("a", "上山打老虎");
        context.put("el", "b=FlowTestUtil.getName(a)");
        Event e2 = Event.createEvent("elUtilTest", context);
        cepcore.process(e2);
        assertEquals("hello：上山打老虎", e2.getServiceRequest().getContext().get("b"));
    }
}
