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

public interface ProtocolOutListener<ProRequest, ProResponse> {
    // void pre(Request request,Context context,ProtocolListenerChain chain);
    // void afterTrans(Request request,Object protocolObject,Context
    // context,ProtocolListenerChain chain);
    // void post(Request request,Response response,Context
    // context,ProtocolListenerChain chain);
    String getId();

    void setId(String id);

    // exp: bizrequest = xml
    Object beforeSend(Object bizRequest, Context context);

    // exp: trans : request :fix = trans(bizObject:xml)
    ProRequest afterBizToRequestTrans(Object bizRequest, ProRequest request,
                                      Context context);

    // response：fix = send( request：fix )
    ProResponse afterSend(ProRequest request, ProResponse response,
                          Context context);

    // exp: trans : bizResponse：xml = trans(response:fix)
    Object afterResponseToBizTrans(ProResponse response, Object bizResponse,
                                   Context context);

}
