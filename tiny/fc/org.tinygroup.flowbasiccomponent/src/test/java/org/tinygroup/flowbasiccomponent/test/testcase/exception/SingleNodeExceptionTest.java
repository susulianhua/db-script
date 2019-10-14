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
public class SingleNodeExceptionTest extends AbstractFlowComponent {

    /**
     * 服务正常执行
     */
    public void testException() {
        Context context = ContextFactory.getContext();
        context.put("serviceId", "hello");
        Event e = Event.createEvent("singleNodeExceptionTestFlow", context);
        cepcore.process(e);
    }

    /**
     * 服务抛出异常
     */
    public void testThrowException() {
        Context context = ContextFactory.getContext();
        context.put("serviceId", "exceptionService");
        Event e = Event.createEvent("singleNodeExceptionTestFlow", context);
        cepcore.process(e);
    }
}
