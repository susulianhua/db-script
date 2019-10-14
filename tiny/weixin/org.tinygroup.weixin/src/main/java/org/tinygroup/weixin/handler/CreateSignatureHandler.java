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
package org.tinygroup.weixin.handler;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.weixin.WeiXinContext;
import org.tinygroup.weixin.WeiXinHandlerMode;
import org.tinygroup.weixin.common.Client;
import org.tinygroup.weixin.common.PaySignature;

/**
 * 自动创建签名
 *
 * @author yancheng11334
 */
public class CreateSignatureHandler extends AbstractWeiXinHandler {

    public WeiXinHandlerMode getWeiXinHandlerMode() {
        return WeiXinHandlerMode.SEND_INPUT;
    }

    public <T> boolean isMatch(T message, WeiXinContext context) {
        return message instanceof PaySignature;
    }

    public <T> void process(T message, WeiXinContext context) {
        PaySignature signature = (PaySignature) message;
        if (StringUtil.isEmpty(signature.getSignature())) {
            Client client = context.get(Client.DEFAULT_CONTEXT_NAME);
            String sign = signature.createSignature(client.getPayToken());
            signature.setSignature(sign);
            //更新上下文的对象
            context.setInput(signature);
        }
    }

}
