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
package org.tinygroup.springutil;

import org.springframework.core.ParameterNameDiscoverer;
import org.tinygroup.commons.tools.ArrayUtil;
import org.tinygroup.springutil.annotation.MethodParamName;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * 从方法参数注解MethodParamName获取方法参数名称
 *
 * @author renhui
 */
public class AnnotationParameterNameDiscoverer implements
        ParameterNameDiscoverer {


    public String[] getParameterNames(Method method) {
        Class[] paramTypes = method.getParameterTypes();
        String[] names = new String[paramTypes.length];
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        int annotationsFound = 0;
        for (int i = 0; i < names.length; i++) {
            Annotation[] annotations = parameterAnnotations[i];
            if (!ArrayUtil.isEmptyArray(annotations)) {
                for (Annotation annotation : annotations) {
                    if (MethodParamName.class.isInstance(annotation)) {
                        MethodParamName methodParamName = (MethodParamName) annotation;
                        names[i] = methodParamName.value();
                        annotationsFound++;
                    }
                }
            }
        }
        if (annotationsFound == 0) {//发现方法参数没有标记@MethodParamName注解,就从方法字节码中获取参数名称
            return MethodNameAccessTool.getMethodParameterName(method);
        }
        if (annotationsFound != names.length) {
            throw new RuntimeException(String.format("方法：%s,所有参数对象需要增加MethodParamName注解", method.getName()));
        }
        return names;
    }

    public String[] getParameterNames(Constructor ctor) {
        return null;
    }

}
