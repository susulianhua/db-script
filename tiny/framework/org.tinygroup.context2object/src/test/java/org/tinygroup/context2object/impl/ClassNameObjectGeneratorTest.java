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
package org.tinygroup.context2object.impl;

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.context2object.Cat;
import org.tinygroup.context2object.User;
import org.tinygroup.context2object.test.testcase.BastTestCast;

public class ClassNameObjectGeneratorTest extends BastTestCast {


    /**
     * 直接赋值属性
     */
    public void testGetObject() {
        Context context = new ContextImpl();
        Cat cat = new Cat();
        cat.setNickName("tom");
        context.put("cat", cat);
        context.put("name", "luog");
        context.put("age", 33);
        User user = (User) generator.getObject(null, null, User.class.getName(), this.getClass().getClassLoader(), context);
        assertEquals(user.getName(), "luog");
        assertEquals(user.getAge(), 33);
        assertEquals(user.getCat().getNickName(), "tom");
    }

    public void testGetAnnotation() {
        Context context = new ContextImpl();
        context.put("name", "luog");
        NewUser user = (NewUser) newgenerator.getObject(null, null, NewUser.class.getName(), this.getClass().getClassLoader(), context);
        assertEquals(user.getName(), "luog");
    }

    public void testGetObjectWithSubObject() {
        Context context = new ContextImpl();
        context.put("nickName", "tom");
        context.put("name", "luog");
        context.put("age", 33);
        User user = (User) generator.getObject(null, null, User.class.getName(), this.getClass().getClassLoader(), context);
        assertEquals(user.getName(), "luog");
        assertEquals(user.getAge(), 33);
        assertEquals(user.getCat().getNickName(), "tom");
    }

    public void testGetObjectWithSubObjectAnnotation() {
        Context context = new ContextImpl();
        context.put("nickName", "tom");
        context.put("name", "luog");
        NewUser user = (NewUser) newgenerator.getObject(null, null, NewUser.class.getName(), this.getClass().getClassLoader(), context);
        assertEquals(user.getName(), "luog");
        assertEquals(user.getCat().getNickName(), "tom");
    }

    public void testGetObjectWithSubObjectArray() {
        Context context = new ContextImpl();
        String[] nickName = {"tom", "tom1"};
        context.put("nickName", nickName);
        context.put("name", "luog");
        context.put("age", 33);
        User user = (User) generator.getObject(null, null, User.class.getName(), this.getClass().getClassLoader(), context);
        assertEquals(user.getName(), "luog");
        assertEquals(user.getAge(), 33);
        assertEquals(user.getCats()[0].getNickName(), "tom");
        assertEquals(user.getCats()[1].getNickName(), "tom1");
    }

}
