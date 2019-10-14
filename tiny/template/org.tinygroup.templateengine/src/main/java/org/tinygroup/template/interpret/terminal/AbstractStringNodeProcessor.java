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
import org.tinygroup.template.TemplateRender;
import org.tinygroup.template.impl.TemplateRenderDefault;
import org.tinygroup.template.interpret.TemplateFromContext;
import org.tinygroup.template.interpret.TerminalNodeProcessor;

import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串的抽象处理节点
 * @author yancheng11334
 *
 */
public abstract class AbstractStringNodeProcessor implements TerminalNodeProcessor<TerminalNode> {

    private static final String rule = "[$][{][^}]*[}]";
    private static final Pattern pattern = Pattern.compile(rule);

    public boolean processChildren() {
        return false;
    }

    public Object process(TerminalNode terminalNode, TemplateContext context,
                          OutputStream outputStream, TemplateFromContext templateFromContext)
            throws Exception {
        String text = templateFromContext.getObject(terminalNode);
        if (text == null) {
            text = getText(terminalNode);
            if (pattern.matcher(text).find()) {
                // 进行$渲染
                Matcher matcher = pattern.matcher(text);
                StringBuilder sb = new StringBuilder();
                int pos = 0;
                TemplateRender render = new TemplateRenderDefault();
                render.setTemplateEngine(templateFromContext.getTemplateEngine());
                while (matcher.find()) {
                    String group = matcher.group();
                    sb.append(text.substring(pos, matcher.start()));
                    sb.append(render.renderTemplateContent(group, context));
                    pos = matcher.end();
                }
                sb.append(text.substring(pos));
                //返回动态结果，不保留缓存
                return sb.toString();
            } else {
                // 直接保存缓存
                templateFromContext.putObject(terminalNode, text);
            }
        }
        return text;
    }

    /**
     * 提取字符串内容
     * @param terminalNode
     * @return
     */
    protected abstract String getText(TerminalNode terminalNode);

}
