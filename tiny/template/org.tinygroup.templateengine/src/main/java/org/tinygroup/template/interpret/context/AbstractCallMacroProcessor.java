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

import org.antlr.v4.runtime.ParserRuleContext;
import org.tinygroup.template.Macro;
import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.TemplateException;
import org.tinygroup.template.impl.TemplateContextDefault;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.template.interpret.ContextProcessor;
import org.tinygroup.template.interpret.MacroException;
import org.tinygroup.template.interpret.TemplateFromContext;
import org.tinygroup.template.parser.grammer.TinyTemplateParser;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by luoguo on 15/7/26.
 */
public abstract class AbstractCallMacroProcessor<T extends ParserRuleContext> implements ContextProcessor<T> {

    public void callMacro(TemplateEngineDefault engine, TemplateFromContext templateFromContext, String name, TinyTemplateParser.Para_expression_listContext paraList, TemplateContext pageContext, TemplateContext context, OutputStream outputStream) throws Exception {
        callBlockMacro(engine, templateFromContext, name, null, paraList, pageContext, outputStream, context);
    }

    public void callBlockMacro(TemplateEngineDefault engine, TemplateFromContext templateFromContext, String name, TinyTemplateParser.BlockContext block, TinyTemplateParser.Para_expression_listContext paraList, TemplateContext pageContext, OutputStream outputStream, TemplateContext context) throws Exception {
        Macro macro = engine.findMacro(name, templateFromContext, context);
        try {
            TemplateContext newContext = new TemplateContextDefault();
            newContext.setParent(context);
            if (paraList != null) {
                int i = 0;
                List<Object> parameterList = new ArrayList<Object>(); //宏内置的参数遍历列表
                for (TinyTemplateParser.Para_expressionContext para : paraList.para_expression()) {
                    if (para.getChildCount() == 3) {
                        //如果是带参数的
                        Object value = TemplateEngineDefault.interpreter.interpretTree(engine, templateFromContext, para.expression(), pageContext, context, outputStream, templateFromContext.getPath());
                        newContext.put(para.IDENTIFIER().getSymbol().getText(), value);
                        parameterList.add(value);
                    } else {
                        if (i >= macro.getParameterNames().size()) {
                            throw new TemplateException("参数数量超过宏<" + macro.getName() + ">允许接受的数量", paraList, templateFromContext.getPath());
                        }
                        Object value = TemplateEngineDefault.interpreter.interpretTree(engine, templateFromContext, para.expression(), pageContext, context, outputStream, templateFromContext.getPath());
                        newContext.put(macro.getParameterName(i), value);
                        parameterList.add(value);
                    }
                    i++;
                }
                newContext.put(name + "ParameterList", parameterList); //增加内置对象
            }

            Stack<TinyTemplateParser.BlockContext> stack = context.get("$bodyContent");
            if (stack == null) {
                stack = new Stack<TinyTemplateParser.BlockContext>();
                newContext.put("$bodyContent", stack);
            }
            stack.push(block);
            int stackSize = stack.size();
            macro.render(templateFromContext, pageContext, newContext, outputStream);
            if (stack.size() == stackSize) {
                //检查是否有#bodyContent,如果没有,主要主动弹出刚才放的空bodyContent
                stack.pop();
            }
        } catch (MacroException me) {
            throw new MacroException(macro, me);
        } catch (TemplateException te) {
            //te.setShowUpperMessage(false);
            throw new MacroException(macro, te);
        } catch (Exception e) {
            throw new MacroException(macro, e);
        }

    }

}
