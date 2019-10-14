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
import org.tinygroup.flowbasiccomponent.util.XMLOperatorUtil;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * @author qiucn
 */
public class MoveNodeTest extends AbstractFlowComponent {

    public void testMoveNode() {
        Context context = ContextFactory.getContext();
        context.put("xml", getXml());
        context.put("fromNodePath", "/CFX/HEAD/VER");
        context.put("toNodePath", "/CFX/HEAD/SRC");
        Event e = Event.createEvent("moveNodeFlow", context);
        cepcore.process(e);
        XmlNode xmlNode = e.getServiceRequest().getContext().get("xml");
        assertEquals("<HEAD><SRC>100000000000<VER>1.0</VER></SRC><DES>114200000000</DES><APP>TIPS</APP><MsgNo>2001</MsgNo><MsgID>20050830200100087356</MsgID><MsgRef>12345678901234567890</MsgRef><WorkDate>20050824</WorkDate></HEAD>", XMLOperatorUtil.getSubNode("/CFX/HEAD", xmlNode).toString());
    }

}
