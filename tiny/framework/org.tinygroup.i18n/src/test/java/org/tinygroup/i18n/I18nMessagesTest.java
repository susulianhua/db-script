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
package org.tinygroup.i18n;

import junit.framework.TestCase;
import org.tinygroup.commons.i18n.LocaleUtil;
import org.tinygroup.context.impl.ContextImpl;

import java.util.Properties;

public class I18nMessagesTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Properties properties = new Properties();
        properties.load(getClass().getResource("/i18n/info_zh_CN.properties").openStream());
        I18nMessageFactory.addCustomizeResource(LocaleUtil.getContext().getLocale(), properties);
    }

    public void testGetMessage() {
        I18nMessages i18 = I18nMessageFactory.getI18nMessages();
        //测试name
        assertEquals("罗果", i18.getMessage("name"));
        assertEquals("罗果", i18.getMessage("name", new ContextImpl()));
        assertEquals("罗果", i18.getMessage("name", new ContextImpl(), LocaleUtil.getContext().getLocale()));
        assertEquals("罗果", i18.getMessage("name", "default", new ContextImpl()));
        assertEquals("罗果", i18.getMessage("name", ""));
        assertEquals("罗果", i18.getMessage("name", LocaleUtil.getContext().getLocale()));
        assertEquals("罗果", i18.getMessage("name", "", new ContextImpl(), LocaleUtil.getContext().getLocale()));

        //测试defaultValue
        assertEquals(null, i18.getMessage("name1"));
        assertEquals("", i18.getMessage("name1", new ContextImpl()));
        assertEquals("", i18.getMessage("name1", new ContextImpl(), LocaleUtil.getContext().getLocale()));
        assertEquals("default", i18.getMessage("name1", "default", new ContextImpl()));
        assertEquals("default", i18.getMessage("name1", "default", new Object()));
        assertEquals("default", i18.getMessage("name1", LocaleUtil.getContext().getLocale(), "default", new Object()));
        assertEquals("default", i18.getMessage("name1", "default", new ContextImpl(), LocaleUtil.getContext().getLocale()));
    }

    public void testGetMessage1() {
        I18nMessages i18 = I18nMessageFactory.getI18nMessages();
        //占位符多余参数
//        try {
//            i18.getMessage("loginInfo1", LocaleUtil.getContext().getLocale(), "default", "z", "zz");
//            assertTrue(false);
//        } catch (Exception e) {
//            assertTrue(true);
//        }
        //占位符多余参数，无参
        try {
            i18.getMessage("loginInfo1", LocaleUtil.getContext().getLocale(), "default");
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
        //占位符多余参数，无参
//        try {
//            i18.getMessage("loginInfo1", LocaleUtil.getContext().getLocale(), "default", "z");
//            assertTrue(false);
//        } catch (Exception e) {
//            assertTrue(true);
//        }
        //正确写法
        try {
            i18.getMessage("loginInfo1", LocaleUtil.getContext().getLocale(), "default", "z", "zz", "zzz");
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
        //参数多余占位符
//        try {
//            i18.getMessage("loginInfo1", LocaleUtil.getContext().getLocale(), "default", "z", "zz", "zzz", "zzzz");
//            assertTrue(false);
//        } catch (Exception e) {
//            assertTrue(true);
//        }
        //----------------------------------------
        //占位符多余参数
        try {
            i18.getMessage("loginInfo", LocaleUtil.getContext().getLocale(), "default");
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
        //正常写法
        try {
            i18.getMessage("loginInfo", LocaleUtil.getContext().getLocale(), "default", "z");
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
        //参数多余占位符
//        try {
//            i18.getMessage("loginInfo", LocaleUtil.getContext().getLocale(), "default", "z", "zz");
//            assertTrue(false);
//        } catch (Exception e) {
//            assertTrue(true);
//        }
        //-----------------------------------
        //正确写法
        try {
            i18.getMessage("name", LocaleUtil.getContext().getLocale(), "default");
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
        //参数多余占位符
//        try {
//            i18.getMessage("name", LocaleUtil.getContext().getLocale(), "default", "z", "zz");
//            assertTrue(false);
//        } catch (Exception e) {
//            assertTrue(true);
//        }
    }

}
