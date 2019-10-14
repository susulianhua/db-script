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
package org.tinygroup.runtimemonitor.service.pojo;

import java.io.Serializable;
import java.util.Map;

/**
 * 运行时间统计汇总信息基类
 * @author zhangliang08072
 * @version $Id: BaseRuntimeSummaryInfo.java, v 0.1 2016年12月28日 下午3:26:24 zhangliang08072 Exp $
 */
public class RuntimeSummaryInfo implements Serializable {
    private static final long serialVersionUID = -5135181108147033316L;
    private String businessId;//代表操作业务的唯一标识
    private String subType;//代表业务的子类型（譬如tiny服务分为local和remoate）
    private Long callTimes = 0L;//调用次数
    private Long successTimes = 0L;//执行成功次数
    private Long failureTimes = 0L;//失败次数
    private Long timeoutTimes = 0L;//超时次数
    private Long sumTimeInterval = 0L;//总运行时长
    private Long avgTimeInterval = 0L;//平均运行时长
    private Long maxTimeInterval = 0L;//最大运行时长
    private Long minTimeInterval = 0L;//最小运行时长

    private Long times0ms_1ms = 0L;
    private Long times1ms_10ms = 0L;
    private Long times10ms_100ms = 0L;
    private Long times100ms_1s = 0L;
    private Long times1s_10s = 0L;
    private Long times10s_infinity = 0L;

    private Map<String, String> extendsItemMap;//特色扩展字段

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public Long getCallTimes() {
        return callTimes;
    }

    public void setCallTimes(Long callTimes) {
        this.callTimes = callTimes;
    }

    public Long getSuccessTimes() {
        return successTimes;
    }

    public void setSuccessTimes(Long successTimes) {
        this.successTimes = successTimes;
    }

    public Long getTimeoutTimes() {
        return timeoutTimes;
    }

    public void setTimeoutTimes(Long timeoutTimes) {
        this.timeoutTimes = timeoutTimes;
    }

    public Long getSumTimeInterval() {
        return sumTimeInterval;
    }

    public void setSumTimeInterval(Long sumTimeInterval) {
        this.sumTimeInterval = sumTimeInterval;
    }

    public Long getAvgTimeInterval() {
        return avgTimeInterval;
    }

    public void setAvgTimeInterval(Long avgTimeInterval) {
        this.avgTimeInterval = avgTimeInterval;
    }

    public Long getMaxTimeInterval() {
        return maxTimeInterval;
    }

    public void setMaxTimeInterval(Long maxTimeInterval) {
        this.maxTimeInterval = maxTimeInterval;
    }

    public Long getMinTimeInterval() {
        return minTimeInterval;
    }

    public void setMinTimeInterval(Long minTimeInterval) {
        this.minTimeInterval = minTimeInterval;
    }

    public Long getFailureTimes() {
        return failureTimes;
    }

    public void setFailureTimes(Long failureTimes) {
        this.failureTimes = failureTimes;
    }

    public Long getTimes0ms_1ms() {
        return times0ms_1ms;
    }

    public void setTimes0ms_1ms(Long times0ms_1ms) {
        this.times0ms_1ms = times0ms_1ms;
    }

    public Long getTimes1ms_10ms() {
        return times1ms_10ms;
    }

    public void setTimes1ms_10ms(Long times1ms_10ms) {
        this.times1ms_10ms = times1ms_10ms;
    }

    public Long getTimes10ms_100ms() {
        return times10ms_100ms;
    }

    public void setTimes10ms_100ms(Long times10ms_100ms) {
        this.times10ms_100ms = times10ms_100ms;
    }

    public Long getTimes100ms_1s() {
        return times100ms_1s;
    }

    public void setTimes100ms_1s(Long times100ms_1s) {
        this.times100ms_1s = times100ms_1s;
    }

    public Long getTimes1s_10s() {
        return times1s_10s;
    }

    public void setTimes1s_10s(Long times1s_10s) {
        this.times1s_10s = times1s_10s;
    }

    public Long getTimes10s_infinity() {
        return times10s_infinity;
    }

    public void setTimes10s_infinity(Long times10s_infinity) {
        this.times10s_infinity = times10s_infinity;
    }

    public Map<String, String> getExtendsItemMap() {
        return extendsItemMap;
    }

    public void setExtendsItemMap(Map<String, String> extendsItemMap) {
        this.extendsItemMap = extendsItemMap;
    }

}
