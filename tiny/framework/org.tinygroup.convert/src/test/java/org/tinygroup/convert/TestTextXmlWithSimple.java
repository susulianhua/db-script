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
package org.tinygroup.convert;

import junit.framework.TestCase;
import org.tinygroup.convert.textxml.simple.TextToXml;
import org.tinygroup.convert.textxml.simple.XmlToText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestTextXmlWithSimple extends TestCase {
    private static final String TEXT = "标识,姓名,地址,邮件;11,haha,address,e;12,hehe,address,email;13,xixi,address,email";
    private static final String XML = "<students><student><id>11</id><name>haha</name><address>address</address><email>e</email></student><student><id>12</id><name>hehe</name><address>address</address><email>email</email></student><student><id>13</id><name>xixi</name><address>address</address><email>email</email></student></students>";

    public void testText2Xml() throws ConvertException {
        Map<String, String> titleMap = new HashMap<String, String>();
        titleMap.put("标识", "id");
        titleMap.put("姓名", "name");
        titleMap.put("地址", "address");
        titleMap.put("邮件", "email");
        TextToXml textToXml = new TextToXml(titleMap, "students", "student",
                ";", ",");
        assertEquals(XML, textToXml.convert(TEXT));
    }

    public void testXml2Text() throws ConvertException {
        Map<String, String> titleMap = new HashMap<String, String>();
        List<String> fieldList = new ArrayList<String>();
        titleMap.put("id", "标识");
        titleMap.put("name", "姓名");
        titleMap.put("address", "地址");
        titleMap.put("email", "邮件");
        fieldList.add("id");
        fieldList.add("name");
        fieldList.add("address");
        fieldList.add("email");
        XmlToText xmlToText = new XmlToText(titleMap, fieldList, "students",
                "student", ";", ",");
        assertEquals(TEXT, xmlToText.convert(XML));
    }

}
