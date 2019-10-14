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
package org.tinygroup.xstream.test;

import com.thoughtworks.xstream.XStream;
import junit.framework.TestCase;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;
import org.tinygroup.xstream.XStreamFactory;
import org.tinygroup.xstream.config.XStreamConfiguration;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TestCompanyCase extends TestCase {

    public void testToXml() throws Exception {
        XStream companyXstream = init("/company.xstream.xml");
        Company c = getCompany();
        String xml = companyXstream.toXML(c);
        System.out.println(xml);
        XmlNode companyNode = new XmlStringParser().parse(xml).getRoot();
        assertEquals("tiny", companyNode.getAttribute("company-name"));
        assertEquals("contory", companyNode.getSubNode("contory").getContent());
        assertEquals("11", companyNode.getSubNode("num").getContent());
        assertEquals("1.1", companyNode.getSubNode("money").getContent());
        assertEquals(null, companyNode.getSubNodes("departments"));
        assertEquals(null, companyNode.getSubNodes("roles"));
        assertEquals(2, companyNode.getSubNodes("department").size());
        assertEquals(2, companyNode.getSubNodes("role").size());
    }

    public void testToXml2() throws Exception {
        XStream companyXstream = init("/company2.xstream.xml");
        Company c = getCompany();
        String xml = companyXstream.toXML(c);
        System.out.println(xml);
        XmlNode companyNode = new XmlStringParser().parse(xml).getRoot();
        assertEquals("tiny", companyNode.getAttribute("company-name2"));
        assertEquals("contory", companyNode.getSubNode("contory").getContent());
        assertEquals("11", companyNode.getSubNode("num").getContent());
        assertEquals("1.1", companyNode.getSubNode("money").getContent());
        assertEquals(null, companyNode.getSubNodes("dontoutput"));
        assertEquals(1, companyNode.getSubNodes("departments").size());
        assertEquals(1, companyNode.getSubNodes("roles").size());
        assertEquals(2, companyNode.getSubNodesRecursively("department").size());
        assertEquals(2, companyNode.getSubNodesRecursively("role").size());
    }

    public void testToXml2Times() throws Exception {
        XStream companyXstream = init("/company2.xstream.xml");
        Company c = getCompany();
        int times = 1000;
        long time = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            companyXstream.toXML(c);
        }
        long totalTime = System.currentTimeMillis() - time;
        System.out.println("totalTime:" + totalTime);

    }

    public XStream init(String path) throws Exception {
        XStream stream = new XStream();
        stream.autodetectAnnotations(true);
        stream.alias("xstream-configuration", XStreamConfiguration.class);
        InputStream inputStream = TestCompanyCase.class
                .getResourceAsStream(path);
        XStreamConfiguration xStreamConfiguration = (XStreamConfiguration) stream
                .fromXML(inputStream);
        XStreamFactory.parse(xStreamConfiguration);
        return XStreamFactory.getXStream(xStreamConfiguration.getPackageName());
    }

    public Company getCompany() {
        Company c = new Company();
        c.setContory("contory");
        c.setMoney(1.1);
        c.setName("tiny");
        c.setNum(11);
        c.setDontoutput("a");

        Department d1 = new Department();
        d1.setName("p1");
        d1.setPeoples(1);
        Department d2 = new Department();
        d2.setName("p2");
        d2.setPeoples(2);

        List<Department> list = new ArrayList<Department>();
        list.add(d1);
        list.add(d2);

        Role r1 = new Role("r1", 1);
        Role r2 = new Role("r2", 2);
        Role[] roles = new Role[]{r1, r2};
        c.setRoles(roles);
        c.setDepartments(list);

        return c;
    }
}
