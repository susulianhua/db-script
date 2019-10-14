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
package org.tinygroup.cepcoreimpl.test.newcase.threadlocal;

import org.tinygroup.cepcore.util.ThreadContextUtil;
import org.tinygroup.cepcoreimpl.test.testcase.CEPCoreBaseTestCase;
import org.tinygroup.context.util.ContextFactory;
import org.tinygroup.event.Event;

public class TestThreadLocalService extends CEPCoreBaseTestCase {
    public void testAsyn() {
        ThreadContextUtil.put("a", "a");
        Event event = getEvent(Event.EVENT_MODE_ASYNCHRONOUS);
        getCore().process(event);
        assertEquals("a", ThreadContextUtil.get("a"));
    }

    public void testSyn() {
        ThreadContextUtil.put("a", "a");
        Event event = getEvent(Event.EVENT_MODE_SYNCHRONOUS);
        getCore().process(event);
        assertEquals("a", ThreadContextUtil.get("a"));
    }

    private Event getEvent(int mode) {
        Event e = Event.createEvent("threadLocalService",
                ContextFactory.getContext());
        e.setMode(mode);
        return e;
    }
}
