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
package org.tinygroup.weixinpay.result;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 每笔红包信息
 *
 * @author yancheng11334
 */
@XStreamAlias("hbinfo")
public class RedEnvelopeInfo {

    /**
     * 领取红包的Openid
     */
    @XStreamAlias("openid")
    private String openId;

    /**
     * 红包状态
     */
    private String status;

    /**
     * 红包金额，单位为分
     */
    private int amount;

    /**
     * 领取红包时间
     */
    @XStreamAlias("rcv_time")
    private String receiveTime;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

}
