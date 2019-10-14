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

import org.tinygroup.template.Template;
import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.TemplateException;
import org.tinygroup.template.impl.TemplateContextDefault;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.template.loader.StringResourceLoader;

import java.util.HashMap;

/**
 * Created by luoguo on 15/7/22.
 */
public class EvalInterpret {
    public static void main(String[] args) throws TemplateException {
        TemplateContext context = new TemplateContextDefault();
        StringResourceLoader loader = new StringResourceLoader();
        TemplateEngineDefault engine = new TemplateEngineDefault();
        HashMap map;
        engine.addResourceLoader(loader);
        Template template = loader.createTemplate("#set(map={'a':1}) #for(var:map)${var.key}: ${var.value}#end");
        template.render(context, System.out);
    }

}

