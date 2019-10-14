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
package org.tinygroup.weixin.message;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.tinygroup.weixin.common.FromServerMessage;

/**
 * 通用消息<br>
 * 微信消息有三种模式：非加密、加密、兼容，兼容模式会同时包含前两者内容。
 *
 * @author yancheng11334
 */
public class CommonMessage implements FromServerMessage {

    @XStreamAlias("FromUserName")
    private String fromUserName;

    @XStreamAlias("MsgType")
    private String msgType;

    @XStreamAlias("CreateTime")
    private int createTime;

    @XStreamAlias("MsgId")
    private long msgId;

    @XStreamAlias("Encrypt")
    private String encrypt;

    @XStreamAlias("ToUserName")
    private String toUserName;

    @XStreamAlias("URL")
    private String url;

    public String getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(String encrypt) {
        this.encrypt = encrypt;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
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

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
