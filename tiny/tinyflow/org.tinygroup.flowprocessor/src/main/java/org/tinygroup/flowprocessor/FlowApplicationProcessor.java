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
package org.tinygroup.flowprocessor;

import org.tinygroup.application.Application;
import org.tinygroup.application.ApplicationProcessor;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.flow.FlowExecutor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.xmlparser.node.XmlNode;

public class FlowApplicationProcessor implements ApplicationProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowApplicationProcessor.class);
    private FlowEventProcessor flowprocessor;
    private CEPCore cepcore;
    private FlowExecutor executor;

    public String getApplicationNodePath() {
        return null;
    }

    public String getComponentConfigPath() {
        return null;
    }

    public void config(XmlNode applicationConfig, XmlNode componentConfig) {
    }

    public XmlNode getComponentConfig() {
        return null;
    }

    public XmlNode getApplicationConfig() {
        return null;
    }

    public int getOrder() {
        return 0;
    }

    public void start() {
        LOGGER.logMessage(LogLevel.INFO, "启动FlowApplicationProcessor");
        cepcore.registerEventProcessor(flowprocessor);
        LOGGER.logMessage(LogLevel.INFO, "启动FlowApplicationProcessor完成");
    }

    public void init() {
        LOGGER.logMessage(LogLevel.INFO, "初始化FlowApplicationProcessor");
        initProcessors();
        LOGGER.logMessage(LogLevel.INFO, "初始化FlowApplicationProcessor完成");
    }

    private void initProcessors() {
        flowprocessor = new FlowEventProcessor();
        flowprocessor.setExecutor(executor);
    }

    public void stop() {
        LOGGER.logMessage(LogLevel.INFO, "停止FlowApplicationProcessor");
        cepcore.unregisterEventProcessor(flowprocessor);
        LOGGER.logMessage(LogLevel.INFO, "停止FlowApplicationProcessor完成");
    }

    public void setApplication(Application application) {
    }

    public CEPCore getCepcore() {
        return cepcore;
    }

    public void setCepcore(CEPCore cepcore) {
        this.cepcore = cepcore;
    }

    public FlowExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(FlowExecutor executor) {
        this.executor = executor;
    }


}
