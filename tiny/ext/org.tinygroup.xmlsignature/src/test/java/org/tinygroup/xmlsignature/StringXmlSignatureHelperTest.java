/**
 * Copyright (c) 2012-2017, www.tinygroup.org (luo_guo@icloud.com).
 * <p>
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * <p>
 *       http://www.gnu.org/licenses/gpl.html
 * <p>
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.xmlsignature;

import junit.framework.TestCase;
import org.tinygroup.tinyrunner.Runner;
import org.tinygroup.xmlparser.XmlDocument;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;
import org.tinygroup.xmlsignature.impl.StringXmlSignatureHelper;

public class StringXmlSignatureHelperTest extends TestCase {

    protected void setUp() throws Exception {
        Runner.initDirect("application.xml", null);
    }

    public void testString() {
        StringXmlSignatureHelper helper = new StringXmlSignatureHelper();
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Access><UserId>GSYH001</UserId></Access>", helper.getTemplateXml(null));
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Access><UserId>SERVER007</UserId></Access>", helper.getTemplateXml("SERVER007"));

        //获得userId
        XmlDocument doc = new XmlStringParser().parse("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Access><UserId>GSYH001</UserId></Access>");
        XmlNode node = doc.getRoot();
        assertEquals("GSYH001", node.getSubNode("UserId").getContent());
    }
}
