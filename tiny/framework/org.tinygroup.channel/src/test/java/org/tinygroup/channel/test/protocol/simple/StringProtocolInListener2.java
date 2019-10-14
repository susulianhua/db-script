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
package org.tinygroup.channel.test.protocol.simple;

import org.tinygroup.channel.protocol.ProtocolInListener;
import org.tinygroup.channel.test.protocol.util.ConstantUtil;
import org.tinygroup.context.Context;

public class StringProtocolInListener2 implements
        ProtocolInListener<String, String> {
    // public void pre(String r,Context context,ProtocolListenerChain chain) {
    // if(context.exist(ConstantUtil.KEY)){
    // context.put(ConstantUtil.KEY, context.get(ConstantUtil.KEY)+"String2");
    // }else{
    // context.put(ConstantUtil.KEY, "String2");
    // }
    // chain.pre(r, context);
    // }
    // public void post(String r,String r1,Context context,ProtocolListenerChain
    // chain) {
    //
    // chain.post(r, r1, context);
    // }
    public String getId() {
        return null;
    }

    public void setId(String id) {
    }

    public String afterReceived(String request, Context context) {
        if (context.exist(ConstantUtil.KEY)) {
            context.put(ConstantUtil.KEY, context.get(ConstantUtil.KEY)
                    + "String2");
        } else {
            context.put(ConstantUtil.KEY, "String2");
        }
        return request;
    }

    public Object afterRequestToBizTrans(String request, Object bizRequest,
                                         Context context) {

        return bizRequest;
    }

    public Object afterProcess(String request, Object bizResponse,
                               Context context) {

        return bizResponse;
    }

    public String afterBizToResponseTrans(Object bizResponse, String response,
                                          Context context) {

        return response;
    }

}
