/**
 * Copyright (c) 2012-2016, www.tinygroup.org (luo_guo@icloud.com).
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
package org.tinygroup.flowbasiccomponent.test.testcase.datautil;

import org.tinygroup.context.Context;
import org.tinygroup.context.util.ContextFactory;
import org.tinygroup.event.Event;
import org.tinygroup.flowbasiccomponent.test.AbstractFlowComponent;

/**
 * 系统指令执行
 *
 * @author qiucn
 */
public class LoggerPrintTest extends AbstractFlowComponent {

    public void testLoggerPrintInfo() {
        Context context = ContextFactory.getContext();
        context.put("logLevel", "info");
        Event e = Event.createEvent("loggerPrintTestFlow", context);
        cepcore.process(e);
    }

    public void testLoggerPrintDebug() {
        Context context = ContextFactory.getContext();
        context.put("logLevel", "debug");
        Event e = Event.createEvent("loggerPrintTestFlow", context);
        cepcore.process(e);
    }

    public void testLoggerPrintError() {
        Context context = ContextFactory.getContext();
        context.put("logLevel", "error");
        Event e = Event.createEvent("loggerPrintTestFlow", context);
        cepcore.process(e);
    }
}
