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
package org.tinygroup.weixinkf.kfaccount;

import com.alibaba.fastjson.annotation.JSONField;
import org.tinygroup.convert.objectjson.fastjson.ObjectToJson;
import org.tinygroup.weixin.common.ToServerMessage;

/**
 * 客服管理中的客服账户
 *
 * @author yancheng11334
 */
public abstract class ManageCustomerAccount implements ToServerMessage {

    //完整客服账号，格式为：账号前缀@公众号微信号
    @JSONField(name = "kf_account")
    String accountId;
    //客服昵称，最长6个汉字或12个英文字符
    @JSONField(name = "nickname")
    String nickName;
    //客服账号登录密码，格式为密码明文的32位加密MD5值
    String password;

    public ManageCustomerAccount() {
        this(null, null, null);
    }

    public ManageCustomerAccount(String accountId) {
        this(accountId, null, null);
    }

    public ManageCustomerAccount(String accountId, String nickName, String password) {
        this.accountId = accountId;
        this.nickName = nickName;
        this.password = password;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        ObjectToJson<ManageCustomerAccount> json = new ObjectToJson<ManageCustomerAccount>();
        return json.convert(this);
    }

}
