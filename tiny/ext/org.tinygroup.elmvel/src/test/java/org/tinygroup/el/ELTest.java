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
package org.tinygroup.el;

import junit.framework.TestCase;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.el.impl.MvelImpl;

public class ELTest extends TestCase {
    EL el = new MvelImpl();

    public void testExecute() {
        Context context = new ContextImpl();
        context.put("age", 12);
        boolean result = (Boolean) (el.execute("age==12", context));
        assertEquals(true, result);
        int newAge = (Integer) el.execute("age=age+1;return age;", context);
        assertEquals(13, newAge);
    }
}
