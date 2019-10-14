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

import org.tinygroup.runtimemonitor.service.pojo.RuntimeSummaryInfo;

/**
 *
 * @author zhangliang08072
 * @version $Id: RuntimeSummaryInfoProvider.java, v 0.1 2017年1月1日 下午3:41:01 zhangliang08072 Exp $
 */
public interface RuntimeSummaryInfoMemoProvider {

    /**
     * 新增
     * @param info
     */
    void add(RuntimeSummaryInfo info);

    /**
     * 修改
     * @param info
     */
    void update(RuntimeSummaryInfo info);

    /**
     * 获取单条统计汇总信息
     * @param businessId
     * @param subType
     * @return
     */
    RuntimeSummaryInfo getRuntimeSummaryInfoById(String businessId, String subType);
}
