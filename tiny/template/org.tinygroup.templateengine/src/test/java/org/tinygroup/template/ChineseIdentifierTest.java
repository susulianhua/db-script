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
 * 中文标识符测试
 *
 * @author yancheng11334
 */
public class ChineseIdentifierTest extends TestCase {

    private TemplateRender templateRender;

    protected void setUp() {
        templateRender = new TemplateRenderDefault();
        templateRender.setTemplateEngine(new TemplateEngineDefault());
    }

    /**
     * 中文参数测试用例
     *
     * @throws TemplateException
     */
    public void testParameter() throws TemplateException {
        assertEquals("abc", templateRender.renderTemplateContent("#set(中文a='abc')${中文a}", new TemplateContextDefault()));
        assertEquals("3", templateRender.renderTemplateContent("#set(中文b=1+2)${中文b}", new TemplateContextDefault()));
    }
}
