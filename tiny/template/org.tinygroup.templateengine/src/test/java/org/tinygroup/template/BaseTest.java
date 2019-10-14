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

public class BaseTest extends TestCase {

    private TemplateRender templateRender;

    protected void setUp() throws Exception {
        templateRender = new TemplateRenderDefault();
        templateRender.setTemplateEngine(new TemplateEngineDefault());
    }

    public void testBaseOperator() throws Exception {

        //测试Long的加减乘除取模
        assertEquals("52", templateRender.renderTemplateContent("#set(a=50L+2L)${a}", new TemplateContextDefault()));
        assertEquals("48", templateRender.renderTemplateContent("#set(a=50L-2L)${a}", new TemplateContextDefault()));
        assertEquals("100", templateRender.renderTemplateContent("#set(a=50L*2L)${a}", new TemplateContextDefault()));
        assertEquals("25", templateRender.renderTemplateContent("#set(a=50L/2L)${a}", new TemplateContextDefault()));
        assertEquals("0", templateRender.renderTemplateContent("#set(a=50L%2L)${a}", new TemplateContextDefault()));

    }

}
