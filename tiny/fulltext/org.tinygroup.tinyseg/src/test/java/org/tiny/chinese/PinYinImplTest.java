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
package org.tiny.chinese;

import junit.framework.TestCase;
import org.tiny.seg.impl.PinYinImpl;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.List;

public class PinYinImplTest extends TestCase {

    public void testA() {
    }

    public void testPinYin() {
        PinYinImpl p = new PinYinImpl();
        try {
            URL u = this.getClass().getClassLoader()
                    .getResource("word.pinyin");
            FileInputStream f = new FileInputStream(new File(u.toURI()));
            p.loadPinFile(f, "utf-8");
            p.getPinYin("呵");
            // 呵 7 阿 4
            assertEquals(p.getPinYin("呵".charAt(0)).size(), 7);
            assertEquals(p.getPinYin("阿".charAt(0)).size(), 4);
            assertEquals(p.getPinYin("呵").get(0), "a1");
            char[] chars = new char[]{"呵".charAt(0), "阿".charAt(0)};

            List<String>[] listArray = p.getPinYin(chars);
            assertEquals(listArray.length, 2);
            assertEquals(listArray[0].size(), 7);

            List<String> value = p.getPinYin("呵阿", "11");
            assertEquals("a1", value.get(0));
            assertEquals("a1", value.get(1));
        } catch (Exception e) {
            e.printStackTrace();
            assertFalse(true);
        }

    }
}
