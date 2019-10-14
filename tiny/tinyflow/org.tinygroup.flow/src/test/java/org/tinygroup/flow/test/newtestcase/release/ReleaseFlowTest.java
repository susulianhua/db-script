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
package org.tinygroup.flow.test.newtestcase.release;

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.event.Event;
import org.tinygroup.flow.component.AbstractFlowComponent;
import org.tinygroup.flow.release.FlowReleaseManager;
import org.tinygroup.flow.release.config.FlowRelease;
import org.tinygroup.flow.release.config.ReleaseItem;
import org.tinygroup.tinyrunner.Runner;

import java.util.ArrayList;
import java.util.List;

public class ReleaseFlowTest extends AbstractFlowComponent {


    protected void setUp() throws Exception {
        FlowReleaseManager.clear();
        FlowRelease flowRelease = new FlowRelease();
        ReleaseItem item = new ReleaseItem();
        List<String> flowNames = new ArrayList<String>();
        flowNames.add("releaseFlow");
        item.setItems(flowNames);
        flowRelease.setIncludes(item);
        FlowReleaseManager.add(flowRelease);
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        FlowReleaseManager.clear();
        Runner.initDirect("application.xml", new ArrayList<String>());
        super.setUp();
    }

    public void testInclude1() {
        Context context = new ContextImpl();
        context.put("a", 1);
        context.put("b", 2);
        flowExecutor.execute("releaseFlow", "begin", context);
        assertEquals(3, Integer.valueOf(context.get("sum").toString()).intValue());

        Context context2 = new ContextImpl();
        context2.put("a", 1);
        context2.put("b", 2);
        Event e = Event.createEvent("releaseFlow", context2);
        cepcore.process(e);
        assertEquals(3, Integer.valueOf(e.getServiceRequest().getContext().get("sum").toString()).intValue());

    }

}
