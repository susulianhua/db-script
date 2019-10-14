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
package org.tinygroup.template.impl;

import org.tinygroup.template.Macro;
import org.tinygroup.template.Template;
import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.TemplateException;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 抽象宏
 * Created by luoguo on 2014/6/6.
 */
public abstract class AbstractBlockMacro extends AbstractMacro {
    public AbstractBlockMacro(String name) {
        super(name);
    }


    protected void renderMacro(Template template, TemplateContext pageContext, TemplateContext context, OutputStream outputStream) throws IOException, TemplateException {
        renderHeader(template, context, outputStream);
        Macro macro = (Macro) context.getItemMap().get("bodyContent");
        if (macro != null) {
            macro.render(template, pageContext, context, outputStream);
        }
        renderFooter(template, context, outputStream);
    }

    protected abstract void renderHeader(Template template, TemplateContext context, OutputStream outputStream) throws IOException, TemplateException;

    protected abstract void renderFooter(Template template, TemplateContext context, OutputStream outputStream) throws IOException, TemplateException;

}
