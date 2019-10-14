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
import org.tinygroup.menucommand.config.MenuConfig;
import org.tinygroup.menucommand.config.SystemCommand;

/**
 * 查询系统命令
 *
 * @author yancheng11334
 */
public class QueryCommandHandler extends SystemCommandHandler {

    protected void execute(String command, SystemCommand systemCommand, MenuConfig config, Context context) {
        //按空格进行切分
        String[] keys = command.split("\\s+");
        if (keys != null && keys.length >= 2) {
            context.put(MenuCommandConstants.USER_QUERY_KEY_NAME, keys[1]);
        }
        context.put(MenuCommandConstants.RENDER_PAGE_PATH_NAME, systemCommand.getPath());
        context.put(MenuCommandConstants.GOTO_MENU_ID_NAME, config.getId());
    }

    public void afterExecute(Context context) {
        super.afterExecute(context);
        context.remove(MenuCommandConstants.USER_QUERY_KEY_NAME);
    }

}
