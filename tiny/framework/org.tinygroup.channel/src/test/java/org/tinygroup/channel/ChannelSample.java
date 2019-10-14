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
package org.tinygroup.channel;

import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.channel.impl.AbstractChannel;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceInfo;

import java.util.ArrayList;
import java.util.List;

public class ChannelSample extends AbstractChannel {

    protected void receive(Event event) {
        System.out.println(String.format(
                "I am receive Service<id:%s >", event
                        .getServiceRequest().getServiceId()));
    }

    public int getType() {
        return EventProcessor.TYPE_REMOTE;
    }

    public List<ServiceInfo> getServiceInfos() {
        return new ArrayList<ServiceInfo>();
    }

    public int getWeight() {
        // TODO Auto-generated method stub
        return 0;
    }

    public List<String> getRegex() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isRead() {
        // TODO Auto-generated method stub
        return true;
    }

    public void setRead(boolean read) {
        // TODO Auto-generated method stub

    }

    public boolean isEnable() {
        return true;
    }

    public void setEnable(boolean enable) {
    }


}
