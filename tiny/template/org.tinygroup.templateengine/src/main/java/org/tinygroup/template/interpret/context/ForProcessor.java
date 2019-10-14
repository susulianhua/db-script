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
package org.tinygroup.template.interpret.context;

import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.template.interpret.ContextProcessor;
import org.tinygroup.template.interpret.TemplateFromContext;
import org.tinygroup.template.interpret.TemplateInterpreter;
import org.tinygroup.template.interpret.terminal.ForBreakException;
import org.tinygroup.template.interpret.terminal.ForContinueException;
import org.tinygroup.template.parser.grammer.TinyTemplateParser;
import org.tinygroup.template.rumtime.ForIterator;

import java.io.OutputStream;

/**
 * Created by luog on 15/7/17.
 */
public class ForProcessor implements ContextProcessor<TinyTemplateParser.For_directiveContext> {

    public Class<TinyTemplateParser.For_directiveContext> getType() {
        return TinyTemplateParser.For_directiveContext.class;
    }


    public Object process(TemplateInterpreter interpreter, TemplateFromContext templateFromContext, TinyTemplateParser.For_directiveContext parseTree, TemplateContext pageContext, TemplateContext context, TemplateEngineDefault engine, OutputStream outputStream, String fileName) throws Exception {
        String name = templateFromContext.getObject(parseTree);
        if (name == null) {
            name = parseTree.for_expression().IDENTIFIER().getText();
            templateFromContext.putObject(parseTree, name);
        }
        Object values = interpreter.interpretTree(engine, templateFromContext, parseTree.getChild(1).getChild(2), pageContext, context, outputStream, fileName);
        ForIterator forIterator = new ForIterator(values);
        context.put(name + "For", forIterator);
        boolean hasItem = false;
        while (forIterator.hasNext()) {
            hasItem = true;
            Object value = forIterator.next();
            context.put(name, value);
            try {
                interpreter.interpretTree(engine, templateFromContext, parseTree.getChild(3), pageContext, context, outputStream, fileName);
            } catch (ForBreakException be) {
                break;
            } catch (ForContinueException ce) {
                continue;
            }
        }
        if (!hasItem) {
            TinyTemplateParser.Else_directiveContext elseDirectiveContext = parseTree.else_directive();
            if (elseDirectiveContext != null) {
                interpreter.interpretTree(engine, templateFromContext, elseDirectiveContext.block(), pageContext, context, outputStream, fileName);
            }
        }
        return null;
    }
}
