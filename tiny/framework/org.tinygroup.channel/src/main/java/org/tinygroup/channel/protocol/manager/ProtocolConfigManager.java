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
package org.tinygroup.channel.protocol.manager;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.channel.protocol.config.ProtocolConfigs;
import org.tinygroup.config.Configuration;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xstream.XStreamFactory;

//<protocol-configs>
//	<protocol-config id="a" bean="protocol1bean">
//   	<param key="" value="">
//   	<param key="" value="">
//   	<param key="" value="">
//   	<lisenter id="a" bean="lisentener1bean">
//        	<param key="" value="">
//        	<param key="" value="">
//   	</lisenter>
//	</protocol-config>
//</protocol-configs>
public class ProtocolConfigManager implements Configuration {
    private final static String XSTREAM_PACKAGE = "org.tinygroup.channel";
    private final static String APPLICATION_NODE_PATH = "/application/protocol-configs";
    private XmlNode applicationConfig;
    private ProtocolConfigs configs;

    public String getApplicationNodePath() {
        return APPLICATION_NODE_PATH;
    }

    public String getComponentConfigPath() {
        return null;
    }

    public void config(XmlNode applicationConfig, XmlNode componentConfig) {
        XStream xstream = XStreamFactory.getXStream(XSTREAM_PACKAGE);
        configs = (ProtocolConfigs) xstream.fromXML(applicationConfig.toString());
    }

    public XmlNode getComponentConfig() {
        return null;
    }

    public XmlNode getApplicationConfig() {
        return applicationConfig;
    }

    public ProtocolConfigs getProtocolConfigs() {
        return configs;
    }
}
