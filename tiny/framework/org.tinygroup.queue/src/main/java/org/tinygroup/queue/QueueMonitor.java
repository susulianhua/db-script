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
package org.tinygroup.queue;

/**
 * 队列监控接口
 * Created by IntelliJ IDEA. User: luoguo
 */
public interface QueueMonitor {
    /**
     * 返回名字
     *
     * @return
     */
    String getName();

    /**
     * 返回队列大小
     *
     * @return
     */
    int getSize();

    /**
     * 返回队列长度
     *
     * @return
     */

    int getUsingSize();

    /**
     * 返回空闲长度
     *
     * @return
     */
    int getIdleSize();
}
