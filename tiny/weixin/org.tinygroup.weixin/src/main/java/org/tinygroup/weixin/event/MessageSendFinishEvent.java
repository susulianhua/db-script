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
 * 群发消息发送结束事件
 *
 * @author yancheng11334
 */
@XStreamAlias("xml")
public class MessageSendFinishEvent extends CommonEvent {

    @XStreamAlias("MsgID")
    private String msgId;
    @XStreamAlias("Status")
    private String status;
    @XStreamAlias("TotalCount")
    private int totalCount;
    @XStreamAlias("FilterCount")
    private int filterCount;
    @XStreamAlias("SentCount")
    private int sentCount;
    @XStreamAlias("ErrorCount")
    private int errorCount;

    public MessageSendFinishEvent() {
        super();
        setEvent(WeiXinEventMode.MASS_SEND_JOB_FINISH.getEvent());
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getFilterCount() {
        return filterCount;
    }

    public void setFilterCount(int filterCount) {
        this.filterCount = filterCount;
    }

    public int getSentCount() {
        return sentCount;
    }

    public void setSentCount(int sentCount) {
        this.sentCount = sentCount;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

}
