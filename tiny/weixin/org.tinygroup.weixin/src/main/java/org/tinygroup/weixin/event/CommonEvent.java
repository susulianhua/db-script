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
import org.tinygroup.weixin.common.FromServerMessage;

/**
 * 微信服务器推送事件
 *
 * @author yancheng11334
 */
public class CommonEvent implements FromServerMessage {

    @XStreamAlias("FromUserName")
    private String fromUserName;
    @XStreamAlias("MsgType")
    private String msgType;
    @XStreamAlias("CreateTime")
    private int createTime;
    @XStreamAlias("ToUserName")
    private String toUserName;
    @XStreamAlias("Event")
    private String event;

    public CommonEvent() {
        setMsgType("event");
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

}
