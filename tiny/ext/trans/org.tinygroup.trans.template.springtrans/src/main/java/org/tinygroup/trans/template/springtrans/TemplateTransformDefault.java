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
package org.tinygroup.trans.template.springtrans;

import org.springframework.messaging.Message;
import org.tinygroup.trans.template.util.TemplateConvertUtil;

public class TemplateTransformDefault {
    private static final String TEMPLATE_SCENE = "BIZ_SCENE";

    /**
     * 对象转xml，采用template的方式
     *
     * @param inMessage
     * @return
     */
    public String transfor2Str(Message<?> inMessage) {
        String scene = (String) inMessage.getHeaders().get(TEMPLATE_SCENE);
        return TemplateConvertUtil.convert(inMessage.getPayload(), scene);
    }

}
