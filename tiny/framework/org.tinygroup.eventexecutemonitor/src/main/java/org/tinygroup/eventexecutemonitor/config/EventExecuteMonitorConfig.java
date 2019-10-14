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
package org.tinygroup.eventexecutemonitor.config;

import org.tinygroup.config.impl.AbstractConfiguration;
import org.tinygroup.eventexecutemonitor.config.inter.EventExecuteMonitorManager;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * Created by zhangliang08072 on 2016/12/21.
 */
class EventExecuteMonitorConfig extends AbstractConfiguration {
    private static final String EVENT_EXECUTE_MONITOR_CONFIG_PATH = "/application/event-monitor-config";
    EventExecuteMonitorManager eventExecuteMonitorManager;

    public EventExecuteMonitorManager getEventExecuteMonitorManager() {
        return eventExecuteMonitorManager;
    }

    public void setEventExecuteMonitorManager(EventExecuteMonitorManager eventExecuteMonitorManager) {
        this.eventExecuteMonitorManager = eventExecuteMonitorManager;
    }

    public String getApplicationNodePath() {
        return EVENT_EXECUTE_MONITOR_CONFIG_PATH;
    }

    public String getComponentConfigPath() {
        return null;
    }

    public void config(XmlNode applicationConfig, XmlNode componentConfig) {
        super.config(applicationConfig, componentConfig);
        eventExecuteMonitorManager.init(applicationConfig);
    }
}
