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
package org.tinygroup.template.listener;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.misc.Nullable;
import org.tinygroup.template.TemplateException;
import org.tinygroup.template.TemplateExceptionInfo;
import org.tinygroup.template.interpret.RecognizerExceptionInfo;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Created by luoguo on 15/8/11.
 */
public class TinyTemplateErrorListener implements org.antlr.v4.runtime.ANTLRErrorListener {
    private final String fileName;
    List<TemplateException> exceptions = new ArrayList<TemplateException>();

    public TinyTemplateErrorListener(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 获取全部TemplateException异常
     * @return
     */
    public List<TemplateException> getTemplateExceptions() {
        return this.exceptions;
    }

    /**
     * 获取第一个TemplateException异常
     * @return
     */
    public TemplateException getTemplateException() {
        if (exceptions != null && !exceptions.isEmpty()) {
            return exceptions.get(0);
        }
        return null;
    }

    public void syntaxError(@NotNull Recognizer<?, ?> recognizer, @Nullable Object offendingSymbol, int line, int charPositionInLine, @NotNull String msg, @Nullable RecognitionException e) {
        ParserRuleContext parserRuleContext = null;
        if (e != null) {
            RuleContext ruleContext = e.getCtx();
            if (ruleContext != null && ruleContext instanceof ParserRuleContext) {
                parserRuleContext = (ParserRuleContext) ruleContext;
            }
        }
        TemplateException exception = null;
        if (parserRuleContext != null) {
            //通过RecognitionException获取错误信息
            exception = new TemplateException(e, parserRuleContext, fileName);
        } else {
            //通过Recognizer获取错误信息
            exception = new TemplateException();
            TemplateExceptionInfo info = new RecognizerExceptionInfo(msg, fileName, line, charPositionInLine);
            exception.getExceptionInfos().add(info);
        }

        if (exception != null) {
            exceptions.add(exception);
        }


    }

    public void reportAmbiguity(@NotNull Parser parser, @NotNull DFA dfa, int i, int i1, boolean b, @Nullable BitSet bitSet, @NotNull ATNConfigSet atnConfigSet) {
        //exception=new TemplateException("",parser.getRuleContext(),fileName);
    }

    public void reportAttemptingFullContext(@NotNull Parser parser, @NotNull DFA dfa, int i, int i1, @Nullable BitSet bitSet, @NotNull ATNConfigSet atnConfigSet) {
//        exception=new TemplateException("",parser.getRuleContext(),fileName);
    }

    public void reportContextSensitivity(@NotNull Parser parser, @NotNull DFA dfa, int i, int i1, int i2, @NotNull ATNConfigSet atnConfigSet) {
//        exception=new TemplateException("",parser.getRuleContext(),fileName);
    }
}