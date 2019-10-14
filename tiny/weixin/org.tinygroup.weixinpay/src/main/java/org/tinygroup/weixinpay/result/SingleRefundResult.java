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
 * 单笔退款记录
 *
 * @author yancheng11334
 */
public class SingleRefundResult {

    /**
     * 商品退款号
     */
    @XStreamAlias("out_refund_no")
    private String outRefundId;

    /**
     * 微信退款号
     */
    @XStreamAlias("refund_id")
    private String refundId;

    /**
     * 微信退款渠道
     */
    @XStreamAlias("refund_channel")
    private String refundChannel;

    /**
     * 退款金额，单位为分
     */
    @XStreamAlias("refund_fee")
    private int refundAmount;

    /**
     * 退款账户信息
     */
    @XStreamAlias("refund_recv_accout")
    private String refundAccount;

    /**
     * 退款状态
     */
    @XStreamAlias("refund_status")
    private String refundStatus;

    public String getOutRefundId() {
        return outRefundId;
    }

    public void setOutRefundId(String outRefundId) {
        this.outRefundId = outRefundId;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public String getRefundChannel() {
        return refundChannel;
    }

    public void setRefundChannel(String refundChannel) {
        this.refundChannel = refundChannel;
    }

    public int getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(int refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getRefundAccount() {
        return refundAccount;
    }

    public void setRefundAccount(String refundAccount) {
        this.refundAccount = refundAccount;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

}
