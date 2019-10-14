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

import org.tinygroup.cepcore.aop.CEPCoreAopManager;
import org.tinygroup.eventexecutemonitor.config.inter.EventExecuteMonitorManager;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * Created by zhangliang08072 on 2016/12/22.
 */
public class EventExecuteMonitorManagerImpl implements EventExecuteMonitorManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventExecuteMonitorManagerImpl.class);

    private CEPCoreAopManager cepCoreAopManager;

    public CEPCoreAopManager getCepCoreAopManager() {
        return cepCoreAopManager;
    }

    public void setCepCoreAopManager(CEPCoreAopManager cepCoreAopManager) {
        this.cepCoreAopManager = cepCoreAopManager;
    }

    public void init(XmlNode config) {
        if (config == null) {
            LOGGER.logMessage(LogLevel.INFO, "/application/event-monitor-config配置信息为空");
            return;
        }
        String enable = config.getAttribute("enable");
        if ("true".equalsIgnoreCase(enable)) {
            cepCoreAopManager.addAopAdapter(CEPCoreAopManager.BEFORE_LOCAL, MONITOR_LOCAL_BEFORE_BEAN, null);
            cepCoreAopManager.addAopAdapter(CEPCoreAopManager.AFTER_LOCAL, MONITOR_LOCAL_AFTER_BEAN, null);
            cepCoreAopManager.addAopAdapter(CEPCoreAopManager.BEFORE_REMOTE, MONITOR_REMOTE_BEFORE_BEAN, null);
            cepCoreAopManager.addAopAdapter(CEPCoreAopManager.AFTER_REMOTE, MONITOR_REMOTE_AFTER_BEAN, null);
        } else {
            LOGGER.logMessage(LogLevel.INFO, "/application/event-monitor-config的enable属性是false");
        }
    }
}
