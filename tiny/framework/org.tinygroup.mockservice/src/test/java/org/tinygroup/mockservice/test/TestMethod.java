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
package org.tinygroup.mockservice.test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestMethod {
    public static void main(String[] args) {
        try {
            Method m = TestMethod.class.getMethod("test", null);
            System.out.println(m.getReturnType());
            System.out.println(m.getGenericReturnType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Method m = TestMethod.class.getMethod("test2", null);
            System.out.println(m.getReturnType());
            System.out.println(m.getGenericReturnType());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> test() {
        List<String> list = new ArrayList<String>();
        list.add("a");
        list.add("b");
        list.add("c");
        return list;
    }

    public void test2() {
    }
}
