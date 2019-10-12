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
import com.xquant.database.config.customsql.CustomSqls;
import com.xquant.database.customesql.CustomSqlProcessor;
import com.xquant.database.util.DataBaseUtil;
import com.xquant.file.FileObject;
import com.xquant.fileresolver.impl.AbstractFileProcessor;
import com.xquant.xml.XStreamFactory;

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
            LOGGER.info( "正在移除customsql文件[{0}]",
                    fileObject.getAbsolutePath());
            CustomSqls customsqls = (CustomSqls) caches.get(fileObject
                    .getAbsolutePath());
            if (customsqls != null) {
                customSqlProcessor.removeCustomSqls(customsqls);
                caches.remove(fileObject.getAbsolutePath());
            }
            LOGGER.info( "移除customsql文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
        for (FileObject fileObject : changeList) {
            LOGGER.info( "正在加载customsql文件[{0}]",
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
            LOGGER.info( "加载customsql文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
    }

    @Override
    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(CUSTOMSQL_EXTFILENAME)
                || fileObject.getFileName().endsWith(".customsql");
    }

}
