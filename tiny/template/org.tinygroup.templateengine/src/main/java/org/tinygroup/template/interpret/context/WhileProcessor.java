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
import org.tinygroup.template.TemplateException;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.template.interpret.ContextProcessor;
import org.tinygroup.template.interpret.TemplateFromContext;
import org.tinygroup.template.interpret.TemplateInterpreter;
import org.tinygroup.template.interpret.terminal.ForBreakException;
import org.tinygroup.template.interpret.terminal.ForContinueException;
import org.tinygroup.template.parser.grammer.TinyTemplateParser;
import org.tinygroup.template.rumtime.TemplateUtil;

import java.io.OutputStream;

/**
 * Created by luog on 15/7/17.
 */
public class WhileProcessor implements ContextProcessor<TinyTemplateParser.While_directiveContext> {

    private static final int MAX_LOOPS = 10000;

    public Class<TinyTemplateParser.While_directiveContext> getType() {
        return TinyTemplateParser.While_directiveContext.class;
    }


    public Object process(TemplateInterpreter interpreter, TemplateFromContext templateFromContext, TinyTemplateParser.While_directiveContext parseTree, TemplateContext pageContext, TemplateContext context, TemplateEngineDefault engine, OutputStream outputStream, String fileName) throws Exception {
        boolean hasItem = false;
        int count = 0;
        while (TemplateUtil.getBooleanValue(interpreter.interpretTree(engine, templateFromContext, parseTree.expression(), pageContext, context, outputStream, fileName))) {
            count++;
            hasItem = true;
            if (count > MAX_LOOPS) {
                throw new TemplateException("循环次数太多!", parseTree, fileName);
            }
            try {
                interpreter.interpretTree(engine, templateFromContext, parseTree.block(), pageContext, context, outputStream, fileName);
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
