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

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.tinygroup.trans.xstream.XStreamTransManager;
import org.tinygroup.trans.xstream.base.XStreamSceneMappingManager;
import org.tinygroup.trans.xstream.util.XStreamConvertUtil;

import java.util.ArrayList;
import java.util.List;

public class TestXStream extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        XStreamSceneMappingManager
                .initXStreamSceneMapping("src/test/resources/scene-name.xstreamconvert.xml");
        List<String> filePaths = new ArrayList<String>();
        filePaths.add("src/test/resources/user-1.xstream.xml");
        filePaths.add("src/test/resources/user-2.xstream.xml");
        XStreamTransManager.initXStream(filePaths);
    }

    @Test
    public void test2Xml() {
        String str = "<user><aaaa>chendashen</aaaa><bbbb>18</bbbb><cccc>true</cccc><dddd>88.0</dddd><eeee>178</eeee></user>";
        User u = (User) XStreamConvertUtil.convert(str, "10001");
        Assert.assertEquals("chendashen", u.getName());

        User user = new User();
        user.setAge(18);
        user.setHeight(178);
        user.setMale(true);
        user.setName("chendashen");
        user.setWeight(88);
        String xml = (String) XStreamConvertUtil.convert(user, "10001");
        User user2 = (User) XStreamConvertUtil.convert(xml, "10001");
        Assert.assertEquals("chendashen", user2.getName());
        Assert.assertEquals(18, user2.getAge());
        Assert.assertEquals(true, user2.isMale());
        Assert.assertEquals(178, user2.getHeight());
        Assert.assertEquals(88, user2.getWeight(), 2);
    }
}
