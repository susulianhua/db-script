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

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.tinygroup.template.TemplateExceptionInfo;
import org.tinygroup.template.listener.Point;

/**
 * 抽象的模板异常信息块
 *
 * @author yancheng11334
 */
public abstract class AbstractTemplateExceptionInfo implements TemplateExceptionInfo {

    private transient ParserRuleContext parserRuleContext;
    private String reason;
    private String fileInfo;

    public AbstractTemplateExceptionInfo(ParserRuleContext parserRuleContext,
                                         String reason, String fileInfo) {
        super();
        this.parserRuleContext = parserRuleContext;
        this.reason = reason;
        this.fileInfo = fileInfo;
    }

    public String getFileInfo() {
        return fileInfo;
    }

    public String getReason() {
        return reason;
    }

    public Point getStart() {
        Token token = parserRuleContext.getStart();
        Point p = new Point(token.getLine(), token.getCharPositionInLine());
        return p;
    }

    public Point getEnd() {
        Token token = parserRuleContext.getStop();
        Point p = new Point(token.getLine(), token.getCharPositionInLine());
        return p;
    }

    public String getText() {
        return parserRuleContext.getText();
    }

    public ParserRuleContext getParserRuleContext() {
        return parserRuleContext;
    }

    public void updateParserRuleContext(ParserRuleContext context) {
        this.parserRuleContext = context;
    }

}
