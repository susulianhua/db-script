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
package org.tinygroup.cepcore.impl;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.config.impl.AbstractConfiguration;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.exceptionhandler.ExceptionHandler;
import org.tinygroup.exceptionhandler.ExceptionHandlerManager;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.xmlparser.node.XmlNode;

import java.util.List;

public class ExceptionProcessor extends AbstractConfiguration {

    private static final String EXCEPTION_CONFIG_PATH = "/application/exception-config/exception-handlers";
    private ExceptionHandlerManager manager;

    public ExceptionHandlerManager getManager() {
        return manager;
    }

    public void setManager(ExceptionHandlerManager manager) {
        this.manager = manager;
    }

    public void config(XmlNode applicationConfig, XmlNode componentConfig) {
        super.config(applicationConfig, componentConfig);
        List<XmlNode> handlerList = ConfigurationUtil.combineSubList(
                "exception-handler", applicationConfig, componentConfig);
        for (XmlNode handler : handlerList) {
            String exception = handler.getAttribute("exception");
            String handlerBean = handler.getAttribute("handler");
            LOGGER.logMessage(LogLevel.INFO,
                    "添加exception-handler,Exception:{0},handerBean:{1}",
                    exception, handlerBean);
            ExceptionHandler<?> exceptionHandler = BeanContainerFactory
                    .getBeanContainer(this.getClass().getClassLoader())
                    .getBean(handlerBean);
            try {
                manager.addHandler(exception, exceptionHandler);
            } catch (ClassNotFoundException e) {
//				logger.logMessage(LogLevel.INFO,
//						"添加exception-handler出现异常,Exception类:{0}找不到", exception,
//						handlerBean);
                LOGGER.errorMessage("添加exception-handler出现异常,Exception类:{0}找不到", e, exception);
                continue;
            }
            LOGGER.logMessage(LogLevel.INFO,
                    "添加exception-handler,Exception:{0},handerBean:{1}完成",
                    exception, handlerBean);
        }
    }

    public String getApplicationNodePath() {
        return EXCEPTION_CONFIG_PATH;
    }

    public String getComponentConfigPath() {
        return "/exceptionhandler.config.xml";
    }

}
