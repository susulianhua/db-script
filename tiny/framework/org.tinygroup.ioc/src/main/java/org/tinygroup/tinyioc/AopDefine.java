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

import java.util.regex.Pattern;

/**
 * AOP定义
 * Created by luoguo on 13-12-31.
 */
public class AopDefine {
    String interceptorTypeName;
    Pattern classPattern;
    Pattern methodPattern;
    Pattern exceptionPattern;

    public AopDefine() {

    }

    public AopDefine(String classPattern, String methodPattern, String exceptionPattern,
                     String interceptorTypeName) {
        setClassPattern(classPattern);
        setMethodPattern(methodPattern);
        setExceptionPattern(exceptionPattern);
        setInterceptorTypeName(interceptorTypeName);
    }

    public Pattern getClassPattern() {
        return classPattern;
    }

    public void setClassPattern(String classPattern) {
        this.classPattern = Pattern.compile(classPattern);
    }

    public Pattern getMethodPattern() {
        return methodPattern;
    }

    public void setMethodPattern(String methodPattern) {
        this.methodPattern = Pattern.compile(methodPattern);
    }

    public Pattern getExceptionPattern() {
        return exceptionPattern;
    }

    public void setExceptionPattern(String exceptionPattern) {
        this.exceptionPattern = Pattern.compile(exceptionPattern);
    }


    public String getInterceptorTypeName() {
        return interceptorTypeName;
    }

    public void setInterceptorTypeName(String interceptorTypeName) {
        this.interceptorTypeName = interceptorTypeName;
    }
}
