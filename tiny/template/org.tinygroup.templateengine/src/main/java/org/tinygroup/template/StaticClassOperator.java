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

/**
 * 静态类执行器
 *
 * @author yancheng11334
 */
public interface StaticClassOperator {

    /**
     * 获得静态类别名
     *
     * @return
     */
    String getName();

    /**
     * 获得静态类
     *
     * @return
     */
    Class<?> getStaticClass();

    /**
     * 执行静态方法
     *
     * @param methodName
     * @param args
     * @return
     * @throws Exception
     */
    Object invokeStaticMethod(String methodName, Object[] args) throws Exception;
}
