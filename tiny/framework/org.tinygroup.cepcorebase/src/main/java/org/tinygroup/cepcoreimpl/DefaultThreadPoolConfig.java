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
package org.tinygroup.cepcoreimpl;

import java.util.concurrent.*;

public class DefaultThreadPoolConfig implements ThreadPoolConfig {
    private int corePoolSize = 10;
    private int maximumPoolSize = 50;
    private long keepAliveTime = 30000;
    private int workQueueLength = 100;
    private TimeUnit unit = TimeUnit.MILLISECONDS;

    public int getWorkQueueLength() {
        return workQueueLength;
    }

    public void setWorkQueueLength(int workQueueLength) {
        this.workQueueLength = workQueueLength;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public TimeUnit getUnit() {
        if (unit == null) {
            unit = TimeUnit.MILLISECONDS;
        }
        return unit;
    }

    public void setUnit(TimeUnit unit) {
        this.unit = unit;
    }

    public BlockingQueue<Runnable> getWorkQueue() {
        return new ArrayBlockingQueue<Runnable>(workQueueLength);
    }

    public ThreadFactory getThreadFactory() {
        return null;
    }

    public RejectedExecutionHandler getHandler() {
        return new ThreadPoolExecutor.AbortPolicy();
    }


}
