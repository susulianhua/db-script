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
package org.tinygroup.tinyioc;

import org.tinygroup.tinyioc.annotation.Named;
import org.tinygroup.tinyioc.annotation.Singleton;

/**
 * Created by luoguo on 13-12-27.
 */
@Singleton
@Named("abc")
public class Hello1Impl implements Hello {

    public static void main(String[] args) {
        BeanContainer container = BeanContainerFactory.getBeanContainer();
        container.registerClass(Hello1Impl.class);
        Hello hello = container.getBeanByType(Hello.class);
        hello.sayHello("abc");
        hello = container.getBeanByName("abc");
        hello.sayHello("def");
    }

    public void sayHello(String name) {
        System.out.println("Hello:" + name);
    }
}
