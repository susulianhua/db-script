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

/**
 * Created by luoguo on 13-12-30.
 */
public class TestSubContainer {


    public static void main(String[] args) {
        BeanContainer container = BeanContainerFactory.getBeanContainer();
        container.registerClass(InterceptorImpl.class);
        container.registerClass(HelloImpl.class);
        container.registerClass(Hello1Impl.class);
        BeanContainer subContainer = BeanContainerFactory.getBeanContainer("subContainer");
        subContainer.registerClass(HelloHelperImpl.class);
        subContainer.setParent(container);
        container.addAop(new AopDefine(".*Hello1Impl", "sayHello", ".*", InterceptorImpl.class.getName()));
        subContainer.addAop(new AopDefine(".*", "set.*", ".*", InterceptorImpl.class.getName()));
        HelloHelper helloHelper = subContainer.getBeanByType(HelloHelper.class);
        helloHelper.sayHello("abc");
        System.out.println(helloHelper.getHelloList().size());
    }
}


