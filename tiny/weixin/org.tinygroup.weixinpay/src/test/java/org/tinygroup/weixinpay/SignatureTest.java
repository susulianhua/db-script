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
package org.tinygroup.weixinpay;

import junit.framework.TestCase;
import org.tinygroup.weixin.util.WeiXinSignatureUtil;
import org.tinygroup.weixinpay.message.UnityCreateOrder;
import org.tinygroup.weixinpay.pojo.ChoosePayRequest;

/**
 * 签名的测试
 *
 * @author yancheng11334
 */
public class SignatureTest extends TestCase {

    /**
     * 测试统一下单的动态签名
     */
    public void testUnityCreateOrder() {
        UnityCreateOrder order = new UnityCreateOrder();
        order.setAppId("wx2421b1c4370ec43b");
        order.setAdditionalData("支付测试");
        order.setShortDescription("JSAPI支付测试");
        order.setMchId("10000100");
        order.setRandomString("1add1a30ac87aa2db72f57a2375d8fec");
        order.setNotifyUrl("http://wxpay.weixin.qq.com/pub_v2/pay/notify.v2.php");
        order.setOpenId("oUpF8uMuAJO_M2pxb1Q9zNjWeS6o");
        order.setOrderId("1415659990");
        order.setTerminalIp("14.23.150.211");
        order.setTotalAmount(1);
        order.setTradeType("JSAPI");
        System.out.println(order);

        assertNull(order.getSignature());
        String signature = order.createSignature("abct123");
        assertEquals("029A143D40192A41F5562D5C68BB331F", signature);
    }

    /**
     * 测试微信提交支付的测试用例
     */
    public void testChoosePayRequest() {
        ChoosePayRequest request = new ChoosePayRequest();
        request.setAppId("wx325952ab42939270");
        request.setNonceStr("9876543210");
        request.setPrepayId("prepay_id=wx201512101533157829e938230509763133");  //微信比较奇葩，需要增加prepay_id=
        request.setSignType("MD5");
        request.setTimeStamp("1449732795");

        String signature = WeiXinSignatureUtil.createPaySignature(request, "abcdefg");

        assertEquals("82195C41954C5D5C8516B3A1B224BBDA", signature);
    }

}
