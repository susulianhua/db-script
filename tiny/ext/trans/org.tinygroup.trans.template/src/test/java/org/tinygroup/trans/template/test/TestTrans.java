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
package org.tinygroup.trans.template.test;

import junit.framework.TestCase;
import org.tinygroup.trans.template.manager.TemplateConvertManager;
import org.tinygroup.trans.template.util.TemplateConvertUtil;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;

public class TestTrans extends TestCase {
    @Override
    protected void setUp() throws Exception {
        TemplateConvertManager
                .put("src/test/resources/object-xml.templateconvert.xml");
        TemplateConvertUtil.setVarName("var");
    }

    public void testInitMapping() {
        User user = new User();
        user.setAge(18);
        user.setName("zhangsan");
        String xml = TemplateConvertUtil.convert(user, "100001");
        System.out.println(xml);
        XmlNode companyNode = new XmlStringParser().parse(xml).getRoot();
        assertEquals("zhangsan", companyNode.getSubNodesRecursively("name")
                .get(0).getContent());
        assertEquals(
                "18",
                String.valueOf(companyNode.getSubNodesRecursively("age").get(0)
                        .getContent()));
    }

    public void testNullScript() {
        User user = new User();
        user.setAge(18);
        user.setName("zhangsan");
        try {
            TemplateConvertUtil.convert(user, "100008");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
