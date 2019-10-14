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
package org.tinygroup.httpvisitor.request;

import org.tinygroup.httpvisitor.BodyElement;

/**
 * POST独有的HTTP链式构造器接口
 *
 * @param <T>
 * @author yancheng11334
 */
public interface PostRequestBuilderInterface<T> extends BodyRequestBuilderInterface<T> {

    /**
     * 多段提交
     *
     * @param elements
     * @return
     */
    T multipart(BodyElement... elements);

}
