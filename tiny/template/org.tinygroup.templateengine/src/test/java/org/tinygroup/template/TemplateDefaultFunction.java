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
package org.tinygroup.template;

import org.tinygroup.template.impl.TemplateContextDefault;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.template.loader.StringResourceLoader;

/**
 * 模板引擎内置函数测试用例
 *
 * @author yancheng11334
 */
public class TemplateDefaultFunction {

    public static void main(String[] args) throws TemplateException {
        final TemplateEngine engine = new TemplateEngineDefault();
        engine.setSafeVariable(true);
        TemplateContext context = new TemplateContextDefault();
        StringResourceLoader resourceLoader = new StringResourceLoader();
        engine.addResourceLoader(resourceLoader);

        Template template = null;

        template = resourceLoader.createTemplate("${rand()}");
        engine.renderTemplate(template, context, System.out);
        System.out.println();

        template = resourceLoader.createTemplate("${random('int')}");
        engine.renderTemplate(template, context, System.out);
        System.out.println();


        template = resourceLoader.createTemplate("${rand('long')}");
        engine.renderTemplate(template, context, System.out);
        System.out.println();

        template = resourceLoader.createTemplate("${rand('uuid')}");
        engine.renderTemplate(template, context, System.out);
        System.out.println();

        template = resourceLoader.createTemplate("${random('float')}");
        engine.renderTemplate(template, context, System.out);
        System.out.println();

        template = resourceLoader.createTemplate("${rand('double')}");
        engine.renderTemplate(template, context, System.out);
        System.out.println();

        template = resourceLoader.createTemplate("${toInt('87')}");
        engine.renderTemplate(template, context, System.out);
        System.out.println();

        template = resourceLoader.createTemplate("${toLong('1230000000')}");
        engine.renderTemplate(template, context, System.out);
        System.out.println();

        template = resourceLoader.createTemplate("${toBool('true')}");
        engine.renderTemplate(template, context, System.out);
        System.out.println();

        template = resourceLoader.createTemplate("${toFloat('-9.01')}");
        engine.renderTemplate(template, context, System.out);
        System.out.println();

        template = resourceLoader.createTemplate("${toDouble('1.234567890123')}");
        engine.renderTemplate(template, context, System.out);
        System.out.println();

        template = resourceLoader.createTemplate("${formatDate(now())}");
        engine.renderTemplate(template, context, System.out);
        System.out.println();

        template = resourceLoader.createTemplate("${formatDate(0L)}");
        engine.renderTemplate(template, context, System.out);
        System.out.println();

        template = resourceLoader.createTemplate("${formatDate(\"0\")}");
        engine.renderTemplate(template, context, System.out);
        System.out.println();

        template = resourceLoader.createTemplate("${formatDate(now(),'yyyy年MM月dd日 HH:mm:ss')}");
        engine.renderTemplate(template, context, System.out);
        System.out.println();

        template = resourceLoader.createTemplate("#set(numberArray=[1,2,3,4,5]) ${numberArray.get(6)}");
        engine.renderTemplate(template, context, System.out);
        System.out.println();

        template = resourceLoader.createTemplate("${instance(10,\"java.lang.Integer\")}");
        engine.renderTemplate(template, context, System.out);
        System.out.println();

        template = resourceLoader.createTemplate("#set(m0={'name':'LeiLei','id':'4201'},m1={'sex':'male','id':'4444'})#set(m2=m0.extend(m1,false))${m2}");
        engine.renderTemplate(template, context, System.out);
        System.out.println();

        template = resourceLoader.createTemplate("#set(m0={'name':'LeiLei','id':'4201'},m1={'sex':'male','id':'4444'})#set(m2=m0.extend(m1))${m2}");
        engine.renderTemplate(template, context, System.out);
        System.out.println();

        template = resourceLoader.createTemplate("${eval(a)}"); //查询null对象不抛出空指针异常
        engine.renderTemplate(template, context, System.out);
        System.out.println();
    }

}
