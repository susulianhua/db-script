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
package org.tinygroup.flow.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 组件接口
 *
 * @author luoguo
 */
@XStreamAlias("component")
public class Component implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -4678446337219023016L;
    /**
     * className对应的类的属性配置
     */
    private List<FlowProperty> properties;
    @XStreamAsAttribute
    private String name;
    @XStreamAsAttribute
    private String title;
    private String description;

    private transient Map<String, FlowProperty> propertyMap;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, FlowProperty> getPropertyMap() {
        if (propertyMap == null) {
            propertyMap = new HashMap<String, FlowProperty>();
            for (FlowProperty property : getProperties()) {
                propertyMap.put(property.getName(), property);
            }
        }
        return propertyMap;
    }

    public List<FlowProperty> getProperties() {
        if (properties == null)
            properties = new ArrayList<FlowProperty>();
        return properties;
    }

    public void setProperties(List<FlowProperty> properties) {
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void combile(Component component) {
        if (name == null) {
            name = component.getName();
        }
        if (title == null) {
            title = component.getTitle();
        }
        List<FlowProperty> parentProperties = component.getProperties();
        if (properties == null) {
            properties = parentProperties;
            return;
        }
        if (parentProperties != null) {
            // if (properties == null) { //此处有问题，合并不全，
            // if中没有改map，else没有改properties
            // properties = parentProperties;
            // } else {
            for (FlowProperty pProperty : parentProperties) {
                FlowProperty myProperty = getPropertyMap().get(
                        pProperty.getName());
                if (myProperty == null) {
                    getPropertyMap().put(pProperty.getName(), pProperty);
                }
            }
            // }
        }
    }

}
