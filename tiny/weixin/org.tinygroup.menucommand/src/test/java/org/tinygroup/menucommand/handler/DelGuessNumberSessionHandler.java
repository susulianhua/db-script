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
import org.tinygroup.menucommand.GuessNumberOperator;
import org.tinygroup.menucommand.MenuCommandConstants;
import org.tinygroup.menucommand.config.MenuCommand;

/**
 * 清理游戏数据
 *
 * @author yancheng11334
 */
public class DelGuessNumberSessionHandler extends MenuCommandHandler {

    private GuessNumberOperator operator = new GuessNumberOperator();

    @Override
    protected void execute(String command, MenuCommand menuCommand,
                           Context context) {
        String userId = context.get("userId");
        operator.removeGuessNumberSession(userId);

        //返回上级菜单
        context.put(MenuCommandConstants.MENU_CONFIG_PAGE_NAME, menuCommand.getMenuConfig().getParent());
        context.put(MenuCommandConstants.GOTO_MENU_ID_NAME, menuCommand.getMenuConfig().getParent().getId());
    }

}
