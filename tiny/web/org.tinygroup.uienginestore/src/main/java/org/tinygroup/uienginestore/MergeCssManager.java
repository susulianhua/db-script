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
package org.tinygroup.uienginestore;

import java.util.List;

/**
 * css资源管理器
 * @author yancheng11334
 *
 */
public interface MergeCssManager {

    /**
     * 获取合并的css资源
     * @return
     */
    List<String> getCssResource();

    /**
     * 设置css资源
     * @param cssList
     */
    void setCssResource(List<String> cssList);

    /**
     * 清理全部资源
     */
    void clear();

    /**
     * 添加单个css资源
     * @param css
     */
    void addCssResource(String css);

    /**
     * 删除单个css资源
     * @param css
     */
    void removeCssResource(String css);

}
