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
import org.tinygroup.database.config.table.Tables;
import org.tinygroup.database.table.TableProcessor;
import org.tinygroup.database.util.DataBaseUtil;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

public class TableFileResolver extends AbstractFileProcessor {

    private static final String TABLE_EXTFILENAME = ".table.xml";
    TableProcessor tableProcessor;

    public TableProcessor getTableProcessor() {
        return tableProcessor;
    }

    public void setTableProcessor(TableProcessor tableProcessor) {
        this.tableProcessor = tableProcessor;
    }

    public void process() {
        XStream stream = XStreamFactory
                .getXStream(DataBaseUtil.DATABASE_XSTREAM);
        for (FileObject fileObject : deleteList) {
            LOGGER.logMessage(LogLevel.INFO, "正在移除table文件[{0}]",
                    fileObject.getAbsolutePath());
            Tables tables = (Tables) caches.get(fileObject.getAbsolutePath());
            if (tables != null) {
                tableProcessor.removeTables(tables);
                caches.remove(fileObject.getAbsolutePath());
            }
            LOGGER.logMessage(LogLevel.INFO, "移除table文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
        for (FileObject fileObject : changeList) {
            LOGGER.logMessage(LogLevel.INFO, "正在加载table文件[{0}]",
                    fileObject.getAbsolutePath());
            Tables oldTables = (Tables) caches.get(fileObject.getAbsolutePath());
            if (oldTables != null) {
                tableProcessor.removeTables(oldTables);
            }
            Tables tables = convertFromXml(stream, fileObject);
            tableProcessor.addTables(tables);
            caches.put(fileObject.getAbsolutePath(), tables);
            tableProcessor.registerModifiedTime(tables, fileObject.getLastModifiedTime());
            LOGGER.logMessage(LogLevel.INFO, "加载table文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
    }

    @Override
    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(TABLE_EXTFILENAME) || fileObject.getFileName().endsWith(".table");
    }

}
