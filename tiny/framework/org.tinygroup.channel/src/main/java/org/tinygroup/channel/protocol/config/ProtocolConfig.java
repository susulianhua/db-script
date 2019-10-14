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
package org.tinygroup.channel.protocol.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("protocol-config")
public class ProtocolConfig {
    public final static String TYPE_IN = "in";
    public final static String TYPE_OUT = "out";
    @XStreamAsAttribute
    private String id;
    @XStreamAsAttribute
    private String bean;
    @XStreamAsAttribute
    private String type;
    @XStreamImplicit
    private List<ParamConfig> params;
    @XStreamImplicit
    private List<ProtocolListenerConfig> listeners;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBean() {
        return bean;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    public List<ParamConfig> getParams() {
        if (params == null) {
            params = new ArrayList<ParamConfig>();
        }
        return params;
    }

    public void setParams(List<ParamConfig> params) {
        this.params = params;
    }

    public List<ProtocolListenerConfig> getListeners() {
        if (listeners == null) {
            listeners = new ArrayList<ProtocolListenerConfig>();
        }
        return listeners;
    }

    public void setListeners(List<ProtocolListenerConfig> listeners) {
        this.listeners = listeners;
    }

}
