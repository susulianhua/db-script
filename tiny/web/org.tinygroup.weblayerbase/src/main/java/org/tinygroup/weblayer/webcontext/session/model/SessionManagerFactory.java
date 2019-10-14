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
package org.tinygroup.weblayer.webcontext.session.model;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.weblayer.webcontext.session.SessionManager;

public class SessionManagerFactory {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(SessionManagerFactory.class);

    public static SessionManager getSessionManager(String beanId,
                                                   ClassLoader classLoader) {
        if (StringUtil.isBlank(beanId)) {
            return DefaultSessionManager.getSingleton();
        }
        SessionManager manager = null;
        try {
            manager = BeanContainerFactory.getBeanContainer(classLoader)
                    .getBean(beanId);
        } catch (Exception e) {
            LOGGER.logMessage(
                    LogLevel.WARN,
                    "在容器中找不到beanId:[{0}],SessionManager类型的bean,将创建默认的DefaultSessionManager", e, beanId);
        }
        if (manager == null) {
            manager = DefaultSessionManager.getSingleton();
        }
        return manager;
    }

}
