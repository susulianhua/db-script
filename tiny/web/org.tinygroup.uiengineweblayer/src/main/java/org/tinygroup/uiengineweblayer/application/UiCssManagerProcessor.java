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
package org.tinygroup.uiengineweblayer.application;

import org.tinygroup.application.AbstractApplicationProcessor;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.uiengineweblayer.UiCssManager;
import org.tinygroup.xmlparser.node.XmlNode;

import java.io.IOException;

public class UiCssManagerProcessor extends AbstractApplicationProcessor {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(UiCssManagerProcessor.class);

    private UiCssManager uiCssManager;
    private XmlNode applicationConfig;
    private XmlNode componentConfig;


    public UiCssManager getUiCssManager() {
        return uiCssManager;
    }

    public void setUiCssManager(UiCssManager uiCssManager) {
        this.uiCssManager = uiCssManager;
    }

    public void start() {
        LOGGER.logMessage(LogLevel.INFO, "UiCssManagerProcessor应用处理器操作CSS开始...");
        try {
            XmlNode totalConfig = ConfigurationUtil.combineXmlNode(applicationConfig, componentConfig);
            if (totalConfig == null) {
                totalConfig = new XmlNode("");
            }
            String cssName = totalConfig.getAttribute("cssName", "uiengine.uicss");
            String paramterName = totalConfig.getAttribute("paramterName", "cssno");
            long cssLimit = Long.parseLong(totalConfig.getAttribute("cssLimit", "50"));
            uiCssManager.setCssName(cssName);
            uiCssManager.setCssLimit(cssLimit * 1024);
            uiCssManager.setParamterName(paramterName);
            uiCssManager.createDynamicCss();
        } catch (IOException e) {
            LOGGER.errorMessage("UiCssManagerProcessor应用处理器操作CSS发生异常", e);
        }
        LOGGER.logMessage(LogLevel.INFO, "UiCssManagerProcessor应用处理器操作CSS结束!");
    }

    public void stop() {
        applicationConfig = null;
        componentConfig = null;
    }

    public String getApplicationNodePath() {
        return "/application/uicss-manager";
    }

    public String getComponentConfigPath() {
        return "/uicssmanager.config.xml";
    }

    public void config(XmlNode applicationConfig, XmlNode componentConfig) {
        this.applicationConfig = applicationConfig;
        this.componentConfig = componentConfig;
    }

    public XmlNode getComponentConfig() {
        return componentConfig;
    }

    public XmlNode getApplicationConfig() {
        return applicationConfig;
    }

    public int getOrder() {
        return 100;
    }

}
