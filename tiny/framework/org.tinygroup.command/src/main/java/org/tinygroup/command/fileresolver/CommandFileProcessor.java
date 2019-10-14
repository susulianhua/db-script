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
package org.tinygroup.command.fileresolver;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.command.CommandSystem;
import org.tinygroup.command.config.Commands;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import java.io.InputStream;

public class CommandFileProcessor extends AbstractFileProcessor {

    private static final String COMMAND_EXT_FILENAME = ".commands.xml";

    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(COMMAND_EXT_FILENAME);
    }

    public void process() {

        XStream stream = XStreamFactory
                .getXStream(CommandSystem.COMMANDS_XSTREAM);
        for (FileObject fileObject : fileObjects) {
            LOGGER.logMessage(LogLevel.INFO, "正在加载Commands文件[{0}]",
                    fileObject.getAbsolutePath());
            try {
                InputStream inputStream = fileObject
                        .getInputStream();
                Commands commands = (Commands) stream.fromXML(inputStream);
                inputStream.close();
                CommandSystem.getInstance(commands.getPackageName(), commands,
                        System.out);
            } catch (Exception e) {
                LOGGER.errorMessage("加载Commands文件[{0}]出现异常", e,
                        fileObject.getAbsolutePath());
            }

            LOGGER.logMessage(LogLevel.INFO, "加载Commands文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }

    }

}
