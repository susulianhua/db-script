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

import org.junit.Assert;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.cepcore.impl.AbstractEventProcessor;
import org.tinygroup.cepcore.util.ThreadContextUtil;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ThreadLocalServiceProcesor extends AbstractEventProcessor {
    private final static Logger LOGGER = LoggerFactory.getLogger(ThreadLocalServiceProcesor.class);
    List<ServiceInfo> list = new ArrayList<ServiceInfo>();
    List<String> regex = new ArrayList<String>();
    String id;

    public void process(Event event) {
        LOGGER.errorMessage("thread local a = {}", ThreadContextUtil.get("a"));
        System.out.println("thread local a = " + ThreadContextUtil.get("a"));
        Assert.assertEquals("a", ThreadContextUtil.get("a"));
    }

    public void setCepCore(CEPCore cepCore) {
        // do nothing

    }

    public List<ServiceInfo> getServiceInfos() {
        return list;
    }

    public String getId() {
        return ThreadLocalServiceProcesor.class.getName();
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return EventProcessor.TYPE_LOCAL;
    }

    public void setType(int type) {

    }

    public int getWeight() {
        return 0;
    }

    public List<String> getRegex() {
        return regex;
    }

    public boolean isRead() {
        return false;
    }

    public void setRead(boolean read) {

    }

    public boolean isEnable() {
        return true;
    }

    public void setEnable(boolean enable) {
    }

    public ThreadLocalServiceProcesor clone() {
        ThreadLocalServiceProcesor v = new ThreadLocalServiceProcesor();
        v.getServiceInfos().addAll(this.getServiceInfos());
        v.setType(this.getType());
        v.setId(this.getId());
        return v;

    }
}
