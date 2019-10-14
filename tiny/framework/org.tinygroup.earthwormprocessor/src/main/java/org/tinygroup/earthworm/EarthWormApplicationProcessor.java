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
package org.tinygroup.earthworm;

import org.tinygroup.application.Application;
import org.tinygroup.application.ApplicationProcessor;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.xmlparser.node.XmlNode;

public class EarthWormApplicationProcessor implements ApplicationProcessor {
    private final static String SAMPLETRIER_CLASS = "earthworm.sampletrier.class";
    private final static String LOGSUPPORT_CLASS = "earthworm.logsupport.class";

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
    }

    public void init() {
        String sampleTrierClass = ConfigurationUtil.getConfigurationManager().getConfiguration(SAMPLETRIER_CLASS);
        if (!StringUtil.isBlank(sampleTrierClass)) {
            EarthWorm.setSampleTrier(sampleTrierClass);
        }
        String logSupportClass = ConfigurationUtil.getConfigurationManager().getConfiguration(LOGSUPPORT_CLASS);
        if (!StringUtil.isBlank(logSupportClass)) {
            LogService.setLogSupport(logSupportClass);
        }
    }

    public void stop() {

    }

    @Override
    public void setApplication(Application application) {
        // TODO Auto-generated method stub

    }

}
