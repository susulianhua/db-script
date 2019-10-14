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
import org.tinygroup.template.interpret.CompareConditionException;
import org.tinygroup.template.interpret.ContextProcessor;
import org.tinygroup.template.interpret.TemplateFromContext;
import org.tinygroup.template.interpret.TemplateInterpreter;
import org.tinygroup.template.parser.grammer.TinyTemplateParser;
import org.tinygroup.template.rumtime.OperationUtil;
import org.tinygroup.template.rumtime.TemplateUtil;

import java.io.OutputStream;

/**
 * Created by luog on 15/7/17.
 */
public class MathCompareConditionProcessor implements ContextProcessor<TinyTemplateParser.Expr_compare_conditionContext> {


    public Class<TinyTemplateParser.Expr_compare_conditionContext> getType() {
        return TinyTemplateParser.Expr_compare_conditionContext.class;
    }

    public Object process(TemplateInterpreter interpreter, TemplateFromContext templateFromContext, TinyTemplateParser.Expr_compare_conditionContext parseTree, TemplateContext pageContext, TemplateContext context, TemplateEngineDefault engine, OutputStream outputStream, String fileName) throws Exception {

        try {
            boolean a = TemplateUtil.getBooleanValue(interpreter.interpretTree(engine, templateFromContext, parseTree.expression().get(0), pageContext, context, outputStream, fileName));
            String op = parseTree.getChild(1).getText();

            if (checkShortAnd(a, op)) {
                //执行短路与中断
                throw new CompareConditionException((Boolean) a);
            } else if (checkShortOr(a, op)) {
                //执行短路或中断
                throw new CompareConditionException((Boolean) a);
            } else {
                //执行正常逻辑运算
                boolean b = TemplateUtil.getBooleanValue(interpreter.interpretTree(engine, templateFromContext, parseTree.expression().get(1), pageContext, context, outputStream, fileName));
                return OperationUtil.executeOperation(op, a, b);
            }

        } catch (CompareConditionException e) {
            if (getType() != parseTree.getParent().getClass()) {
                //最顶层的CompareConditionException
                return e.getTag();
            } else {
                //继续往上抛
                throw e;
            }
        }
    }

    //检查短路与(首项为假,之后逻辑无需判断)
    private boolean checkShortAnd(Object obj, String op) {
        if (obj != null && obj instanceof Boolean && "&&".equals(op)) {
            return !((Boolean) obj).booleanValue();
        }
        return false;
    }

    //检查短路或(首项为真,之后逻辑无需判断)
    private boolean checkShortOr(Object obj, String op) {
        if (obj != null && obj instanceof Boolean && "||".equals(op)) {
            return ((Boolean) obj).booleanValue();
        }
        return false;
    }
}

