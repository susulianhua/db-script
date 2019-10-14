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
package org.tinygroup.eventexecutemonitor.pojo;

import java.io.Serializable;

/**
 * Created by zhangliang08072 on 2016/12/26.
 */
public class ServiceMonitorItem implements Serializable {
    private static final long serialVersionUID = 2922444868573681157L;

    private String serviceId;
    private Long calltimes = 0L;//实际调用
    private Long successTimes = 0L;
    private Long totalTime = 0L;
    private Long times0ms_10ms = 0L;
    private Long times10ms_100ms = 0L;
    private Long times100ms_1s = 0L;
    private Long times1s_10s = 0L;
    private Long times10s_infinity = 0L;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Long getCalltimes() {
        return calltimes;
    }

    public void setCalltimes(Long calltimes) {
        this.calltimes = calltimes;
    }

    public Long getSuccessTimes() {
        return successTimes;
    }

    public void setSuccessTimes(Long successTimes) {
        this.successTimes = successTimes;
    }

    public Long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Long totalTime) {
        this.totalTime = totalTime;
    }

    public Long getTimes0ms_10ms() {
        return times0ms_10ms;
    }

    public void setTimes0ms_10ms(Long times0ms_10ms) {
        this.times0ms_10ms = times0ms_10ms;
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

}
