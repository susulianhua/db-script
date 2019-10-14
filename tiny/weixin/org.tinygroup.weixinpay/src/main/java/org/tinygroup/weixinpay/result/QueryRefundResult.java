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
import org.tinygroup.weixin.common.ToServerResult;
import org.tinygroup.weixinpay.pojo.BusinessResultPojo;

import java.util.List;

/**
 * 查询退款的结果
 *
 * @author yancheng11334
 */
@XStreamAlias("xml")
public class QueryRefundResult extends BusinessResultPojo implements ToServerResult {

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
     * 货币类型
     */
    @XStreamAlias("fee_type")
    private String currencyType;

    /**
     * 总金额，单位为分
     */
    @XStreamAlias("total_fee")
    private int totalAmount;

    /**
     * 现金支付金额，单位为分
     */
    @XStreamAlias("cash_fee")
    private int cashAmount;

    /**
     * 退款笔数
     */
    @XStreamAlias("refund_count")
    private int refundNumber;

    /**
     * 微信退款记录列表
     */
    private List<SingleRefundResult> refundResultList;

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

    public List<SingleRefundResult> getRefundResultList() {
        return refundResultList;
    }

    public void setRefundResultList(List<SingleRefundResult> refundResultList) {
        this.refundResultList = refundResultList;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(int cashAmount) {
        this.cashAmount = cashAmount;
    }

    public int getRefundNumber() {
        return refundNumber;
    }

    public void setRefundNumber(int refundNumber) {
        this.refundNumber = refundNumber;
    }

}
