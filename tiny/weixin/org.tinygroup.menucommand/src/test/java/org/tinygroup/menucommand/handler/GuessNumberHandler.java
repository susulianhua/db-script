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
import org.tinygroup.menucommand.GuessNumberSession;
import org.tinygroup.menucommand.config.MenuCommand;

/**
 * 判断数字的业务逻辑
 *
 * @author yancheng11334
 */
public class GuessNumberHandler extends MenuCommandHandler {

    private GuessNumberOperator operator = new GuessNumberOperator();

    @Override
    protected void execute(String command, MenuCommand menuCommand,
                           Context context) {
        String userId = context.get("userId");
        GuessNumberSession session = operator.getGuessNumberSession(userId);
        try {
            int num = Integer.parseInt(command);
            if (num < 1 || num > 100) {
                context.put("gamestatus", "error");
                return;
            }

            session.count++;
            if (num > session.num) {
                context.put("gamestatus", "large");
            } else if (num < session.num) {
                context.put("gamestatus", "small");
            } else {
                context.put("gamestatus", "right");
                context.put("gamenumber", session.count);
            }
            operator.addGuessNumberSession(session);
        } catch (Exception e) {
            context.put("gamestatus", "error");
        }

    }

}
