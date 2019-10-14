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
package org.tinygroup.context;

import junit.framework.TestCase;
import org.tinygroup.context.impl.BaseContextImpl;

public class BaseContextTest extends TestCase {
    BaseContext context = new BaseContextImpl();

    protected void setUp() throws Exception {
        super.setUp();
        context.clear();
    }

    public void testPut() {
        context.put("aa", 3);
        int aa = (Integer) context.get("aa");
        assertEquals(3, aa);
    }

    public void testRemove() {
        context.put("aa", 3);
        int aa = (Integer) context.remove("aa");
        assertEquals(3, aa);
        if (context.exist("aa")) {
            fail("应该不存在");
        }
    }

    public void testGetString() {
        context.put("aa", 3);
        int aa = (Integer) context.get("aa");
        assertEquals(3, aa);
    }

    public void testGetStringT() {
        context.put("a1a", 3);
        int result = (Integer) context.get("aa", 4);
        assertEquals(4, result);
    }

    public void testExist() {
        context.put("aa", 3);
        assertEquals(true, context.exist("aa"));
        assertEquals(false, context.exist("a1a"));
    }

    public void testClear() {
        context.put("aaa", 3);
        context.clear();
        assertEquals(false, context.exist("aaa"));
    }

}
