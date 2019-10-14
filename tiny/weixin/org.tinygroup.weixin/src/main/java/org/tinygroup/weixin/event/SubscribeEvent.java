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
package org.tinygroup.weixin.event;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.tinygroup.weixin.util.WeiXinEventMode;

/**
 * 订阅事件模型
 *
 * @author yancheng11334
 */
@XStreamAlias("xml")
public class SubscribeEvent extends CommonEvent {

    @XStreamAlias("EventKey")
    private String eventKey;
    @XStreamAlias("Ticket")
    private String ticket;

    public SubscribeEvent() {
        super();
        setEvent(WeiXinEventMode.SUBSCRIBE.getEvent());
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

}
