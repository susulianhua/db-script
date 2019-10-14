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
package org.tinygroup.cepcoregovernance.container;

public class ExecuteTimeInfo {
    private long minTime = 0;
    private long maxTime = 0;
    private long times = 0;
    private long totalTime = 0;
    private String serviceId;

    public ExecuteTimeInfo(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public long getMinTime() {
        return minTime;
    }

    public void setMinTime(long minTime) {
        this.minTime = minTime;
    }

    public long getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(long maxTime) {
        this.maxTime = maxTime;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public long getTimes() {
        return times;
    }

    public void setTimes(long times) {
        this.times = times;
    }

    public void addTime(long time) {
        times++;
        totalTime += time;
        checkMax(time);
        checkMin(time);
    }

    private void checkMin(long time) {
        if (minTime == 0) {
            setMinTime(time);
        } else if (time < minTime) {
            minTime = time;
        }

    }

    private void checkMax(long time) {
        if (maxTime == 0) {
            setMaxTime(time);
        } else if (time > maxTime) {
            maxTime = time;
        }
    }

}
