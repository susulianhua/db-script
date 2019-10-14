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
package org.tinygroup.config.impl;

import org.tinygroup.config.Configuration;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.xmlparser.node.XmlNode;

public abstract class AbstractConfiguration implements Configuration {
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractConfiguration.class);
    protected XmlNode applicationConfig;
    protected XmlNode componentConfig;

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

}
