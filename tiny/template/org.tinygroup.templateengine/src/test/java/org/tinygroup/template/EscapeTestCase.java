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

/**
 * 测试html的转义与反转义
 * @author yancheng11334
 *
 */
public class EscapeTestCase extends TestCase {

    private TemplateRender templateRender;

    protected void setUp() throws Exception {
        templateRender = new TemplateRenderDefault();
        templateRender.setTemplateEngine(new TemplateEngineDefault());
    }

    public void testHtml() throws Exception {
        String content = "<html>hello&;</html>";
        String result = "&lt;html&gt;hello&amp;;&lt;/html&gt;";
        TemplateContext context = new TemplateContextDefault();
        context.put("content", content);
        context.put("result", result);
        assertEquals(result, templateRender.renderTemplateContent("${escapeHtml(content)}", context));
        assertEquals(content, templateRender.renderTemplateContent("${unescapeHtml(result)}", context));
    }

    public void testNull() throws Exception {
        TemplateContext context = new TemplateContextDefault();
        assertEquals("", templateRender.renderTemplateContent("${escapeHtml(null)}", context));
        assertEquals("", templateRender.renderTemplateContent("${unescapeHtml(null)}", context));
    }

    public void testEmpty() throws Exception {
        TemplateContext context = new TemplateContextDefault();
        context.put("abc", "");
        assertEquals("", templateRender.renderTemplateContent("${escapeHtml(abc)}", context));
        assertEquals("", templateRender.renderTemplateContent("${unescapeHtml(abc)}", context));
    }
}
