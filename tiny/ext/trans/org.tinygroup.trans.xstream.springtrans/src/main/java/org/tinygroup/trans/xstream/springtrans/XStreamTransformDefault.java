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

import com.thoughtworks.xstream.XStream;
import org.springframework.messaging.Message;
import org.tinygroup.trans.xstream.util.XStreamConvertUtil;
import org.tinygroup.xstream.XStreamFactory;

public class XStreamTransformDefault {
    private String xstreamPackageName;

    public String getXstreamPackageName() {
        return xstreamPackageName;
    }

    public void setXstreamPackageName(String xstreamPackageName) {
        this.xstreamPackageName = xstreamPackageName;
    }

    /**
     * xml转对象，采用xstream的方式
     *
     * @param inMessage
     * @return
     */
    public Object transfor2Object(Message<?> inMessage) {
        XStream xStream = XStreamFactory.getXStream(xstreamPackageName);
        return XStreamConvertUtil.xml2Object(xStream,
                (String) inMessage.getPayload());
    }

    /**
     * 对象转xml，采用xstream的方式
     *
     * @param inMessage
     * @return
     */
    public String transfor2Xml(Message<?> inMessage) {
        XStream xStream = XStreamFactory.getXStream(xstreamPackageName);
        return XStreamConvertUtil.object2Xml(xStream, inMessage.getPayload());
    }

}
