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
package org.tinygroup.menucommand.handler;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.context.Context;
import org.tinygroup.menucommand.MenuCommandConstants;
import org.tinygroup.menucommand.config.MenuCommand;
import org.tinygroup.menucommand.config.MenuConfig;
import org.tinygroup.menucommand.config.SystemCommand;

/**
 * 默认的命令展示handler
 *
 * @author yancheng11334
 */
public class ShowCommandHandler extends SystemCommandHandler {

    protected void execute(String command, SystemCommand systemCommand,
                           MenuConfig config, Context context) {
        //存在用户菜单路径
        if (config != null && !StringUtil.isEmpty(config.getPath())) {
            context.put(MenuCommandConstants.RENDER_PAGE_PATH_NAME, config.getPath());
        }
        dealEnterEvent(command, config, context);
        context.put(MenuCommandConstants.GOTO_MENU_ID_NAME, config.getId());
        context.put(MenuCommandConstants.MENU_CONFIG_PAGE_NAME, config);
    }

    /**
     * 只有满足菜单位置发生变动和用户配置Enter事件处理器才执行
     *
     * @param config
     * @param context
     */
    protected void dealEnterEvent(String command, MenuConfig config, Context context) {
        String oldMenuId = context.get(MenuCommandConstants.BEFORE_MENU_ID_NAME);
        //菜单发生变动
        if (!config.getId().equals(oldMenuId)) {
            MenuCommand menuCommand = config.matchEvent(MenuCommandConstants.ENTER_EVENT_TYPE);
            if (menuCommand != null) {
                MenuCommandHandler handler = menuCommand.createCommandObject();
                if (handler != null) {
                    //执行操作
                    handler.execute(command, menuCommand, context);
                    //存在子操作的菜单路径
                    if (!StringUtil.isEmpty(menuCommand.getPath())) {
                        context.put(MenuCommandConstants.RENDER_PAGE_PATH_NAME, menuCommand.getPath());
                    }
                }

            }

        }
    }

}
