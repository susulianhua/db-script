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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.tinygroup.weixin.common.ToServerMessage;
import org.tinygroup.weixin.handler.AbstactPaySignature;

/**
 * 发送普通微信红包<br>
 * 详细字段参考：https://pay.weixin.qq.com/wiki/doc/api/cash_coupon.php?chapter=13_5
 *
 * @author yancheng11334
 */
@XStreamAlias("xml")
public class CommonRedEnvelope extends AbstactPaySignature implements ToServerMessage {

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
    @XStreamAlias("mch_billno")
    private String orderId;

    /**
     * 商户号
     */
    @XStreamAlias("mch_id")
    private String mchId;

    /**
     * 公众账号ID
     */
    @XStreamAlias("wxappid")
    private String appId;

    /**
     * 发送红包的商户名称
     */
    @XStreamAlias("send_name")
    private String sendName;

    /**
     * 接收红包的微信用户id
     */
    @XStreamAlias("re_openid")
    private String openId;

    /**
     * 红包金额，单位为分
     */
    @XStreamAlias("total_amount")
    private int totalAmount;

    /**
     * 总人数
     */
    @XStreamAlias("total_num")
    private int totalNum;

    /**
     * 红包祝福语
     */
    private String wishing;

    /**
     * 终端IP
     */
    @XStreamAlias("client_ip")
    private String terminalIp;

    /**
     * 红包活动名称
     */
    @XStreamAlias("act_name")
    private String actionName;

    /**
     * 备注
     */
    private String remark;

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

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public String getWishing() {
        return wishing;
    }

    public void setWishing(String wishing) {
        this.wishing = wishing;
    }

    public String getTerminalIp() {
        return terminalIp;
    }

    public void setTerminalIp(String terminalIp) {
        this.terminalIp = terminalIp;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getWeiXinKey() {
        return "commonRedEnvelope";
    }

}
