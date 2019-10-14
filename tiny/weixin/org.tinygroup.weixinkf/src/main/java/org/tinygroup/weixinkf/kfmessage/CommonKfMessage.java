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
package org.tinygroup.weixinkf.kfmessage;

import com.alibaba.fastjson.annotation.JSONField;
import org.tinygroup.weixin.common.ToServerMessage;
import org.tinygroup.weixinkf.kfaccount.CustomerServiceAccount;

/**
 * 客服消息统一接口
 *
 * @author yancheng11334
 */
public class CommonKfMessage implements ToServerMessage {

    @JSONField(name = "touser")
    private String toUser;

    @JSONField(name = "msgtype")
    private String msgType;
    //客服消息可以指定客服信息，如果为空则为微信号
    @JSONField(name = "customservice")
    private CustomerServiceAccount kfAccount;

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public CustomerServiceAccount getKfAccount() {
        return kfAccount;
    }

    public void setKfAccount(CustomerServiceAccount kfAccount) {
        this.kfAccount = kfAccount;
    }

    @JSONField(serialize = false)
    public String getWeiXinKey() {
        return "sendKf";
    }

}
