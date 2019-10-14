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
package org.tiny.seg.test;

import junit.framework.TestCase;
import org.tinygroup.chinese.WordParserManager;
import org.tinygroup.chinese.parsermanager.WordParserManagerImpl;

public class WordParserManagerTest extends TestCase {
    WordParserManager ww = new WordParserManagerImpl();

    public void setUp() {
        try {
            super.setUp();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ww.addWordString("中华");
        ww.addWordString("人民");
        ww.addWordString("国");
        ww.addWordString("共和");
        ww.addWordString("共和国");
        ww.addWordString("中华人民");
        ww.addWordString("中华人民共和国");
        ww.addWordString("苹果6");
    }

    public void testManager() {

    }
}
