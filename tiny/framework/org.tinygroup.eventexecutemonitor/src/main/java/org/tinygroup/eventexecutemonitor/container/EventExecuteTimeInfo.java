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
package org.tinygroup.eventexecutemonitor.container;

import java.io.Serializable;

/**
 * Created by zhangliang08072 on 2016/12/21.
 */
public class EventExecuteTimeInfo implements Serializable {
    private String serviceId;
    private long times = 0;
    private long totalTime = 0;
    private long times0ms_10ms = 0;
    private long times10ms_100ms = 0;
    private long times100ms_1s = 0;
    private long times1s_10s = 0;
    private long times10s_infinity = 0;

    public EventExecuteTimeInfo(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public long getTimes() {
        return times;
    }

    public void setTimes(long times) {
        this.times = times;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public long getTimes0ms_10ms() {
        return times0ms_10ms;
    }

    public void setTimes0ms_10ms(long times0ms_10ms) {
        this.times0ms_10ms = times0ms_10ms;
    }

    public long getTimes10ms_100ms() {
        return times10ms_100ms;
    }

    public void setTimes10ms_100ms(long times10ms_100ms) {
        this.times10ms_100ms = times10ms_100ms;
    }

    public long getTimes100ms_1s() {
        return times100ms_1s;
    }

    public void setTimes100ms_1s(long times100ms_1s) {
        this.times100ms_1s = times100ms_1s;
    }

    public long getTimes1s_10s() {
        return times1s_10s;
    }

    public void setTimes1s_10s(long times1s_10s) {
        this.times1s_10s = times1s_10s;
    }

    public long getTimes10s_infinity() {
        return times10s_infinity;
    }

    public void setTimes10s_infinity(long times10s_infinity) {
        this.times10s_infinity = times10s_infinity;
    }

    public void addTime(long time) {
        times++;
        totalTime += time;
        if (time <= 10) {
            times0ms_10ms++;
        } else if (10 < time && time <= 100) {
            times10ms_100ms++;
        } else if (100 < time && time <= 1000) {
            times100ms_1s++;
        } else if (1000 < time && time <= 10000) {
            times1s_10s++;
        } else if (time > 10000) {
            times10s_infinity++;
        }
    }
}
