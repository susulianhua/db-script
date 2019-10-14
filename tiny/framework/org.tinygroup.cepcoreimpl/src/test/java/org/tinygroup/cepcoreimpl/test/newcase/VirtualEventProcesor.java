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
package org.tinygroup.cepcoreimpl.test.newcase;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.impl.AbstractEventProcessor;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceInfo;

import java.util.ArrayList;
import java.util.List;

public class VirtualEventProcesor extends AbstractEventProcessor {
    List<ServiceInfo> list = new ArrayList<ServiceInfo>();
    List<String> regex = new ArrayList<String>();
    int type;
    String id;

    public void process(Event event) {
        // do nothing

    }

    public void setCepCore(CEPCore cepCore) {
        // do nothing

    }

    public List<ServiceInfo> getServiceInfos() {
        return list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
        return false;
    }

    public void setEnable(boolean enable) {
    }

    public VirtualEventProcesor clone() {
        VirtualEventProcesor v = new VirtualEventProcesor();
        v.getServiceInfos().addAll(this.getServiceInfos());
        v.setType(this.getType());
        v.setId(this.getId());
        return v;

    }
}
