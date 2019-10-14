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
package org.tinygroup.template;

import junit.framework.TestCase;
import org.tinygroup.template.impl.TemplateContextDefault;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.template.impl.TemplateRenderDefault;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.RandomAccess;

/**
 * 验证不同类修饰符的反射invoke
 * @author yancheng11334
 *
 */
public class MethodInvokeTest extends TestCase {

    private TemplateRender render;

    protected void setUp() throws Exception {
        render = new TemplateRenderDefault();
        render.setTemplateEngine(new TemplateEngineDefault());
    }

    public void testPrivateClass() throws Exception {
        TemplateContext context = new TemplateContextDefault();
        //context.put("emptyList", Collections.emptyList());
        //context.put("emptyList", new EmptyList1());
        //context.put("emptyList", new EmptyList2());
        context.put("emptyList", new EmptyList3());
        assertEquals("0", render.renderTemplateContent(
                "${emptyList.size()}", context));

    }

    private static class EmptyList1 extends AbstractList<Object> implements
            RandomAccess, Serializable {
        // use serialVersionUID from JDK 1.2.2 for interoperability
        private static final long serialVersionUID = 8842843931221139166L;

        public int size() {
            return 0;
        }

        public boolean contains(Object obj) {
            return false;
        }

        public Object get(int index) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }

    }

    protected static class EmptyList2 extends AbstractList<Object> implements
            RandomAccess, Serializable {
        // use serialVersionUID from JDK 1.2.2 for interoperability
        private static final long serialVersionUID = 8842843931221139166L;

        public int size() {
            return 0;
        }

        public boolean contains(Object obj) {
            return false;
        }

        public Object get(int index) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }

    }

    public static class EmptyList3 extends AbstractList<Object> implements
            RandomAccess, Serializable {
        // use serialVersionUID from JDK 1.2.2 for interoperability
        private static final long serialVersionUID = 8842843931221139166L;

        public int size() {
            return 0;
        }

        public boolean contains(Object obj) {
            return false;
        }

        public Object get(int index) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }

        public int size2() {
            return 0;
        }

    }
}
