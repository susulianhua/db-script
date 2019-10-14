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
import org.tinygroup.template.interpret.ContextProcessor;
import org.tinygroup.template.interpret.TemplateFromContext;
import org.tinygroup.template.interpret.TemplateInterpreter;
import org.tinygroup.template.parser.grammer.TinyTemplateParser;

import java.io.OutputStream;
import java.util.HashMap;

/**
 * Created by luog on 15/7/17.
 */
public class MapProcessor implements ContextProcessor<TinyTemplateParser.Expr_hash_mapContext> {


    public Class<TinyTemplateParser.Expr_hash_mapContext> getType() {
        return TinyTemplateParser.Expr_hash_mapContext.class;
    }


    public Object process(TemplateInterpreter interpreter, TemplateFromContext templateFromContext, TinyTemplateParser.Expr_hash_mapContext parseTree, TemplateContext pageContext, TemplateContext context, TemplateEngineDefault engine, OutputStream outputStream, String fileName) throws Exception {
        if (parseTree == null || parseTree.hash_map_entry_list() == null) {
            return new HashMap();
        }
        return interpreter.interpretTree(engine, templateFromContext, parseTree.hash_map_entry_list(), pageContext, context, outputStream, fileName);
    }

}

