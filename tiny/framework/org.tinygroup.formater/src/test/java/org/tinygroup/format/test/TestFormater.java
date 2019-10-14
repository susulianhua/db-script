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
package org.tinygroup.format.test;

import junit.framework.TestCase;
import org.tinygroup.context.Context;
import org.tinygroup.context.util.ContextFactory;
import org.tinygroup.format.Formater;
import org.tinygroup.format.impl.ContextFormater;
import org.tinygroup.format.impl.FormaterImpl;

public class TestFormater extends TestCase {

    public void testString() {
        Formater formater = new FormaterImpl();
        formater.addFormatProvider("", new ContextFormater());
        Context context = ContextFactory.getContext();
        String string = "${a}";
        context.put("a", "hello");
        string = formater.format(context, string);
        System.out.println(string);
        assertEquals("hello", string);
    }

    public void testStringPost() {
        Formater formater = new FormaterImpl();
        formater.addFormatProvider("", new ContextFormater());
        Context context = ContextFactory.getContext();
        String string = "${a}a";
        context.put("a", "hello");
        string = formater.format(context, string);
        System.out.println(string);
        assertEquals("helloa", string);
    }

    public void testStringPre() {
        Formater formater = new FormaterImpl();
        formater.addFormatProvider("", new ContextFormater());
        Context context = ContextFactory.getContext();
        String string = "a${a}";
        context.put("a", "hello");
        string = formater.format(context, string);
        System.out.println(string);
        assertEquals("ahello", string);
    }

    public void testStringWithOther1() {
        Formater formater = new FormaterImpl();
        formater.addFormatProvider("", new ContextFormater());
        Context context = ContextFactory.getContext();
        String string = "\"\\/a${a}\"";
        context.put("a", "hello");
        string = formater.format(context, string);
        System.out.println(string);
        assertEquals("\"\\/ahello\"", string);
    }


    public void testStringWithOther2() {
        Formater formater = new FormaterImpl();
        formater.addFormatProvider("", new ContextFormater());
        Context context = ContextFactory.getContext();
        String string = "-+=<!--CDATA---->\"\\/a${a}\"";
        context.put("a", "hello");
        string = formater.format(context, string);
        System.out.println(string);
        assertEquals("-+=<!--CDATA---->\"\\/ahello\"", string);
    }

}
