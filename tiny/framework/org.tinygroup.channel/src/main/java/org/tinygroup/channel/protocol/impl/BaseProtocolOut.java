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

import org.tinygroup.channel.protocol.ChannelClient;
import org.tinygroup.channel.protocol.ProtocolOutInterface;
import org.tinygroup.context.Context;
import org.tinygroup.context.util.ContextFactory;

@SuppressWarnings({"rawtypes", "unchecked"})
public class BaseProtocolOut<ProRequest, ProResponse> extends
        AbstractProtocolOut<ProRequest, ProResponse> implements
        ProtocolOutInterface<ProRequest, ProResponse> {
    private ChannelClient channelClient;

    public Object send(Object bizRequest) {
        Context context = ContextFactory.getContext();
        // 处理业务请求对象
        bizRequest = beforeSend(bizRequest, context);
        // 业务请求对象转协议请求对象
        ProRequest proRequest = (ProRequest) getProtocolTrans()
                .transBizReq2ProReq(bizRequest, context);
        // 处理协议请求对象
        proRequest = afterBizToRequestTrans(bizRequest, proRequest, context);
        // 发送请求
        ProResponse proResponse = (ProResponse) channelClient.send(proRequest);
        // 处理协议响应对象
        proResponse = afterSend(proRequest, proResponse, context);
        // 协议响应对象转业务响应对象
        Object bizResponse = getProtocolTrans().transProRes2BizRes(proResponse,
                context);
        // 处理业务响应对象
        bizResponse = afterResponseToBizTrans(proResponse, bizResponse, context);
        return bizResponse;
    }

    public void asynSend(Object bizRequest) {
        Context context = ContextFactory.getContext();
        // 处理业务对象
        bizRequest = beforeSend(bizRequest, context);
        // 业务对象转协议对象
        ProRequest proRequest = (ProRequest) getProtocolTrans()
                .transBizReq2ProReq(bizRequest, context);
        // 处理协议对象
        proRequest = afterBizToRequestTrans(bizRequest, proRequest, context);
        // 发送请求
        channelClient.asynSend(proRequest);
    }

    public ChannelClient getChannelClient() {
        return channelClient;
    }

    public void setChannelClient(ChannelClient client) {
        this.channelClient = client;
    }

}
