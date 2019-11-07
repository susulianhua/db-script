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
package com.xquant.database.fileresolver;

import com.thoughtworks.xstream.XStream;
import com.xquant.database.config.table.Tables;
import com.xquant.database.table.TableProcessor;
import com.xquant.database.util.DataBaseUtil;
import com.xquant.vfs.FileObject;
import com.xquant.fileresolver.impl.AbstractFileProcessor;
import com.xquant.xml.XStreamFactory;

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
       /* for (FileObject fileObject : deleteList) {
            LOGGER.info( "正在移除table文件[{0}]",
                    fileObject.getAbsolutePath());
            Tables tables = (Tables) caches.get(fileObject.getAbsolutePath());
            if (tables != null) {
                tableProcessor.removeTables(tables);
                caches.remove(fileObject.getAbsolutePath());
            }
            LOGGER.info( "移除table文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }*/
        for (FileObject fileObject : changeList) {
            LOGGER.info( "正在加载table文件[{0}]",
                    fileObject.getAbsolutePath());
            Tables oldTables = (Tables) caches.get(fileObject.getAbsolutePath());
            if (oldTables != null) {
                tableProcessor.removeTables(oldTables);
            }
            Tables tables = convertFromXml(stream, fileObject);
            tableProcessor.addTables(tables);
            caches.put(fileObject.getAbsolutePath(), tables);
            tableProcessor.registerModifiedTime(tables, fileObject.getLastModifiedTime());
            LOGGER.info( "加载table文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
    }

    @Override
    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(TABLE_EXTFILENAME) || fileObject.getFileName().endsWith(".table");
    }

}
