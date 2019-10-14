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
package org.tinygroup.remoteconfig.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Module implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 58712306119580740L;
    /**
     * 唯一标识
     */
    private String name;
    /**
     * 模块名
     */
    private String moduleName;

    private List<Module> subModules = new ArrayList<Module>();

    private Map<String, ConfigValue> configItemMap;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public List<Module> getSubModules() {
        return subModules;
    }

    public void setSubModules(List<Module> subModules) {
        this.subModules = subModules;
    }

    public Map<String, ConfigValue> getConfigItemMap() {
        return configItemMap;
    }

    public void setConfigItemMap(Map<String, ConfigValue> configItemMap) {
        this.configItemMap = configItemMap;
    }

}
