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
package org.tinygroup.template.interpret;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.TemplateException;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.template.interpret.terminal.OtherTerminalNodeProcessor;
import org.tinygroup.template.listener.TinyTemplateErrorListener;
import org.tinygroup.template.parser.grammer.TinyTemplateLexer;
import org.tinygroup.template.parser.grammer.TinyTemplateParser;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luog on 15/7/17.
 */
public class TemplateInterpreter {
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(TemplateInterpreter.class);
    TerminalNodeProcessor[] terminalNodeProcessors = new TerminalNodeProcessor[200];
    Map<Class<ParserRuleContext>, ContextProcessor> contextProcessorMap = new HashMap<Class<ParserRuleContext>, ContextProcessor>();
    OtherTerminalNodeProcessor otherNodeProcessor = new OtherTerminalNodeProcessor();

    public OtherTerminalNodeProcessor getOtherNodeProcessor() {
        return otherNodeProcessor;
    }

    public void setOtherNodeProcessor(OtherTerminalNodeProcessor otherNodeProcessor) {
        this.otherNodeProcessor = otherNodeProcessor;
    }

    public void addTerminalNodeProcessor(TerminalNodeProcessor processor) {
        terminalNodeProcessors[processor.getType()] = processor;
    }

    public void addContextProcessor(ContextProcessor contextProcessor) {
        contextProcessorMap.put(contextProcessor.getType(), contextProcessor);
    }

    public TinyTemplateParser.TemplateContext parserTemplateTree(String sourceName, String templateString, boolean throwLexerError) throws TemplateException {
        char[] source = templateString.toCharArray();
        ANTLRInputStream is = new ANTLRInputStream(source, source.length);
        // set source file name, it will be displayed in error report.
        is.name = sourceName;
        TinyTemplateErrorListener listener = new TinyTemplateErrorListener(sourceName);
        TinyTemplateLexer lexer = new TinyTemplateLexer(is);
        if (throwLexerError) {
            lexer.addErrorListener(listener);
        }
        TinyTemplateParser parser = new TinyTemplateParser(new CommonTokenStream(lexer));

        parser.addErrorListener(listener);
        TinyTemplateParser.TemplateContext context = parser.template();

        //重构异常TinyTemplateErrorListener
        TemplateException e = listener.getTemplateException();
        if (e != null) {
            throw e;
        }
        return context;
    }

    public void interpret(TemplateEngineDefault engine, TemplateFromContext templateFromContext, String templateString, String sourceName, TemplateContext pageContext, TemplateContext context, OutputStream outputStream, String fileName) throws Exception {
        interpret(engine, templateFromContext, parserTemplateTree(sourceName, templateString, engine.isThrowLexerError()), pageContext, context, outputStream, fileName);
    }

    public void interpret(TemplateEngineDefault engine, TemplateFromContext templateFromContext, TinyTemplateParser.TemplateContext templateParseTree, TemplateContext pageContext, TemplateContext context, OutputStream outputStream, String fileName) throws Exception {
        for (int i = 0; i < templateParseTree.getChildCount(); i++) {
            interpretTree(engine, templateFromContext, templateParseTree.getChild(i), pageContext, context, outputStream, fileName);
        }
    }

    public Object interpretTree(TemplateEngineDefault engine, TemplateFromContext templateFromContext, ParseTree tree, TemplateContext pageContext, TemplateContext context, OutputStream outputStream, String fileName) throws Exception {
        Object returnValue = null;
        if (tree instanceof TerminalNode) {
            TerminalNode terminalNode = (TerminalNode) tree;
            //LOGGER.logMessage(LogLevel.INFO, String.format("TerminalNode:%s type:%s", tree.getText(),terminalNode.getSymbol().getType()));
            TerminalNodeProcessor processor = terminalNodeProcessors[terminalNode.getSymbol().getType()];
            if (processor != null) {
                return processor.process(terminalNode, context, outputStream, templateFromContext);
            } else {
                return otherNodeProcessor.process(terminalNode, context, outputStream, templateFromContext);
            }
        } else if (tree instanceof ParserRuleContext) {
            try {
                //LOGGER.logMessage(LogLevel.INFO, String.format("ParserRuleContext:%s type:%s", tree.getText(),tree.getClass().getName()));
                ContextProcessor processor = contextProcessorMap.get(tree.getClass());
                if (processor != null) {
                    returnValue = processor.process(this, templateFromContext, (ParserRuleContext) tree, pageContext, context, engine, outputStream, fileName);
                }
                if (processor == null) {
                    for (int i = 0; i < tree.getChildCount(); i++) {
                        Object value = interpretTree(engine, templateFromContext, tree.getChild(i), pageContext, context, outputStream, fileName);
                        if (value != null) {
                            returnValue = value;
                        }
                    }
                }
            } catch (StopException se) {
                throw se;
            } catch (ReturnException se) {
                throw se;
            } catch (CompareConditionException ce) {
                throw ce;
            } catch (MacroException me) {
                ParserRuleContext parserRuleContext = (ParserRuleContext) tree;
                //if (checkMacroContext(me, tree)) {
                //me.setContext(parserRuleContext, null);
                // }
                me.updateExceptionInfo(parserRuleContext, fileName);
                throw me;
            } catch (TemplateException te) {
                //if (te.getContext() == null) {
                //    te.setContext((ParserRuleContext) tree, fileName);
                //}
                te.updateExceptionInfo((ParserRuleContext) tree, fileName);
                throw te;
            } catch (Exception e) {
                throw new TemplateException(e, (ParserRuleContext) tree, fileName);
            }
        } else {
            //这段应该是没有用的
            for (int i = 0; i < tree.getChildCount(); i++) {
                Object value = interpretTree(engine, templateFromContext, tree.getChild(i), pageContext, context, outputStream, fileName);
                if (returnValue == null && value != null) {
                    returnValue = value;
                }
            }
        }
        return returnValue;
    }

    /**
     * 是否更新MacroException上下文
     * @param me
     * @param tree
     * @return
     */
//    private boolean checkMacroContext(MacroException me, ParseTree tree) {
//        if (me.getContext() != null) {
//            return false;
//        }
//        return tree.getClass() == TinyTemplateParser.Call_macro_block_directiveContext.class || tree.getClass() == TinyTemplateParser.BlockContext.class;
//    }

}
