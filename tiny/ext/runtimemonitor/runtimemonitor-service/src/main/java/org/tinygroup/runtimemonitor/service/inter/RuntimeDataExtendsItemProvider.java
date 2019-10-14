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
import org.tinygroup.runtimemonitor.service.pojo.RuntimeSummaryInfo;

import java.util.Map;

/**
 * 运行时间数据扩展项处理接口
 * @author zhangliang08072
 * @version $Id: RuntimeDataExtendsItemHandler.java, v 0.1 2017年1月2日 上午1:38:47 zhangliang08072 Exp $
 */
public interface RuntimeDataExtendsItemProvider {

    /**
     * 处理运行时间详细信息的扩展字段
     * @param detail
     */
    void handleRuntimeInfoDetail(RuntimeInfoDetail detail, Map<String, Object> itemMap);

    /**
     * 处理运行时间汇总统计信息的扩展字段
     * @param info
     */
    void handleRuntimeSummaryInfo(RuntimeSummaryInfo info, Map<String, Object> itemMap);
}
