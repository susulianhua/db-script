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
package org.tinygroup.eventexecutemonitorbase;

import org.tinygroup.event.Event;

/**
 * 监控数据处理接口
 * Created by zhangliang08072 on 2016/12/28.
 */
public interface MonitorDataHandler {
    static final String LOCAL_DATA_TYPE = "local";
    static final String REMOTE_DATA_TYPE = "remote";

    /**
     * 在业务处理之前同步处理监控数据
     * @param e
     * @param type
     */
    void monitorDataBeforeSyn(Event e, String type);

    /**
     * 在业务处理之前异步处理监控数据
     * @param e
     * @param type
     */
    void monitorDataBeforeAsyn(Event e, String type);

    /**
     * 在业务处理之后同步处理监控数据
     * @param e
     * @param type
     */
    void monitorDataAfterSyn(Event e, String type);

    /**
     * 在业务处理之后异步处理监控数据
     * @param e
     * @param type
     */
    void monitorDataAfterAsyn(Event e, String type);
}
