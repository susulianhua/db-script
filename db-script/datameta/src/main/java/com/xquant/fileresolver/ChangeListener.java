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
package com.xquant.fileresolver;

/**
 * 扫描或者动态刷新时，文件发生变化后的行为处理的Lisenter
 *
 * @author chenjiao
 */
public interface ChangeListener {
    /**
     * 文件变化后触发的监听方法
     *
     * @param resolver 文件搜索对象
     */
    void change(FileResolver resolver);
}
