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
import org.tinygroup.template.impl.TemplateContextDefault;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.template.interpret.ContextProcessor;
import org.tinygroup.template.interpret.TemplateFromContext;
import org.tinygroup.template.interpret.TemplateInterpreter;
import org.tinygroup.template.parser.grammer.TinyTemplateParser;

import java.io.OutputStream;
import java.net.URL;
import java.util.Map;

/**
 * Created by luog on 15/7/17.
 */
public class IncludeProcessor implements ContextProcessor<TinyTemplateParser.Include_directiveContext> {


    public Class<TinyTemplateParser.Include_directiveContext> getType() {
        return TinyTemplateParser.Include_directiveContext.class;
    }


    public Object process(TemplateInterpreter interpreter, TemplateFromContext templateFromContext, TinyTemplateParser.Include_directiveContext parseTree, TemplateContext pageContext, TemplateContext context, TemplateEngineDefault engine, OutputStream outputStream, String fileName) throws Exception {
        String path = interpreter.interpretTree(engine, templateFromContext, parseTree.expression(), pageContext, context, outputStream, fileName).toString();
        if (!path.startsWith("/")) {
            //如果不是绝对路径
            URL url = new URL("file:" + templateFromContext.getPath());
            URL newUrl = new URL(url, path);
            path = newUrl.getPath();
        }
        if (parseTree.hash_map_entry_list() != null) {
            Map map = (Map) interpreter.interpretTree(engine, templateFromContext, parseTree.hash_map_entry_list(), pageContext, context, outputStream, fileName);
            TemplateContext newContext = new TemplateContextDefault(map);
            engine.renderTemplateWithOutLayout(path, newContext, outputStream);
        } else {
            engine.renderTemplateWithOutLayout(path, context, outputStream);
        }
        //        context.put(key,value);
        return null;
    }
}