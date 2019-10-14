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

import org.tinygroup.chinese.ParserException;
import org.tinygroup.chinese.WordParserManager;
import org.tinygroup.chinese.fileProcessor.ChineseContainer;

public class TestChinesePinYin extends BaseChineseTestCase {
    public void testGetWordSpell() {
        WordParserManager manager = ChineseContainer.getWordParserManager("");
        try {
            String[] s = manager.getWordSpell("重庆");
            assertEquals("qing4", s[1]);
            assertEquals("chong2", s[0]);
        } catch (ParserException e) {
            assertTrue(false);
        }
    }

    public void testGetWordSpellShort() {
        WordParserManager manager = ChineseContainer.getWordParserManager("");
        try {
            String s = manager.getWordSpellShort("重庆");
            assertEquals("cq", s);
        } catch (ParserException e) {
            assertTrue(false);
        }
    }

    public void testGetCharacterSpell() {
        WordParserManager manager = ChineseContainer.getWordParserManager("");
        Character ch = "庆".charAt(0);
        String s = manager.getCharacterSpell(ch);
        assertEquals("qing4", s);
    }

    public void testGetCharacterSpellWithIndex() {
        WordParserManager manager = ChineseContainer.getWordParserManager("");
        Character ch = "重".charAt(0);
        String s = manager.getCharacterSpell(ch, 1);
        assertEquals("chong2", s);
    }


}
