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
package org.tinygroup.menucommand;

import org.tinygroup.context.Context;
import org.tinygroup.menucommand.config.MenuConfig;
import org.tinygroup.menucommand.config.MenuConfigs;
import org.tinygroup.menucommand.config.SystemCommand;

import java.util.List;

/**
 * 菜单管理器
 *
 * @author yancheng11334
 */
public interface MenuConfigManager {

    /**
     * 添加一组定义菜单
     *
     * @param configs
     */
    void addMenuConfigs(MenuConfigs configs);

    /**
     * 删除一组定义菜单
     *
     * @param configs
     */
    void removeMenuConfigs(MenuConfigs configs);

    /**
     * 添加定义菜单
     *
     * @param config
     */
    void addMenuConfig(MenuConfig config);

    /**
     * 删除定义菜单
     *
     * @param config
     */
    void removeMenuConfig(MenuConfig config);

    /**
     * 获得指定的菜单
     *
     * @param menuId
     * @return
     */
    MenuConfig getMenuConfig(String menuId);

    /**
     * 添加系统命令
     *
     * @param command
     */
    void addSystemCommand(SystemCommand command);

    /**
     * 删除系统命令
     *
     * @param command
     */
    void removeSystemCommand(SystemCommand command);

    /**
     * 匹配系统命令
     *
     * @param command
     * @return
     */
    SystemCommand getSystemCommand(String command);

    /**
     * 获得支持的系统命令列表
     *
     * @return
     */
    List<SystemCommand> getSystemCommandList();

    /**
     * 得到对应的命令执行器
     *
     * @param menuId
     * @param command
     * @param context
     * @return
     */
    CommandExecutor getCommandExecutor(String menuId, String command, Context context);
}
