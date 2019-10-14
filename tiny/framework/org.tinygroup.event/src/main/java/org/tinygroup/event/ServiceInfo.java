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
package org.tinygroup.event;

import java.io.Serializable;
import java.util.List;

public interface ServiceInfo extends Serializable, Comparable<ServiceInfo> {
    /**
     * @return 服务id
     */
    String getServiceId();


    /**
     * 若参数为空，需要返回一个空list
     *
     * @return 参数列表
     */
    List<Parameter> getParameters();

    /**
     * 若结果集为空，需要返回一个空list
     *
     * @return 结果集列表
     */
    List<Parameter> getResults();

    /**
     * 返回服务分类
     *
     * @return
     */
    String getCategory();

    /**
     * 返回服务超时时间
     * @return
     */
    long getRequestTimeout();
}
