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
import org.tinygroup.template.application.DefaultStaticClassOperator;
import org.tinygroup.template.impl.TemplateContextDefault;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.template.loader.StringResourceLoader;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 静态类的测试用例
 * @author yancheng11334
 *
 */
public class StaticClassTest extends TestCase {

    private TemplateEngine engine;
    private StringResourceLoader resourceLoader;
    private TemplateContext context;

    protected void setUp() throws Exception {
        engine = new TemplateEngineDefault();
        context = new TemplateContextDefault();
        resourceLoader = new StringResourceLoader();
        engine.addResourceLoader(resourceLoader);
        engine.registerStaticClassOperator(new DefaultStaticClassOperator("stringUtil", this.getClass().getClassLoader().loadClass("org.tinygroup.commons.tools.StringUtil")));
    }


    public void testCase1() throws Exception {

        //Template template =resourceLoader.createTemplate("#set(abc=stringUtil.isEmpty(\"ddd\"))${abc}");
        Map<Object, String> map = new HashMap<Object, String>();
        //Integer key = Integer.valueOf(1);
        String key = "name";
        map.put(key, "1888888888");
        context.put("map", map);
        context.put("key", key);
        Template template = resourceLoader.createTemplate("${map.get(key)}");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            engine.renderTemplate(template, context, outputStream);
            assertEquals("1888888888", getString(outputStream));
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }
//	
//   public void testCase2() throws Exception{
//		
//		Template template =resourceLoader.createTemplate("#set(abc=stringUtil.isEmpty(\"\"))${abc}");
//		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//		try{
//		   engine.renderTemplate(template, context, outputStream);
//		   assertEquals("true", getString(outputStream));
//		}finally{
//			if(outputStream!=null){
//				outputStream.close();
//			}
//		}
//	}

    public void testCase3() throws Exception {
        context.put("person", new Person());
        Template template = resourceLoader.createTemplate("#set(abc=person.get(\"yc\"))${abc}");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            engine.renderTemplate(template, context, outputStream);
            assertEquals("hello:yc", getString(outputStream));
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    public void testCase4() throws Exception {
        List<String> list = new ArrayList<String>();
        list.add("bbb");
        list.add("aaa");
        context.put("list", list);
        Template template = resourceLoader.createTemplate("#set(abc=list.get(1))${abc}");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            engine.renderTemplate(template, context, outputStream);
            assertEquals("aaa", getString(outputStream));
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

//    public void testCase5() throws Exception{
//    	Template template =resourceLoader.createTemplate("#[\n#for(i:[1..3])${i}#eol#end#]");
//		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//		try{
//		   engine.renderTemplate(template, context, outputStream);
//		   String s = getString(outputStream);
//		   String target = "\n\n1\n\n";
//		   assertEquals(target, s);
//		}finally{
//			if(outputStream!=null){
//				outputStream.close();
//			}
//		}
//	}

    private String getString(ByteArrayOutputStream outputStream) throws Exception {
        return new String(outputStream.toByteArray(), "utf-8");
    }
}
