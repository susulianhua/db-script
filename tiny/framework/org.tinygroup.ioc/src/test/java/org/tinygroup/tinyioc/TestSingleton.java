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

import org.tinygroup.tinyioc.impl.BeanContainerImpl;

/**
 * Created by luoguo on 13-12-27.
 */
public class TestSingleton {
    public static void main(String[] args) {
        BeanContainer container = new BeanContainerImpl();
        container.registerClass(Pet1.class);
        Pet pet = container.getBeanByName("pet1");
        Pet pet1 = container.getBeanByName("pet1");
        System.out.println(pet == pet1);
        System.out.println(pet.getClass().getName());
    }
}
