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
package org.tinygroup.flowbasiccomponent.test.testcase.datautil;

import org.tinygroup.context.Context;
import org.tinygroup.context.util.ContextFactory;
import org.tinygroup.event.Event;
import org.tinygroup.flowbasiccomponent.test.AbstractFlowComponent;
import org.tinygroup.flowbasiccomponent.test.User;

/**
 * @author qiucn
 */
public class NewInstanceTest extends AbstractFlowComponent {

    public void testNewInstance() {
        Context context = ContextFactory.getContext();
        Event e = Event.createEvent("newInstanceTestFlow", context);
        flowExecutor.execute("newInstanceTestFlow", context);
        User u = e.getServiceRequest().getContext().get("user");
        assertEquals("浙江", u.getName());
        User u1 = e.getServiceRequest().getContext().get("user1");
        assertEquals("杭州", u1.getName());
    }

}
