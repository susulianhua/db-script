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
import org.tinygroup.template.interpret.*;
import org.tinygroup.template.parser.grammer.TinyTemplateParser;

import java.io.OutputStream;

/**
 * Created by luog on 15/7/17.
 */
public class MacroDefineProcessor implements ContextProcessor<TinyTemplateParser.Macro_directiveContext> {

    public Class<TinyTemplateParser.Macro_directiveContext> getType() {
        return TinyTemplateParser.Macro_directiveContext.class;
    }


    public Object process(TemplateInterpreter interpreter, TemplateFromContext templateFromContext, TinyTemplateParser.Macro_directiveContext parseTree, TemplateContext pageContext, TemplateContext context, TemplateEngineDefault engine, OutputStream outputStream, String fileName) throws Exception {
        String name = parseTree.getChild(0).getText();
        name = name.substring(6, name.length() - 1).trim();
        MacroFromContext macroFromContext = new MacroFromContext(TemplateEngineDefault.interpreter, name, parseTree.block(), templateFromContext);
        if (parseTree.define_expression_list() != null) {
            for (TinyTemplateParser.Define_expressionContext exp : parseTree.define_expression_list().define_expression()) {
                if (exp.expression() == null) {
                    macroFromContext.addParameter(exp.IDENTIFIER().getSymbol().getText(), null);
                } else {
                    macroFromContext.addParameter(exp.IDENTIFIER().getSymbol().getText(), new EvaluateExpressionImpl(interpreter, engine, templateFromContext, exp.expression()));
                }
            }
        }
        templateFromContext.addMacro(macroFromContext);
        return null;
    }


}

