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

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.lang.BooleanUtils;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.remoteconfig.utils.TinyConfigParamUtil;
import org.tinygroup.remoteconfig.zk.manager.impl.ZKConfigClientImpl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Enumeration;

public class RemoteConfigStartupListener implements ServletContextListener {
    private static final String GLOBAL_ENABLE = "tiny-remoteconfig";
    private static Logger logger = LoggerFactory
            .getLogger(RemoteConfigStartupListener.class);

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.logMessage(LogLevel.INFO, "web.xml配置中心配置项读取开始...");

        Boolean status = initContextParam(servletContextEvent);

        logger.logMessage(LogLevel.INFO, "web.xml配置中心配置项全局启用状态[{0}]", BooleanUtils.isTrue(status));

        TinyConfigParamUtil.setReadClient(new ZKConfigClientImpl());

        logger.logMessage(LogLevel.INFO, "web.xml配置中心配置项读取完成...");
    }

    @SuppressWarnings("unchecked")
    private Boolean initContextParam(ServletContextEvent event) {
        Enumeration<String> names = event.getServletContext().getInitParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String value = event.getServletContext().getInitParameter(name);
            if (value != null && StringUtils.equals(name, GLOBAL_ENABLE)) {
                Boolean status = BooleanUtils.toBooleanObject(value);
                TinyConfigParamUtil.setEnable(status);
                return status;
            }
        }
        return null;
    }

}
