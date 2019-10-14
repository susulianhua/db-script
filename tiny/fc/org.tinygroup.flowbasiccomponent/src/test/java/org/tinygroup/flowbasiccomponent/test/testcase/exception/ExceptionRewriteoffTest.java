/**
 * Copyright (c) 2012-2016, www.tinygroup.org (luo_guo@icloud.com).
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
package org.tinygroup.flowbasiccomponent.test.testcase.exception;

import org.tinygroup.context.Context;
import org.tinygroup.context.util.ContextFactory;
import org.tinygroup.event.Event;
import org.tinygroup.flowbasiccomponent.test.AbstractFlowComponent;

/**
 * @author qiucn
 */
public class ExceptionRewriteoffTest extends AbstractFlowComponent {

    /**
     * a==1
     */
    public void testException1() {
        Context context = ContextFactory.getContext();
        context.put("a", 1);
        context.put("serviceId", "exceptionNodeService");
        Event e = Event.createEvent("exceptionRewriteoffTestFlow", context);
        cepcore.process(e);
        Integer result = e.getServiceRequest().getContext().get("a");
        assertEquals(1, result.intValue());
    }

    /**
     * a==2
     */
    public void testException2() {
        Context context = ContextFactory.getContext();
        context.put("a", 2);
        context.put("serviceId", "exceptionNodeService");
        Event e = Event.createEvent("exceptionRewriteoffTestFlow", context);
        cepcore.process(e);
        Integer result = e.getServiceRequest().getContext().get("a");
        assertEquals(2, result.intValue());
    }

    /**
     * a==3
     */
    public void testException3() {
        Context context = ContextFactory.getContext();
        context.put("a", 3);
        context.put("serviceId", "exceptionNodeService");
        Event e = Event.createEvent("exceptionRewriteoffTestFlow", context);
        cepcore.process(e);
        Integer result = e.getServiceRequest().getContext().get("a");
        assertEquals(3, result.intValue());
    }

    /**
     * a==4
     */
    public void testException() {
        Context context = ContextFactory.getContext();
        context.put("a", 4);
        context.put("serviceId", "exceptionNodeService");
        Event e = Event.createEvent("exceptionRewriteoffTestFlow", context);
        cepcore.process(e);
        Integer result = e.getServiceRequest().getContext().get("a");
        assertEquals(4, result.intValue());
    }
}
