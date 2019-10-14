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
package org.tinygroup.cepcoremonitor;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.event.Event;

public class MonitorUtil {
    /**
     * 此方法在计算出event所执行的processor后即可调用
     * 添加from/to信息，from当前节点的nodeName,to目标处理器的id
     * 如果是远程请求，目标处理器id为目标节点的ip:port:nodeName
     * 如果是本地请求，目标处理器就是本地处理器id
     * @param e 请求事件对象
     * @param core 当前节点cepcore
     * @param processor 目标处理器
     */
    public static void addMonitorInfo(Event e, CEPCore core, EventProcessor processor) {
        addFormInfo(e, core);
        addToInfo(e, processor);
    }

    public static void addFormInfo(Event e, CEPCore core) {
        e.getServiceRequest().getContext().put(MonitorConstants.FORM_NODE, core.getNodeName());
    }

    public static void addToInfo(Event e, EventProcessor processor) {
        e.getServiceRequest().getContext().put(MonitorConstants.TO_NODE, processor.getId());
    }
}
