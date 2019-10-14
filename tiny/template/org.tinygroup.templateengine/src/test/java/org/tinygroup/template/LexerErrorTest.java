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
import org.tinygroup.template.impl.AbstractMacro;
import org.tinygroup.template.impl.TemplateContextDefault;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.template.impl.TemplateRenderDefault;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 测试Lexer异常
 * @author yancheng11334
 *
 */
public class LexerErrorTest extends TestCase {


    public void testLexerError1() throws Exception {
        TemplateEngineDefault templateEngine = new TemplateEngineDefault();
        TemplateRender render = new TemplateRenderDefault();
        render.setTemplateEngine(templateEngine);

        TemplateContext context = new TemplateContextDefault();
        context.put("d", new java.util.Date());

        templateEngine.registerMacro(new HtmlMacro());
        templateEngine.setThrowLexerError(true);
        //抛出异常
        try {
            render.renderTemplateContent("#html(\"Hello,world\"，\"30\")", context);
            fail("No exception thrown.");
        } catch (TemplateException e) {
            assertEquals(true, e.getMessage().indexOf("token recognition error at: '，'") > 0);
        }

        //提示告警
        templateEngine.setThrowLexerError(false);
        render.renderTemplateContent("#html(\"Hello,world\"，\"30\")", context);
    }

    class HtmlMacro extends AbstractMacro {

        public HtmlMacro() {
            super("html");
            getParameterNames().add("title");
            getParameterNames().add("size");
        }

        protected void renderMacro(Template template,
                                   TemplateContext pageContext, TemplateContext context,
                                   OutputStream outputStream) throws IOException,
                TemplateException {
            outputStream.write("hello,world".getBytes());
        }

    }


}
