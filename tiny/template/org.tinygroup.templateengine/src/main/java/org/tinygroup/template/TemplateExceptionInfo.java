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

import org.tinygroup.template.listener.Point;

/**
 * 模板引擎异常信息块(一个异常会包含多个信息块)
 *
 * @author yancheng11334
 */
public interface TemplateExceptionInfo {

    /**
     * 报错文件信息
     *
     * @return
     */
    String getFileInfo();

    /**
     * 报错原因
     *
     * @return
     */
    String getReason();

    /**
     * 报错开始位置
     *
     * @return
     */
    Point getStart();

    /**
     * 报错结束位置
     *
     * @return
     */
    Point getEnd();

    /**
     * 报错文本
     *
     * @return
     */
    String getText();

    /**
     * 是否宏异常
     *
     * @return
     */
    boolean isMacroException();

    /**
     * 获得宏信息
     *
     * @return
     */
    Macro getMacroInfo();
}
