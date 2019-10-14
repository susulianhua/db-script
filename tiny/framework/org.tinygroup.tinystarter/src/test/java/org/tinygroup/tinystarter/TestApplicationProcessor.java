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
package org.tinygroup.tinystarter;

import org.tinygroup.application.AbstractApplicationProcessor;
import org.tinygroup.xmlparser.node.XmlNode;

public class TestApplicationProcessor extends AbstractApplicationProcessor {
    private boolean started;
    private XmlNode applicationConfig;
    private XmlNode componentConfig;

    @Override
    public void start() {
        started = true;
    }

    @Override
    public void stop() {

    }

    @Override
    public String getApplicationNodePath() {
        return "/application/test-config";
    }

    @Override
    public String getComponentConfigPath() {
        return null;
    }

    @Override
    public void config(XmlNode applicationConfig, XmlNode componentConfig) {
        this.applicationConfig = applicationConfig;
        this.componentConfig = componentConfig;
    }

    @Override
    public XmlNode getComponentConfig() {
        return componentConfig;
    }

    @Override
    public XmlNode getApplicationConfig() {
        return applicationConfig;
    }

    @Override
    public int getOrder() {
        return DEFAULT_PRECEDENCE;
    }

    public boolean isStarted() {
        return started;
    }

}
