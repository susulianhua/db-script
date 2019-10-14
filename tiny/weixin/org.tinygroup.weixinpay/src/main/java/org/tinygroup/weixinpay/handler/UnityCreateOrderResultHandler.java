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
package org.tinygroup.weixinpay.handler;

import org.tinygroup.weixin.WeiXinContext;
import org.tinygroup.weixin.WeiXinHandlerMode;
import org.tinygroup.weixin.common.Client;
import org.tinygroup.weixin.handler.AbstractWeiXinHandler;
import org.tinygroup.weixin.util.WeiXinSignatureUtil;
import org.tinygroup.weixinpay.pojo.ChoosePayRequest;
import org.tinygroup.weixinpay.result.UnityCreateOrderResult;

/**
 * 根据统一下单的报文结果包装生成支付申请
 *
 * @author yancheng11334
 */
public class UnityCreateOrderResultHandler extends AbstractWeiXinHandler {

    public WeiXinHandlerMode getWeiXinHandlerMode() {
        return WeiXinHandlerMode.SEND_OUPUT;
    }

    public <T> boolean isMatch(T message, WeiXinContext context) {
        //LOGGER.logMessage(LogLevel.INFO, "message="+message.getClass().getName());
        return message instanceof UnityCreateOrderResult;
    }

    public <T> void process(T message, WeiXinContext context) {
        UnityCreateOrderResult orderResult = (UnityCreateOrderResult) message;
        //LOGGER.logMessage(LogLevel.INFO, "businessCode="+orderResult.getBusinessCode()+" errorCode="+orderResult.getErrorCode()+" errorMsg="+orderResult.getErrorMsg());
        if ("SUCCESS".equals(orderResult.getBusinessCode())) {

            ChoosePayRequest choosePayResult = new ChoosePayRequest();

            Client client = context.get(Client.DEFAULT_CONTEXT_NAME);
            choosePayResult.setAppId(client.getAppId());
            choosePayResult.setPrepayId("prepay_id=" + orderResult.getPrepayId());
            choosePayResult.setNonceStr(orderResult.getRandomString());
            choosePayResult.setSignType("MD5");
            choosePayResult.setTimeStamp(String.valueOf(System
                    .currentTimeMillis() / 1000));

            //生成签名
            String signature = WeiXinSignatureUtil.createPaySignature(choosePayResult, client.getPayToken());
            choosePayResult.setPaySign(signature);

            //LOGGER.logMessage(LogLevel.INFO, "signature="+signature);
            //更新结果
            context.setOutput(choosePayResult);
        }
    }

}
