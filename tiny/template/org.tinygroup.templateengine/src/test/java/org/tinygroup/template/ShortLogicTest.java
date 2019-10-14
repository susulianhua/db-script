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

public class ShortLogicTest extends TestCase {

    private TemplateRender templateRender;

    protected void setUp() {
        templateRender = new TemplateRenderDefault();
        templateRender.setTemplateEngine(new TemplateEngineDefault());
    }

    //测试短路或
    public void testShortOr() throws Exception {
        TemplateContext context = new TemplateContextDefault();
        //b为null
        assertEquals("true", templateRender.renderTemplateContent("#set(a=(b==null || b.length()==0))${a}", context));
        //b为""
        context.put("b", "");
        assertEquals("true", templateRender.renderTemplateContent("#set(a=(b==null || b.length()==0))${a}", context));
        //b为"123"
        context.put("b", "123");
        assertEquals("false", templateRender.renderTemplateContent("#set(a=(b==null || b.length()==0))${a}", context));
    }

    //测试短路与
    public void testShortAdd() throws Exception {
        TemplateContext context = new TemplateContextDefault();
        //b为null
        assertEquals("false", templateRender.renderTemplateContent("#set(a=(b!=null && b.length()>0))${a}", context));
        //b为""
        context.put("b", "");
        assertEquals("false", templateRender.renderTemplateContent("#set(a=(b!=null && b.length()>0))${a}", context));

        //b为"temp"
        context.put("b", "temp");
        assertEquals("true", templateRender.renderTemplateContent("#set(a=(b!=null && b.length()>0))${a}", context));
    }

}
