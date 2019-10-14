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
import org.tinygroup.weixinpay.message.PayResultMessage;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 微信支付结果的转换器
 *
 * @author yancheng11334
 */
public class PayResultMessageConvert extends AbstractXmlNodeConvert {

    public PayResultMessageConvert() {
        super(PayResultMessage.class);
    }

    public WeiXinConvertMode getWeiXinConvertMode() {
        return WeiXinConvertMode.RECEIVE;
    }

    protected boolean checkMatch(XmlNode input, WeiXinContext context) {
        return "SUCCESS".equals(get(input, "return_code"))
                && "SUCCESS".equals(get(input, "result_code"))
                && contains(input, "openid") && contains(input, "cash_fee");
    }

}
