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
package org.tinygroup.weixinpay.message;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.tinygroup.weixin.common.ToServerMessage;
import org.tinygroup.weixin.handler.AbstactPaySignature;

/**
 * 支付退款<br>
 * 详细字段参考：https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_4
 *
 * @author yancheng11334
 */
@XStreamAlias("xml")
public class Refund extends AbstactPaySignature implements ToServerMessage {

    /**
     * 公众账号ID
     */
    @XStreamAlias("appid")
    private String appId;

    /**
     * 商户号
     */
    @XStreamAlias("mch_id")
    private String mchId;

    /**
     * 设备号
     */
    @XStreamAlias("device_info")
    private String deviceId;

    /**
     * 随机字符串
     */
    @XStreamAlias("nonce_str")
    private String randomString;

    /**
     * 签名
     */
    @XStreamAlias("sign")
    private String signature;

    /**
     * 商品订单号
     */
    @XStreamAlias("out_trade_no")
    private String orderId;

    /**
     * 微信订单号
     */
    @XStreamAlias("transaction_id")
    private String transactionId;

    /**
     * 商品退款号
     */
    @XStreamAlias("out_refund_no")
    private String outRefundId;

    /**
     * 总金额，单位为分
     */
    @XStreamAlias("total_fee")
    private int totalAmount;

    /**
     * 退款金额，单位为分
     */
    @XStreamAlias("refund_fee")
    private int refundAmount;

    /**
     * 退款货币类型
     */
    @XStreamAlias("refund_fee_type")
    private String refundFeeType;

    /**
     * 操作员ID
     */
    @XStreamAlias("op_user_id")
    private String operatorId;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getRandomString() {
        return randomString;
    }

    public void setRandomString(String randomString) {
        this.randomString = randomString;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOutRefundId() {
        return outRefundId;
    }

    public void setOutRefundId(String outRefundId) {
        this.outRefundId = outRefundId;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(int refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getRefundFeeType() {
        return refundFeeType;
    }

    public void setRefundFeeType(String refundFeeType) {
        this.refundFeeType = refundFeeType;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getWeiXinKey() {
        return "refund";
    }

    public String toString() {
        XStream xstream = new XStream(new XppDriver(new NoNameCoder()));
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.processAnnotations(getClass());
        return xstream.toXML(this);
    }
}
