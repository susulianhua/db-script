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
package org.tinygroup.database.fileresolver;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.database.config.customsql.CustomSqls;
import org.tinygroup.database.customesql.CustomSqlProcessor;
import org.tinygroup.database.util.DataBaseUtil;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

public class CustomSqlFileResolver extends AbstractFileProcessor {

    private static final String CUSTOMSQL_EXTFILENAME = ".customsql.xml";
    CustomSqlProcessor customSqlProcessor;

    public CustomSqlProcessor getCustomSqlProcessor() {
        return customSqlProcessor;
    }

    public void setCustomSqlProcessor(CustomSqlProcessor customSqlProcessor) {
        this.customSqlProcessor = customSqlProcessor;
    }

    public void process() {
        XStream stream = XStreamFactory
                .getXStream(DataBaseUtil.DATABASE_XSTREAM);
        for (FileObject fileObject : deleteList) {
            LOGGER.logMessage(LogLevel.INFO, "正在移除customsql文件[{0}]",
                    fileObject.getAbsolutePath());
            CustomSqls customsqls = (CustomSqls) caches.get(fileObject
                    .getAbsolutePath());
            if (customsqls != null) {
                customSqlProcessor.removeCustomSqls(customsqls);
                caches.remove(fileObject.getAbsolutePath());
            }
            LOGGER.logMessage(LogLevel.INFO, "移除customsql文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
        for (FileObject fileObject : changeList) {
            LOGGER.logMessage(LogLevel.INFO, "正在加载customsql文件[{0}]",
                    fileObject.getAbsolutePath());
            CustomSqls customsqls = convertFromXml(stream, fileObject);
            CustomSqls oldCustomsqls = (CustomSqls) caches.get(fileObject
                    .getAbsolutePath());
            if (oldCustomsqls != null) {
                customSqlProcessor.removeCustomSqls(oldCustomsqls);
            }

            customSqlProcessor.addCustomSqls(customsqls);
            caches.put(fileObject.getAbsolutePath(), customsqls);
            customSqlProcessor.registerModifiedTime(customsqls, fileObject.getLastModifiedTime());
            LOGGER.logMessage(LogLevel.INFO, "加载customsql文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
    }

    @Override
    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(CUSTOMSQL_EXTFILENAME)
                || fileObject.getFileName().endsWith(".customsql");
    }

}
