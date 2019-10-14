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
package org.tinygroup.validate;

import java.io.Serializable;
import java.util.List;

/**
 * 校验结果
 *
 * @author luoguo
 */
public interface ValidateResult extends Serializable {
    /**
     * 添加错误
     *
     * @param field
     * @param errorField
     * @param errorInfo
     */
    void addError(String name, String errorInfo);

    /**
     * 添加错误描述
     *
     * @param errorDescription
     */
    void addError(ErrorDescription errorDescription);

    /**
     * 返回所有的错误信息
     *
     * @return
     */
    List<ErrorDescription> getErrorList();

    /**
     * 返回是否有错误
     *
     * @return
     */
    boolean hasError();
}
