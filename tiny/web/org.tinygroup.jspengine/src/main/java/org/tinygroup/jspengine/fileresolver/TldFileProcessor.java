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
package org.tinygroup.jspengine.fileresolver;

import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.vfs.FileObject;

public class TldFileProcessor extends AbstractFileProcessor {

    private static final String TLD_FILE_EXT_NAME = ".tld";

    public void process() {
        //设置符合的tld文件列表对象
        TldFileManager manager = TldFileManager.getInstance();
        for (FileObject fileObject : deleteList) {
            LOGGER.logMessage(LogLevel.INFO, "正在移除tld文件：<{}>",
                    fileObject.getAbsolutePath());
            manager.removeTldFile(fileObject);
            LOGGER.logMessage(LogLevel.INFO, "移除tld文件：<{}>结束",
                    fileObject.getAbsolutePath());
        }
        for (FileObject fileObject : changeList) {
            LOGGER.logMessage(LogLevel.INFO, "正在加载tld文件：<{}>",
                    fileObject.getAbsolutePath());
            manager.addTldFile(fileObject);
            LOGGER.logMessage(LogLevel.INFO, "加载tld文件：<{}>结束",
                    fileObject.getAbsolutePath());
        }
    }

    @Override
    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(TLD_FILE_EXT_NAME);
    }

}
