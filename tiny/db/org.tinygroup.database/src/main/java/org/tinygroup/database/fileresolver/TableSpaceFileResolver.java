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
import org.tinygroup.database.config.tablespace.TableSpaces;
import org.tinygroup.database.tablespace.TableSpaceProcessor;
import org.tinygroup.database.util.DataBaseUtil;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

/**
 * Created by wangwy11342 on 2016/8/1.
 */
public class TableSpaceFileResolver extends AbstractFileProcessor {
    private static final String CUSTOMSQL_EXTFILENAME = ".tablespace.xml";

    private TableSpaceProcessor tableSpaceProcessor;

    public TableSpaceProcessor getTableSpaceProcessor() {
        return tableSpaceProcessor;
    }

    public void setTableSpaceProcessor(TableSpaceProcessor tableSpaceProcessor) {
        this.tableSpaceProcessor = tableSpaceProcessor;
    }

    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(CUSTOMSQL_EXTFILENAME)
                || fileObject.getFileName().endsWith(".tablespace");
    }

    public void process() {
        XStream stream = XStreamFactory
                .getXStream(DataBaseUtil.DATABASE_XSTREAM);
        for (FileObject fileObject : deleteList) {
            LOGGER.logMessage(LogLevel.INFO, "正在移除tablespace文件[{0}]",
                    fileObject.getAbsolutePath());
            TableSpaces tableSpaces = (TableSpaces) caches.get(fileObject
                    .getAbsolutePath());
            if (tableSpaces != null) {
                tableSpaceProcessor.removeTableSpaces(tableSpaces);
                caches.remove(fileObject.getAbsolutePath());
            }
            LOGGER.logMessage(LogLevel.INFO, "移除tablespace文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
        for (FileObject fileObject : changeList) {
            LOGGER.logMessage(LogLevel.INFO, "正在加载tablespace文件[{0}]",
                    fileObject.getAbsolutePath());
            TableSpaces tableSpaces = (TableSpaces) stream.fromXML(fileObject
                    .getInputStream());
            TableSpaces oldTableSpaces = (TableSpaces) caches.get(fileObject
                    .getAbsolutePath());
            if (oldTableSpaces != null) {
                tableSpaceProcessor.removeTableSpaces(oldTableSpaces);
            }
            tableSpaceProcessor.addTableSpaces(tableSpaces);
            caches.put(fileObject.getAbsolutePath(), tableSpaces);
            LOGGER.logMessage(LogLevel.INFO, "加载tablespace文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
    }
}
