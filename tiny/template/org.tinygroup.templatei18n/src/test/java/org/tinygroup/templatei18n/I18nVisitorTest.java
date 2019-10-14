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
package org.tinygroup.templatei18n;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.tinygroup.commons.i18n.LocaleUtil;
import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.impl.TemplateContextDefault;
import org.tinygroup.tinytestutil.AbstractTestUtil;

import java.util.Locale;

public class I18nVisitorTest extends TestCase {

    protected void setUp() throws Exception {
        AbstractTestUtil.init(null, false);
    }

    /**
     * 测试上下文获取Locale
     */
    public void testContextLocaleI18nVisitor() {
        TemplateContext context = new TemplateContextDefault();
        ContextLocaleI18nVisitor i18nVisitor = new ContextLocaleI18nVisitor();
        i18nVisitor.setLocaleName("i18nlocale");
        String result;
        Assert.assertNull(i18nVisitor.getLocale(context));

        context.put("i18nlocale", Locale.US);
        result = i18nVisitor.getI18nMessage(context, "sex");
        Assert.assertEquals("male", result);

        context.put("name", "dog");
        result = i18nVisitor.getI18nMessage(context, "loginInfoContext");
        Assert.assertEquals("user dog login faild.", result);

        //测试不存在的键值
        result = i18nVisitor.getI18nMessage(context, "aaa");
        Assert.assertEquals("", result);
    }

    /**
     * 测试LocaleUtil取得默认的locale
     */
    public void testDefaultLocaleI18nVisitor() {
        TemplateContext context = new TemplateContextDefault();
        DefaultLocaleI18nVisitor i18nVisitor = new DefaultLocaleI18nVisitor();
        String result;

        Assert.assertNotNull(i18nVisitor.getLocale(context));

        LocaleUtil.setDefault(Locale.US);
        result = i18nVisitor.getI18nMessage(context, "sex");
        Assert.assertEquals("male", result);

        context.put("name", "dog");
        result = i18nVisitor.getI18nMessage(context, "loginInfoContext");
        Assert.assertEquals("user dog login faild.", result);

        //测试不存在的键值
        result = i18nVisitor.getI18nMessage(context, "aaa");
        Assert.assertEquals("", result);

    }

    /**
     * 测试LocaleUtil取得系统的locale(因为不同系统的locale不同，所以不能比对内容)
     */
    public void testSystemLocaleI18nVisitor() {
        TemplateContext context = new TemplateContextDefault();
        ThreadLocaleI18nVisitor i18nVisitor = new ThreadLocaleI18nVisitor();

        Assert.assertNotNull(i18nVisitor.getLocale(context));
    }

    /**
     * 测试LocaleUtil取得当前线程的locale
     */
    public void testThreadLocaleI18nVisitor() {

        LocaleUtil.setContext(Locale.CHINESE);

        TemplateContext context = new TemplateContextDefault();
        ThreadLocaleI18nVisitor i18nVisitor = new ThreadLocaleI18nVisitor();

        Assert.assertNotNull(i18nVisitor.getLocale(context));

        //测试不存在的键值
        String result = i18nVisitor.getI18nMessage(context, "aaa");
        Assert.assertEquals("", result);

    }
}
