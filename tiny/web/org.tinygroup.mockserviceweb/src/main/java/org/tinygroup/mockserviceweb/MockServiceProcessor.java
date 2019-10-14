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
package org.tinygroup.mockserviceweb;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.context.Context;
import org.tinygroup.event.Event;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.mockservice.Hession;
import org.tinygroup.weblayer.AbstractTinyProcessor;
import org.tinygroup.weblayer.WebContext;

import javax.servlet.ServletException;
import java.io.IOException;

public class MockServiceProcessor extends AbstractTinyProcessor {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(MockServiceProcessor.class);
    CEPCore core;

    @Override
    protected void customInit() throws ServletException {

    }

    @Override
    public void reallyProcess(String urlString, WebContext context)
            throws ServletException, IOException {
        int lastSplash = urlString.lastIndexOf('/');
        int lastDot = urlString.lastIndexOf('.');
        String serviceId = urlString.substring(lastSplash + 1, lastDot);
        Event event = callService(serviceId, context);
        event.setType(Event.EVENT_TYPE_RESPONSE);
        if (urlString.endsWith("mockservice")) {// 返回xml
            context.getResponse().getOutputStream().write(Hession.serialize(event));
        }
    }

    private Event callService(String serviceId, Context context) {
        Event event = null;
        try {
            event = (Event) Hession.deserialize((byte[]) context.get("TINY_MOCK_SERVICE"));
        } catch (IOException e1) {
            throw new RuntimeException("请求:" + serviceId + "对象反序列化失败", e1);
        }
        try {
            core.process(event);
        } catch (Exception e1) {
            LOGGER.errorMessage("请求:{}执行异常", e1, serviceId);
            event.setThrowable(e1);
        }
        return event;
    }

    public CEPCore getCore() {
        return core;
    }

    public void setCore(CEPCore core) {
        this.core = core;
    }

}
