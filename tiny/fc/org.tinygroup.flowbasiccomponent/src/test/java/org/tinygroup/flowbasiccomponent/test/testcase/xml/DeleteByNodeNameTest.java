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
package org.tinygroup.flowbasiccomponent.test.testcase.xml;

import org.tinygroup.context.Context;
import org.tinygroup.context.util.ContextFactory;
import org.tinygroup.event.Event;
import org.tinygroup.flowbasiccomponent.test.AbstractFlowComponent;

/**
 * @author qiucn
 */
public class DeleteByNodeNameTest extends AbstractFlowComponent {

    /**
     *
     */
    public void testXmlDeleteByNodeName() {
        Context context = ContextFactory.getContext();
        context.put("xml", getXml());
        context.put("nodePath", "/CFX/HEAD");
        context.put("nodeName", "VER");
        context.put("resultKey", "result");
        context.put("result", "");
        Event e = Event.createEvent("deleteByNodeNameFlow", context);
        cepcore.process(e);
        assertEquals("<VER>1.0</VER>", e.getServiceRequest().getContext().get("result"));
        assertEquals("<CFX><HEAD><SRC>100000000000</SRC><DES>114200000000</DES><APP>TIPS</APP><MsgNo>2001</MsgNo><MsgID>20050830200100087356</MsgID><MsgRef>12345678901234567890</MsgRef><WorkDate>20050824</WorkDate></HEAD><MSG><SingleReturn2001><OriTaxOrgCode>14200000000</OriTaxOrgCode><OriTraNo>00000016</OriTraNo><OriEntrustDate>20050827</OriEntrustDate><TaxVouNo>1</TaxVouNo><TaxDate>20050824</TaxDate><Result>90000</Result><AddWord>成功</AddWord></SingleReturn2001></MSG></CFX>", e.getServiceRequest().getContext().get("xml").toString());
    }

}
