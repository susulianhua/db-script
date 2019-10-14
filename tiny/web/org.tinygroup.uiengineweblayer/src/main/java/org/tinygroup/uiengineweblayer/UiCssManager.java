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
package org.tinygroup.uiengineweblayer;

import java.io.IOException;
import java.util.List;

/**
 * css合并管理
 * @author yancheng11334
 *
 */
public interface UiCssManager {

    /** 获取css资源的访问路径
     * @return
     */
    List<String> getCssPaths();

    /**
     * 获取对应的css对象
     * @param path
     * @return
     */
    UiCssInfo getUiCssInfo(String path);

    /**
     * 获取对应的css对象
     * @param no
     * @return
     */
    UiCssInfo getUiCssInfo(int no);

    /**
     * 增加css对象
     * @param info
     */
    void addUiCssInfo(UiCssInfo info);

    /**
     * 删除css对象
     * @param path
     */
    void removeUiCssInfo(String path);

    /**
     * 删除css对象
     * @param no
     */
    void removeUiCssInfo(int no);

    void clear();

    String getParamterName();

    void setParamterName(String paramterName);

    long getCssLimit();

    void setCssLimit(long cssLimit);

    String getCssName();

    void setCssName(String cssName);

    /**
     * 动态创建css
     * @param contextPath
     * @param servletPath
     * @throws IOException
     */
    void createDynamicCss(String contextPath, String servletPath) throws IOException;

    /**
     * 动态创建css
     * @throws IOException
     */
    void createDynamicCss() throws IOException;
}
