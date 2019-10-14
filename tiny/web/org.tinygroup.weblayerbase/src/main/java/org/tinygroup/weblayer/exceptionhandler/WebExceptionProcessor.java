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
package org.tinygroup.weblayer.exceptionhandler;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.config.impl.AbstractConfiguration;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.xmlparser.node.XmlNode;

import java.util.List;

/**
 * 功能说明:页面异常处理配置信息加载类
 * <p>
 * 开发人员: renhui <br>
 * 开发时间: 2013-9-22 <br>
 * <br>
 */
public class WebExceptionProcessor extends AbstractConfiguration {
    private static final String EXCEPTION_CONFIG_PATH = "/application/exception-config/exception-handlers";
    private WebExceptionHandlerManager webHandlerManager;

    public WebExceptionHandlerManager getWebHandlerManager() {
        return webHandlerManager;
    }

    public void setWebHandlerManager(
            WebExceptionHandlerManager webHandlerManager) {
        this.webHandlerManager = webHandlerManager;
    }

    public String getApplicationNodePath() {
        return EXCEPTION_CONFIG_PATH;
    }

    public String getComponentConfigPath() {
        return "webexceptionhandler.config.xml";
    }

    public void config(XmlNode applicationConfig, XmlNode componentConfig) {
        super.config(applicationConfig, componentConfig);
        List<XmlNode> webHandlerList = ConfigurationUtil.combineSubList(
                "web-exception-handler", applicationConfig, componentConfig);
        for (XmlNode handler : webHandlerList) {
            String exception = handler.getAttribute("exception");
            String handlerBean = handler.getAttribute("handler");
            LOGGER.logMessage(LogLevel.INFO,
                    "添加web-exception-handler,Exception:{0},handerBean:{1}",
                    exception, handlerBean);
            WebExceptionHandler exceptionHandler = BeanContainerFactory
                    .getBeanContainer(this.getClass().getClassLoader())
                    .getBean(handlerBean);
            try {
                webHandlerManager.addHandler(exception, exceptionHandler);
            } catch (ClassNotFoundException e) {
                LOGGER.logMessage(LogLevel.INFO,
                        "添加web-exception-handler出现异常,Exception类:{0}找不到",
                        e, handlerBean);
                continue;
            }
            LOGGER.logMessage(LogLevel.INFO,
                    "添加web-exception-handler,Exception:{0},handerBean:{1}完成",
                    exception, handlerBean);
        }
    }

}
