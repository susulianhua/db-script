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
package org.tinygroup.flowbasiccomponent.test.testcase.data;

import org.tinygroup.context.Context;
import org.tinygroup.context.util.ContextFactory;
import org.tinygroup.event.Event;
import org.tinygroup.flowbasiccomponent.test.AbstractFlowComponent;

/**
 * @author qiucn
 */
public class ElNum2StrTest extends AbstractFlowComponent {

    /**
     *
     */
    public void testElNum2Str() {
        Context context = ContextFactory.getContext();
        context.put("num", 123);
        context.put("el", "result=''+num");
        Event e = Event.createEvent("elNum2StrTestFlow", context);
        cepcore.process(e);
        assertEquals("123", e.getServiceRequest().getContext().get("result"));
    }

}
