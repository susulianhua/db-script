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
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 菜单命令(仅在当前菜单有效)
 *
 * @author yancheng11334
 */
@XStreamAlias("menu-command")
public class MenuCommand extends BaseCommand {

    /**
     * 获得菜单命令的所属菜单
     */
    private MenuConfig menuConfig;

    /**
     * 事件类型，目前支持enter和exit两种事件
     */
    @XStreamAlias("event-type")
    @XStreamAsAttribute
    private String eventType;

    /**
     * 本菜单是否支持系统命令
     */
    @XStreamAlias("system-enable")
    @XStreamAsAttribute
    private boolean systemEnable;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public MenuConfig getMenuConfig() {
        return menuConfig;
    }

    public void setMenuConfig(MenuConfig menuConfig) {
        this.menuConfig = menuConfig;
    }

    public boolean isSystemEnable() {
        return systemEnable;
    }

    public void setSystemEnable(boolean systemEnable) {
        this.systemEnable = systemEnable;
    }

}
