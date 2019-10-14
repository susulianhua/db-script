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
package org.tinygroup.weixinpay.convert.xml;

import org.tinygroup.weixin.WeiXinContext;
import org.tinygroup.weixin.WeiXinConvertMode;
import org.tinygroup.weixin.convert.xml.AbstractXmlNodeConvert;
import org.tinygroup.weixin.exception.WeiXinException;
import org.tinygroup.weixinpay.result.QueryRefundResult;
import org.tinygroup.weixinpay.result.SingleRefundResult;
import org.tinygroup.xmlparser.node.XmlNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析微信退款查询结果(需要支持动态字段)
 *
 * @author yancheng11334
 */
public class QueryRefundResultConvert extends AbstractXmlNodeConvert {

    public QueryRefundResultConvert() {
        super(QueryRefundResult.class);
        setPriority(-100);
    }

    public WeiXinConvertMode getWeiXinConvertMode() {
        return WeiXinConvertMode.SEND;
    }

    protected boolean checkMatch(XmlNode input, WeiXinContext context) {
        return "SUCCESS".equals(get(input, "return_code")) && "SUCCESS".equals(get(input, "result_code")) && contains(input, "refund_count");
    }

    /**
     * 因为动态字段的问题，采用人工解析
     */
    @SuppressWarnings("unchecked")
    protected <OUTPUT> OUTPUT convertXmlNode(XmlNode input, WeiXinContext context) {
        try {
            QueryRefundResult result = new QueryRefundResult();

            //解析固定属性
            result.setReturnCode(get(input, "return_code"));
            result.setReturnMsg(get(input, "return_msg"));
            result.setAppId(get(input, "appid"));
            result.setMchId(get(input, "mch_id"));
            result.setDeviceId(get(input, "device_info"));
            result.setRandomString(get(input, "nonce_str"));
            result.setSignature(get(input, "sign"));
            result.setOrderId(get(input, "out_trade_no"));
            result.setTransactionId(get(input, "transaction_id"));
            result.setCurrencyType(get(input, "fee_type"));
            result.setTotalAmount(Integer.parseInt(get(input, "total_fee")));
            result.setCashAmount(Integer.parseInt(get(input, "cash_fee")));
            result.setRefundNumber(Integer.parseInt(get(input, "refund_count")));

            //退款笔数列表，至少存在一笔
            List<SingleRefundResult> refundResultList = new ArrayList<SingleRefundResult>();

            //解析动态属性
            for (int i = 0; i < result.getRefundNumber(); i++) {
                SingleRefundResult sr = new SingleRefundResult();
                sr.setOutRefundId(get(input, "out_refund_no_" + i));
                sr.setRefundId(get(input, "refund_id_" + i));
                sr.setRefundChannel(get(input, "refund_channel_" + i));
                sr.setRefundAmount(Integer.parseInt(get(input, "refund_fee_" + i)));
                sr.setRefundAccount(get(input, "refund_recv_accout_" + i));
                sr.setRefundStatus(get(input, "refund_status_" + i));
                refundResultList.add(sr);
            }

            result.setRefundResultList(refundResultList);

            return (OUTPUT) result;
        } catch (Exception e) {
            throw new WeiXinException(String.format("%s convert to class:%s is failed!", input.toString(), clazz.getName()), e);
        }
    }

}
