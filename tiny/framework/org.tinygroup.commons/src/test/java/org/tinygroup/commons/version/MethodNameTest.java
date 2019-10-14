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
package org.tinygroup.commons.version;

import junit.framework.TestCase;
import org.tinygroup.commons.beanutil.BeanUtil;
import org.tinygroup.commons.tools.ReflectionUtils;

import java.util.List;
import java.util.Map;

public class MethodNameTest extends TestCase {

    public void testMethodName() {
        String[] parameterNames = BeanUtil
                .getMethodParameterName(ReflectionUtils.findMethod(
                        TestObject.class, "method1", String.class, int.class));
        assertEquals("name1", parameterNames[0]);
        assertEquals("name2", parameterNames[1]);

        parameterNames = BeanUtil.getMethodParameterName(ReflectionUtils
                .findMethod(TestObject.class, "method2", Class.class));
        assertEquals("name3", parameterNames[0]);

        parameterNames = BeanUtil.getMethodParameterName(ReflectionUtils
                .findMethod(TestObject.class, "method3", Class[].class));
        assertEquals("name4", parameterNames[0]);

        parameterNames = BeanUtil.getMethodParameterName(ReflectionUtils
                .findMethod(TestObject.class, "method4", List.class));
        assertEquals("name5", parameterNames[0]);

        parameterNames = BeanUtil.getMethodParameterName(ReflectionUtils
                .findMethod(TestObject.class, "method5", Map.class));
        assertEquals("name6", parameterNames[0]);

    }

}
