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

/**
 * 统一下单的反馈结果
 *
 * @author yancheng11334
 */
@XStreamAlias("xml")
public class UnityCreateOrderResult extends BusinessResultPojo implements ToServerResult {

    /**
     * 交易类型
     */
    @XStreamAlias("trade_type")
    private String tradeType;

    /**
     * 预支付会话Id
     */
    @XStreamAlias("prepay_id")
    private String prepayId;

    /**
     * 二维码URL
     */
    @XStreamAlias("code_url")
    private String codeUrl;


    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }


}
