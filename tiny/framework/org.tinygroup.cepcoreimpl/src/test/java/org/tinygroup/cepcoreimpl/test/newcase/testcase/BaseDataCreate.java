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
package org.tinygroup.cepcoreimpl.test.newcase.testcase;

import junit.framework.TestCase;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.cepcoreimpl.test.newcase.VirtualEventProcesor;
import org.tinygroup.cepcoreimpl.test.newcase.VirtualService;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.event.ServiceInfo;

import java.util.ArrayList;
import java.util.List;

public class BaseDataCreate extends TestCase {
    protected List<ServiceInfo> getService(String ids) {
        List<ServiceInfo> list = new ArrayList<ServiceInfo>();
        String[] service = ids.split(",");
        for (String id : service) {
            VirtualService v = new VirtualService(id);
            list.add(v);
        }
        return list;
    }

    protected EventProcessor getEventProcessor(String id, int type, String ids) {
        VirtualEventProcesor eventProcessor = new VirtualEventProcesor();
        eventProcessor.setId(id);
        eventProcessor.getServiceInfos().addAll(getService(ids));
        eventProcessor.setType(type);
        return eventProcessor;
    }

    protected List<String> trans(String str) {

        List<String> list = new ArrayList<String>();
        if (StringUtil.isBlank(str)) {
            return list;
        }
        String[] array = str.split(",");
        for (String s : array) {
            list.add(s);
        }
        return list;
    }

    protected void registerEventProcessor(CEPCore cepcore, String id, int type,
                                          String ids) {
        EventProcessor processorLocal = getEventProcessor(id, type, ids);
        cepcore.registerEventProcessor(processorLocal);
    }

    protected void unRegisterEventProcessor(CEPCore cepcore, String id,
                                            int type, String ids) {
        EventProcessor processorRemote = getEventProcessor(id, type, ids);
        cepcore.unregisterEventProcessor(processorRemote);
    }

    public void testVoid() {
    }
}
