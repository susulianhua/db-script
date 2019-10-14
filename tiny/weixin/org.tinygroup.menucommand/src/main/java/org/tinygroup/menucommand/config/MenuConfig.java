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
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.List;

/**
 * 定义菜单
 *
 * @author yancheng11334
 */
@XStreamAlias("menu-config")
public class MenuConfig extends BaseObject {

    /**
     * 上级菜单
     */
    @XStreamOmitField
    private MenuConfig parent;

    /**
     * 子菜单列表
     */
    @XStreamImplicit
    private List<MenuConfig> menuConfigList;

    /**
     * 菜单命令列表
     */
    @XStreamImplicit
    private List<MenuCommand> menuCommandList;

    public List<MenuConfig> getMenuConfigList() {
        return menuConfigList;
    }

    public void setMenuConfigList(List<MenuConfig> menuConfigList) {
        this.menuConfigList = menuConfigList;
    }

    public List<MenuCommand> getMenuCommandList() {
        return menuCommandList;
    }

    public void setMenuCommandList(List<MenuCommand> menuCommandList) {
        this.menuCommandList = menuCommandList;
    }

    public MenuConfig getParent() {
        return parent;
    }

    public void setParent(MenuConfig parent) {
        this.parent = parent;
    }

    /**
     * 得到匹配的子菜单配置
     *
     * @param command
     * @return
     */
    public MenuConfig getMatchMenuConfig(String command) {
        if (menuConfigList != null) {
            for (MenuConfig menuConfig : menuConfigList) {
                if (menuConfig.match(command)) {
                    return menuConfig;
                }
            }
        }
        return null;
    }

    /**
     * 得到匹配的子菜单命令
     *
     * @param command
     * @return
     */
    public MenuCommand getMatchMenuCommand(String command) {
        if (menuCommandList != null) {
            for (MenuCommand menuCommand : menuCommandList) {
                if (menuCommand.match(command)) {
                    return menuCommand;
                }
            }
        }
        return null;
    }

    /**
     * 获得菜单所在路径
     *
     * @return
     */
    public String getMenuConfigPath() {
        MenuConfig config = this;
        String showpath = config.getTitle();
        while (config.getParent() != null) {
            config = config.getParent();
            showpath = config.getTitle() + "/" + showpath;
        }
        return showpath;
    }

    /**
     * 根据事件类型进行命令匹配
     *
     * @param eventType
     * @return
     */
    public MenuCommand matchEvent(String eventType) {
        if (eventType == null) {
            return null;
        }
        if (menuCommandList != null) {
            for (MenuCommand menuCommand : menuCommandList) {
                if (eventType.equals(menuCommand.getEventType())) {
                    return menuCommand;
                }
            }
        }
        return null;
    }

}
