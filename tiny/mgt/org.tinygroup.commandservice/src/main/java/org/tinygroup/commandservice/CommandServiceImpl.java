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
package org.tinygroup.commandservice;

import org.tinygroup.command.CommandSystem;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class CommandServiceImpl {
    private Logger logger = LoggerFactory.getLogger(CommandServiceImpl.class);

    public void execute(String sysName, String cmd) {
        CommandSystem sys = CommandSystem.getInstance(sysName);
        if (sys != null) {
            sys.execute(cmd);
        } else {
            logger.logMessage(LogLevel.ERROR, "命令系统{0}不存在", sysName);
        }

    }
}
