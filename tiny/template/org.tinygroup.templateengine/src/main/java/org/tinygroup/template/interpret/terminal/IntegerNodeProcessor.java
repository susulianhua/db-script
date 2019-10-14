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
package org.tinygroup.template.interpret.terminal;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.interpret.TemplateFromContext;
import org.tinygroup.template.interpret.TerminalNodeProcessor;
import org.tinygroup.template.parser.grammer.TinyTemplateParser;

import java.io.OutputStream;

/**
 * Created by luog on 15/7/17.
 */
public class IntegerNodeProcessor implements TerminalNodeProcessor<TerminalNode> {


    public int getType() {
        return TinyTemplateParser.INTEGER;
    }

    public boolean processChildren() {
        return false;
    }

    public Object process(TerminalNode terminalNode, TemplateContext context, OutputStream outputStream, TemplateFromContext templateFromContext) {
        Object value = templateFromContext.getObject(terminalNode);
        if (value == null) {
            String text = terminalNode.getText().toLowerCase();
            if (text.endsWith("l")) {
                value = Long.parseLong(text.substring(0, text.length() - 1));
            } else if (text.endsWith("f")) {
                value = Float.parseFloat(text.substring(0, text.length() - 1));
            } else if (text.endsWith("d")) {
                value = Double.parseDouble(text.substring(0, text.length() - 1));
            } else {
                value = Integer.parseInt(text);
            }
            templateFromContext.putObject(terminalNode, value);
        }
        return value;
    }
}
