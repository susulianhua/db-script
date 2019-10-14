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

import org.tinygroup.runtimemonitor.service.pojo.Pager;
import org.tinygroup.runtimemonitor.service.pojo.RuntimeInfoDetail;
import org.tinygroup.runtimemonitor.service.pojo.RuntimeSummaryInfo;

/**
 * 提供运行时间查询服务
 * @author zhangliang08072
 * @version $Id: RuntimeDataService.java, v 0.1 2017年1月2日 下午7:56:01 zhangliang08072 Exp $
 */
public interface RuntimeDataMonitorService {

    /**
     * 分页查询运行时间统计汇总信息
     * @param businessType 业务类型
     * @param businessId 业务ID
     * @param subType 子类型
     * @param start 起始索引
     * @param limit 每页记录数
     * @param sortField 排序字段
     * @param sortOrder 排序
     * @return
     */
    Pager<RuntimeSummaryInfo> queryRuntimeSummaryInfoByPage(String businessType, String businessId, String subType, int start, int limit, String sortField, String sortOrder);

    /**
     * 查询单条运行时间统计汇总信息
     * @param businessType
     * @param businessId
     * @param subType
     * @return
     */
    RuntimeSummaryInfo queryRuntimeSummaryById(String businessType, String businessId, String subType);

    /**
     * 分页查询运行时间详细信息
     * @param businessType 业务类型
     * @param runtimeId 运行ID
     * @param subType 子类型
     * @param minBiginTime 最小启动时间
     * @param maxBiginTime 最大启动时间
     * @param minInterval 最小运行时长
     * @param maxInterval 最大运行时长
     * @param start 起始索引
     * @param limit 每页记录数
     * @param sortField 排序字段
     * @param sortOrder 排序
     * @return
     */
    Pager<RuntimeInfoDetail> queryRuntimeInfoDetailsByPage(String businessType, String runtimeId, String subType, Long minBiginTime, Long maxBiginTime, Long minInterval, Long maxInterval, int start, int limit, String sortField, String sortOrder);

    /**
     * 查询单条运行时间统计汇总信息
     * @param businessType
     * @param runtimeId
     * @return
     */
    RuntimeInfoDetail queryRuntimeInfoDetailById(String businessType, String runtimeId);
}
