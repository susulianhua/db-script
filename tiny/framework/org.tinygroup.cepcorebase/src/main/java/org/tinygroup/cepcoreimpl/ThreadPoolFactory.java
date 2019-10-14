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

import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPoolFactory {

    public static ThreadPoolExecutor getThreadPoolExecutor(
            ThreadPoolConfig config) {
        if (config.getThreadFactory() == null && config.getHandler() == null) {
            return new ThreadPoolExecutor(config.getCorePoolSize(),
                    config.getMaximumPoolSize(), config.getKeepAliveTime(),
                    config.getUnit(), config.getWorkQueue());

        } else if (config.getThreadFactory() == null) {
            return new ThreadPoolExecutor(config.getCorePoolSize(),
                    config.getMaximumPoolSize(), config.getKeepAliveTime(),
                    config.getUnit(), config.getWorkQueue(),
                    config.getHandler());
        } else if (config.getHandler() == null) {
            return new ThreadPoolExecutor(config.getCorePoolSize(),
                    config.getMaximumPoolSize(), config.getKeepAliveTime(),
                    config.getUnit(), config.getWorkQueue(),
                    config.getThreadFactory());
        } else {
            return new ThreadPoolExecutor(config.getCorePoolSize(),
                    config.getMaximumPoolSize(), config.getKeepAliveTime(),
                    config.getUnit(), config.getWorkQueue(),
                    config.getThreadFactory(), config.getHandler());
        }
    }
}
