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
public class InsertNodeTest extends AbstractFlowComponent {

    public void testXmlAppendXmlNode() {
        Context context = ContextFactory.getContext();
        context.put("xml", getXml());
        context.put("nodePath", "/CFX/HEAD");
        XmlNode xmlNode = new XmlNode("age");
        xmlNode.setContent("1000");
        context.put("xmlNode", xmlNode.toString());
        Event e = Event.createEvent("insertNodeFlow", context);
        cepcore.process(e);
        XmlNode xn = e.getServiceRequest().getContext().get("xml");
        assertEquals("<age>1000</age>", XMLOperatorUtil.getSubNode("/CFX/HEAD/age", xn).toString());
    }

}
