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

import org.tinygroup.template.impl.AbstractMacro;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.template.loader.StringResourceLoader;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Boilit
 * @see
 */
public final class TinyTemplateHelloWorldMacro {
    public static void main(String[] args) throws TemplateException {
        TemplateEngine engine = new TemplateEngineDefault();
        ResourceLoader<String> resourceLoader = new StringResourceLoader();
        engine.addResourceLoader(resourceLoader);
        engine.registerMacro(new HelloWorldMacro());
        Template template = resourceLoader.createTemplate("#helloWorld()");
        template.render();
    }

    static class HelloWorldMacro extends AbstractMacro {
        public HelloWorldMacro() {
            super("helloWorld");
        }

        protected void renderMacro(Template template, TemplateContext pageContext, TemplateContext context, OutputStream OutputStream) throws IOException, TemplateException {
            OutputStream.write("Hello, World.".getBytes());
        }
    }
}
