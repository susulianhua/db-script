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
import org.tinygroup.template.parser.grammer.TinyTemplateParser;

/**
 * Created by luog on 15/7/17.
 */
public class StringDoubleNodeProcessor extends AbstractStringNodeProcessor {

    public int getType() {
        return TinyTemplateParser.STRING_DOUBLE;
    }

    protected String getText(TerminalNode terminalNode) {
        String text = terminalNode.getText();
        text = text.replaceAll("\\\\\"", "\"");
        text = text.replaceAll("[\\\\][\\\\]", "\\\\");
        text = text.substring(1, text.length() - 1);
        return text;
    }

}
