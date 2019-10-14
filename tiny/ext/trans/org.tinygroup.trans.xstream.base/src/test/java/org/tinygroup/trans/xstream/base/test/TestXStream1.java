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
package org.tinygroup.trans.xstream.base.test;

import com.thoughtworks.xstream.XStream;
import junit.framework.TestCase;
import org.tinygroup.trans.xstream.XStreamTransManager;
import org.tinygroup.trans.xstream.util.XStreamConvertUtil;
import org.tinygroup.xstream.XStreamFactory;

import java.util.ArrayList;
import java.util.List;

public class TestXStream1 extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        List<String> filePaths = new ArrayList<String>();
        filePaths.add("src/test/resources/user-1.xstream.xml");
        filePaths.add("src/test/resources/user-2.xstream.xml");
        XStreamTransManager.initXStream(filePaths);
    }

    public void test2Xml() {
        User user = new User();
        user.setAge(18);
        user.setHeight(178);
        user.setMale(true);
        user.setName("chendashen");
        user.setWeight(88);
        XStream stream = XStreamFactory.getXStream("packageName-1");
        String xml = XStreamConvertUtil.object2Xml(stream, user);
        System.out.println(xml);
        User u = (User) XStreamConvertUtil.xml2Object(stream, xml);
        System.out.println(u.getName());
    }
}
