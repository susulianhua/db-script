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

import org.junit.Assert;
import org.junit.Test;
import org.tinygroup.trans.xstream.base.XStreamSceneMappingManager;

public class SceneMappingTest {

    @Test
    public void testFileInit() {
        XStreamSceneMappingManager.initXStreamSceneMapping("src/test/resources/scene-name.xstreamconvert.xml");
        Assert.assertEquals("packageName-1", XStreamSceneMappingManager.getXStreamPackageName("10001"));
        Assert.assertEquals("packageName-2", XStreamSceneMappingManager.getXStreamPackageName("10002"));
        Assert.assertEquals("packageName-3", XStreamSceneMappingManager.getXStreamPackageName("10003"));
        Assert.assertEquals("packageName-4", XStreamSceneMappingManager.getXStreamPackageName("10004"));
        Assert.assertEquals("packageName-5", XStreamSceneMappingManager.getXStreamPackageName("10005"));
    }
}
