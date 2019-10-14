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

import org.tinygroup.context.Context;
import org.tinygroup.menucommand.MenuCommandConstants;
import org.tinygroup.menucommand.config.MenuCommand;
import org.tinygroup.menucommand.config.MenuConfig;
import org.tinygroup.menucommand.config.SystemCommand;

/**
 * 列出命令的细节
 *
 * @author yancheng11334
 */
public class HelpCommandHandler extends SystemCommandHandler {

    protected void execute(String command, SystemCommand systemCommand,
                           MenuConfig config, Context context) {

        //按空格进行切分
        String[] keys = command.split("\\s+");
        if (keys != null && keys.length >= 2) {
            String key = keys[1].trim();

            //处理下级菜单
            if (config.getMenuConfigList() != null) {
                for (MenuConfig menuConfig : config.getMenuConfigList()) {
                    if (menuConfig.getName().equals(key)) {
                        context.put("helpConfig", config);
                        break;
                    }
                }
            }

            //处理命令
            if (config.getMenuCommandList() != null) {
                for (MenuCommand menuCommand : config.getMenuCommandList()) {
                    if (menuCommand.getName().equals(key)) {
                        context.put("helpCommand", menuCommand);
                    }
                }
            }

        } else {
            context.put("helpConfig", config);
        }

        context.put(MenuCommandConstants.RENDER_PAGE_PATH_NAME, systemCommand.getPath());
        context.put(MenuCommandConstants.GOTO_MENU_ID_NAME, config.getId());
    }

}
