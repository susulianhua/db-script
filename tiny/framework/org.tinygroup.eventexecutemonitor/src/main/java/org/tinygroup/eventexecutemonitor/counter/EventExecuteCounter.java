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
package org.tinygroup.eventexecutemonitor.counter;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.cepcoreimpl.ThreadPoolConfig;
import org.tinygroup.cepcoreimpl.ThreadPoolFactory;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.util.concurrent.ExecutorService;

/**
 * Created by zhangliang08072 on 2016/12/22.
 */
public class EventExecuteCounter {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventExecuteCounter.class);
    private static ExecutorService executor = null;


    protected synchronized void initThreadPool(String configBean) {
        if (executor != null) {
            return;
        }
        ThreadPoolConfig poolConfig = BeanContainerFactory.getBeanContainer(
                this.getClass().getClassLoader()).getBean(configBean);
        executor = ThreadPoolFactory.getThreadPoolExecutor(poolConfig);
    }

    protected ExecutorService getExecutorService() {
        if (executor == null) {
//            LOGGER.logMessage(LogLevel.WARN,
//                    "未配置异步服务线程池config bean,使用默认配置bean:{}",
//                    ThreadPoolConfig.DEFAULT_THREADPOOL);
            initThreadPool(ThreadPoolConfig.DEFAULT_THREADPOOL);

        }
        return executor;
    }
}
