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
package org.tinygroup.weblayer;

import java.util.Iterator;
import java.util.Map;


/**
 * tiny获取配置信息的接口
 *
 * @author renhui
 */
public interface BasicTinyConfig {

    /**
     * 获取名称
     *
     * @return
     */
    String getConfigName();

    /**
     * 根据参数名称获取参数值
     *
     * @param name
     * @return
     */
    String getInitParameter(String name);

    /**
     * 返回所有参数信息
     *
     * @return
     */
    Iterator<String> getInitParameterNames();


    Map<String, String> getParameterMap();


}
