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
package org.tinygroup.menucommand.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * 命令菜单定义
 *
 * @author yancheng11334
 */
@XStreamAlias("menu-configs")
public class MenuConfigs {

    /**
     * 菜单结构组
     */
    @XStreamImplicit
    private List<MenuConfig> menuConfigList;

    /**
     * 系统命令组
     */
    @XStreamImplicit
    private List<SystemCommand> systemCommandList;


    public List<MenuConfig> getMenuConfigList() {
        return menuConfigList;
    }

    public void setMenuConfigList(List<MenuConfig> menuConfigList) {
        this.menuConfigList = menuConfigList;
    }

    public List<SystemCommand> getSystemCommandList() {
        return systemCommandList;
    }

    public void setSystemCommandList(List<SystemCommand> systemCommandList) {
        this.systemCommandList = systemCommandList;
    }

}
