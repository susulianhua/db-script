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
package org.tinygroup.channel.protocol.impl;

import org.tinygroup.channel.protocol.ProtocolProcess;
import org.tinygroup.context.Context;

@SuppressWarnings({"rawtypes", "unchecked"})
public class BaseProtocolIn<ProRequest, ProResponse> extends
        AbstractProtocolIn<ProRequest, ProResponse> {
    private ProtocolProcess protocolProcess;

    public ProtocolProcess getProtocolProcess() {
        return protocolProcess;
    }

    public void setProtocolProcess(ProtocolProcess protocolProcess) {
        this.protocolProcess = protocolProcess;
    }

    protected ProResponse realDeal(ProRequest proRequest, Context context) {
        // 处理协议对象
        proRequest = afterReceived(proRequest, context);
        // 协议对象转业务对象
        Object bizRequest = getProtocolTrans().transProReq2BizReq(proRequest,
                context);
        // 处理业务对象
        bizRequest = afterRequestToBizTrans(proRequest, bizRequest, context);
        // 执行业务逻辑
        Object bizResponse = getProtocolProcess().process(bizRequest, context);
        // 处理业务响应对象
        bizResponse = afterProcess(proRequest, bizResponse, context);
        // 业务响应对象转协议对象
        ProResponse proResponse = (ProResponse) getProtocolTrans()
                .transBizRes2ProRes(bizResponse, context);
        // 处理协议对象
        proResponse = afterBizToResponseTrans(bizResponse, proResponse, context);
        return proResponse;
    }

}
