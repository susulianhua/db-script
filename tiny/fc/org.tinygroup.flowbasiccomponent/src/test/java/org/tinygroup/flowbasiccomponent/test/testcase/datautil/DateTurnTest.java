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
import org.tinygroup.flowbasiccomponent.util.DateTurnUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * @author qiucn
 */
public class DateTurnTest extends AbstractFlowComponent {

    /**
     *
     */
    public void testAddDateTurn() {
        Context context = ContextFactory.getContext();
        Date date = new Date();
        context.put("dateTime", date.getTime());
        context.put("num", 2);
        context.put("calendarField", Calendar.DAY_OF_MONTH);
        context.put("resultKey", "result");
        context.put("result", "");
        Event e = Event.createEvent("dateTurnTestFlow", context);
        cepcore.process(e);
        assertEquals(DateTurnUtil.dateTurn(date, 2, Calendar.DAY_OF_MONTH)
                .getTime(), e.getServiceRequest().getContext().get("result"));
    }

    /**
     *
     */
    public void testMinDateTurn() {
        Context context = ContextFactory.getContext();
        Date date = new Date();
        context.put("dateTime", date.getTime());
        context.put("num", -2);
        context.put("resultKey", "result");
        context.put("calendarField", Calendar.DAY_OF_MONTH);
        context.put("result", "");
        Event e = Event.createEvent("dateTurnTestFlow", context);
        cepcore.process(e);
        assertEquals(DateTurnUtil.dateTurn(date, -2, Calendar.DAY_OF_MONTH)
                .getTime(), e.getServiceRequest().getContext().get("result"));
    }
}
