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
package org.tinygroup.application.impl;

import org.tinygroup.application.AbstractApplicationProcessor;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.xmlparser.node.XmlNode;

public class LogCleanProcessor extends AbstractApplicationProcessor {

    public void start() {
        // TODO Auto-generated method stub

    }

    public void stop() {
        LoggerFactory.clearAllLoggers();
    }

    public String getApplicationNodePath() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getComponentConfigPath() {
        // TODO Auto-generated method stub
        return null;
    }

    public void config(XmlNode applicationConfig, XmlNode componentConfig) {
        // TODO Auto-generated method stub

    }

    public XmlNode getComponentConfig() {
        // TODO Auto-generated method stub
        return null;
    }

    public XmlNode getApplicationConfig() {
        // TODO Auto-generated method stub
        return null;
    }

    public int getOrder() {
        // TODO Auto-generated method stub
        return 0;
    }

}
