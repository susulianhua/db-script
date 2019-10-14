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
package org.tinygroup.stopword;

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.tinyrunner.Runner;

import java.util.Set;

/**
 * 测试停止词
 *
 * @author yancheng11334
 */
public class StopWordTest extends TestCase {

    private StopWordManager stopWordManager;

    protected void setUp() throws Exception {
        super.setUp();
        Runner.init("application.xml", null);
        stopWordManager = BeanContainerFactory.getBeanContainer(this.getClass().getClassLoader()).getBean("stopWordManager");
    }


    public void testLoad() throws Exception {
        Set<String> stopWords = stopWordManager.getStopWords();

        assertTrue(stopWords.contains("的"));
        assertTrue(stopWords.contains("吗"));
        assertFalse(stopWords.contains("ok"));
    }

    public void testStopWord() {
        Set<String> stopWords = stopWordManager.getStopWords();
        assertFalse(stopWords.contains("法制"));
        stopWordManager.addStopWord("法制");
        stopWords = stopWordManager.getStopWords();
        assertTrue(stopWords.contains("法制"));

        stopWordManager.removeStopWord("法制");
        stopWords = stopWordManager.getStopWords();
        assertFalse(stopWords.contains("法制"));
    }
}
