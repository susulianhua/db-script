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
package org.tinygroup.urlrestful;

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.tinytestutil.AbstractTestUtil;

public class RuleTest extends TestCase {

    private static final String TEXT_HTML = "text/html";
    private static final String TEXT_JSON = "text/json";
    private static final String GET = "get";
    private static final String POST = "post";
    private static final String PUT = "put";
    private static final String DELETE = "delete";
    private UrlRestfulManager urlRestfulManager;

    protected void setUp() throws Exception {
        super.setUp();
        AbstractTestUtil.init(null, true);
        urlRestfulManager = BeanContainerFactory.getBeanContainer(
                this.getClass().getClassLoader()).getBean("urlRestfulManager");
    }

    public void testRestFul() {

        String path = "/users/12";
        Context context = urlRestfulManager.getContext(path, GET, TEXT_HTML);
        assertEquals(context.getMappingUrl(), "queryUsersTiny.servicepage");
        assertEquals("12", context.getVariableMap().get("id"));
        context = urlRestfulManager.getContext(path, POST, TEXT_HTML);
        assertEquals(context.getMappingUrl(), "addUserTiny.servicepage");
        context = urlRestfulManager.getContext(path, PUT, TEXT_HTML);
        assertEquals(context.getMappingUrl(), "updateUserTiny.servicepage");
        context = urlRestfulManager.getContext(path, DELETE, TEXT_HTML);
        assertEquals(context.getMappingUrl(), "deleteUserTiny.servicepage");
        //ACCEPT="text/json"
        context = urlRestfulManager.getContext(path, GET, TEXT_JSON);
        assertEquals(context.getMappingUrl(), "queryUsersTiny.servicejson");

        path = "/users/new/";
        context = urlRestfulManager.getContext(path, GET, TEXT_HTML);
        assertEquals(context.getMappingUrl(), "crud/restful/operate.page");

        path = "/users/edit/15";
        context = urlRestfulManager.getContext(path, GET, TEXT_HTML);
        assertEquals(context.getMappingUrl(), "queryUserByIdTiny.servicepage");
        assertEquals("15", context.getVariableMap().get("id"));


        path = "/users/edit/https://www.baidu.com/";
        context = urlRestfulManager.getContext(path, GET, TEXT_HTML);
        assertEquals(context.getMappingUrl(), "queryUserByIdTiny.servicepage");
        assertEquals("https://www.baidu.com/", context.getVariableMap().get("id"));


        path = "/users/12/Tuser";
        context = urlRestfulManager.getContext(path, POST, TEXT_HTML);
        assertEquals(context.getMappingUrl(), "addUserTiny.servicepage");
        assertEquals("12", context.getVariableMap().get("id"));
        assertEquals("Tuser", context.getVariableMap().get("@beantype"));

        path = "/users/12/classes/一年级";
        context = urlRestfulManager.getContext(path, POST, TEXT_HTML);
        assertEquals(context.getMappingUrl(), "queryclasses.servicepage");
        assertEquals("12", context.getVariableMap().get("id"));
        assertEquals("一年级", context.getVariableMap().get("name"));

        //未找到匹配的
        path = "/students/45";
        context = urlRestfulManager.getContext(path, GET, TEXT_HTML);
        assertNull(context);
    }

}
