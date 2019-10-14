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
package org.tinygroup.command.applicationprocessor;

import org.tinygroup.application.Application;
import org.tinygroup.application.ApplicationProcessor;
import org.tinygroup.command.CommandSystem;
import org.tinygroup.command.ConsoleCommander;
import org.tinygroup.config.impl.AbstractConfiguration;
import org.tinygroup.logger.LogLevel;

/**
 * 功能说明:命令插件
 * <p>
 * 开发人员: renhui <br>
 * 开发时间: 2013-11-18 <br>
 * <br>
 */
public class CommandProcessor extends AbstractConfiguration implements ApplicationProcessor {

    private static final String PLUGIN_COMMAND_NODE_PATH = "/application/plugin-command";
    private static final String PLUGIN_INSTANCE_NAME = "plugin";// plugin.command.xml中的package-name

    private CommandSystem command;
    private ConsoleCommander consoleCommander;


    public String getApplicationNodePath() {
        return PLUGIN_COMMAND_NODE_PATH;
    }

    public String getComponentConfigPath() {
        return null;
    }

    public void start() {
        LOGGER.logMessage(LogLevel.DEBUG, "启动插件命令行管理器");
        init();
        LOGGER.logMessage(LogLevel.DEBUG, "启动插件命令行管理器成功");
    }

    public void init() {
        LOGGER.logMessage(LogLevel.DEBUG, "初始化插件命令行管理器");
        command = CommandSystem.getInstance(PLUGIN_INSTANCE_NAME);
        consoleCommander = new ConsoleCommander(command);
        consoleCommander.start();
        LOGGER.logMessage(LogLevel.DEBUG, "初始化插件命令行管理器成功");
    }

    public void stop() {
        if (command != null) {
            command.execute("destroy");
        }
        if (consoleCommander != null && consoleCommander.isAlive()) {
            consoleCommander.stopRead();
        }
    }

    public void setApplication(Application application) {
        //do nothing
    }

    public int getOrder() {
        return DEFAULT_PRECEDENCE;
    }
}
