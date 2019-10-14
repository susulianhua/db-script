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
package org.tinygroup.channel.protocol;

import org.tinygroup.context.Context;

public interface ProtocolTrans<BizRequest, ProRequest, ProResponse, BizResponse> {
    /**
     * 业务请求对象转协议请求对象
     *
     * @param bizRequest
     * @param context
     * @return
     */
    ProRequest transBizReq2ProReq(BizRequest bizRequest, Context context);

    /**
     * 协议请求对象转业务请求对象
     *
     * @param proRequest
     * @param context
     * @return
     */
    BizRequest transProReq2BizReq(ProRequest proRequest, Context context);

    /**
     * 协议响应对象转业务响应对象
     *
     * @param proResponse
     * @param context
     * @return
     */
    BizResponse transProRes2BizRes(ProResponse proResponse, Context context);

    /**
     * 业务响应对象转协议对象
     *
     * @param bizResponse
     * @param context
     * @return
     */
    ProResponse transBizRes2ProRes(BizResponse bizResponse, Context context);

    // Req transReqFromMessage(ReqMessage reqMessage, Context context);
    // ReqMessage transReqToMessage(Req req, Context context);
    //
    // RespMessage transRespToMessage(Resp resp, Context context);
    // Resp transRespFromMessage(RespMessage respMessage, Context context);

}
