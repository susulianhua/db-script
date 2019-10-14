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
package org.tinygroup.remoteconfig.web;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.remoteconfig.RemoteConfigManageClient;
import org.tinygroup.remoteconfig.zk.manager.impl.ConfigItemManagerImpl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class RemoteConfigManageStartupListener implements ServletContextListener {
    private static Logger logger = LoggerFactory
            .getLogger(RemoteConfigManageStartupListener.class);

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.logMessage(LogLevel.INFO, "配置中心管理器初始化开始...");

        RemoteConfigManageClient managerClient = new ConfigItemManagerImpl();
        managerClient.start();
        logger.logMessage(LogLevel.INFO, "配置中心管理器初始化结束...");
    }

}
