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
package org.tinygroup.runtimemonitor.service.inter;

import org.tinygroup.runtimemonitor.service.pojo.RuntimeInfoDetail;

/**
 *
 * @author zhangliang08072
 * @version $Id: RuntimeInfoDetailProvider.java, v 0.1 2017年1月1日 下午3:40:25 zhangliang08072 Exp $
 */
public interface RuntimeInfoDetailMemoProvider {

    /**
     * 新增
     * @param detail
     */
    void add(String businessType, RuntimeInfoDetail detail);

    /**
     * 修改
     * @param detail
     */
    void update(String businessType, RuntimeInfoDetail detail);

    /**
     * 获取单条详细信息
     * @param businessType
     * @param runtimeId
     * @return
     */
    RuntimeInfoDetail getRuntimeInfoDetailById(String businessType, String runtimeId);
}
