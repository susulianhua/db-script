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
package org.tinygroup.trans.xstream.springtrans;

import org.springframework.messaging.Message;
import org.tinygroup.trans.xstream.util.XStreamConvertUtil;

public class XStreamTransformService {
    private static final String XSTREAM_SCENE = "BIZ_SCENE";

    /**
     * xml转对象，采用xstream的方式
     *
     * @param inMessage
     * @return
     */
    public Object transfor2Object(Message<?> inMessage) {
        String scene = (String) inMessage.getHeaders().get(XSTREAM_SCENE);
        return (Object) XStreamConvertUtil.convert(inMessage.getPayload(),
                scene);
    }

    /**
     * 对象转xml，采用xstream的方式
     *
     * @param inMessage
     * @return
     */
    public String transfor2Xml(Message<?> inMessage) {
        String scene = (String) inMessage.getHeaders().get(XSTREAM_SCENE);
        return (String) XStreamConvertUtil.convert(inMessage.getPayload(),
                scene);
    }

}
