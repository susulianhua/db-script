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

import junit.framework.TestCase;
import org.tinygroup.template.impl.TemplateContextDefault;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.template.impl.TemplateRenderDefault;

/**
 * 测试String字符串
 * @author yancheng11334
 *
 */
public class StringTestCase extends TestCase {

    public void testString() throws Exception {

        TemplateEngineDefault templateEngine = new TemplateEngineDefault();
        TemplateRender render = new TemplateRenderDefault();
        render.setTemplateEngine(templateEngine);

        TemplateContext context = new TemplateContextDefault();
        context.put("a", "cat");
        assertEquals("dog and cat", render.renderTemplateContent("#set(s = \"dog and ${a}\")${s}", context));  //双引号
        assertEquals("dog and cat", render.renderTemplateContent("#set(s = 'dog and ${a}')${s}", context));  //单引号

        //测试变量不存在
        assertEquals("dog and ", render.renderTemplateContent("#set(s = \"dog and ${b}\")${s}", context));  //双引号
        assertEquals("dog and ", render.renderTemplateContent("#set(s = 'dog and ${b}')${s}", context));  //单引号

        context.put("i", 1); //测试数值
        assertEquals("images/story1.jpg", render.renderTemplateContent("#set(s = 'images/story${i}.jpg')${s}", context));
        //支持多段
        assertEquals("cat/story1.jpg", render.renderTemplateContent("#set(s = '${a}/story${i}.jpg')${s}", context));

        //支持for循环
        assertEquals("[0][1][2][3][4]", render.renderTemplateContent("#for(i:[1..5])#set(s='[${i-1}]')${s}#end", context));
        //支持表达式
        assertEquals("cat/4", render.renderTemplateContent("#set(s = '${a}/${1+2*4-5}')${s}", context));
        assertEquals("cat1cat2catmmtruem", render.renderTemplateContent("#set(s = '${a}1${a}2${a}mm${a.length()>0}m')${s}", context));
    }
}
