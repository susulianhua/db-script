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

/**
 * 消息服务中提供的客服信息
 *
 * @author yancheng11334
 */
public class CustomerServiceAccount {

    /**
     * 完整客服账号，格式为：账号前缀@公众号微信号
     */
    @JSONField(name = "kf_account")
    private String account;

    /**
     * 客服工号
     */
    @JSONField(name = "kf_id")
    private String id;

    /**
     * 客服昵称
     */
    @JSONField(name = "kf_nick")
    private String nick;

    /**
     * 客服头像URL
     */
    @JSONField(name = "kf_headimgurl")
    private String headimgurl;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }


}
