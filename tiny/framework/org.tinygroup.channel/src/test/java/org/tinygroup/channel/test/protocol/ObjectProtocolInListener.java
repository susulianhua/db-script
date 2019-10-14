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
package org.tinygroup.channel.test.protocol;

import com.google.common.util.concurrent.RateLimiter;
import org.tinygroup.channel.protocol.ProtocolInListener;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.context.Context;

public class ObjectProtocolInListener implements
        ProtocolInListener<Object, Object> {
    // private final static String RATE_LIMIT = "rate_limit";
    private RateLimiter rateLimiter = null;
    private String rate;

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
        if (StringUtil.isBlank(rate)) {
            return;
        }
        rateLimiter = RateLimiter.create(Double.parseDouble(rate));
    }

    // public void pre(Object r, Context context, ProtocolListenerChain chain) {
    // if(rateLimiter!=null&&!rateLimiter.tryAcquire()){
    // throw new RuntimeException("达到流量限制");
    // }
    // chain.pre(r, context);
    // }
    //
    // public void post(Object r, Object r1, Context context,
    // ProtocolListenerChain chain) {
    //
    // chain.post(r, r1, context);
    // }

    public String getId() {
        return null;
    }

    public void setId(String id) {
    }

    public Object afterReceived(Object request, Context context) {
        return request;
    }

    public Object afterRequestToBizTrans(Object bizRContext, Object request,
                                         Context context) {
        return request;
    }

    public Object afterProcess(Object request, Object bizResponse,
                               Context context) {
        return bizResponse;
    }

    public Object afterBizToResponseTrans(Object bizResponse, Object response,
                                          Context context) {
        return response;
    }

}
