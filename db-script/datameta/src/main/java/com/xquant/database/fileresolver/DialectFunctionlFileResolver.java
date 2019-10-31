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
import com.xquant.dialectfunction.DialectFunctionProcessor;
import com.xquant.dialectfunction.DialectFunctions;
import com.xquant.database.util.DataBaseUtil;
import com.xquant.fileresolver.impl.AbstractFileProcessor;
import com.xquant.vfs.FileObject;
import com.xquant.xml.XStreamFactory;

public class DialectFunctionlFileResolver extends AbstractFileProcessor {

    private static final String FUNCTION_EXTFILENAME = ".dialectfunction.xml";
    DialectFunctionProcessor functionProcessor;


    public DialectFunctionProcessor getFunctionProcessor() {
        return functionProcessor;
    }

    public void setFunctionProcessor(DialectFunctionProcessor functionProcessor) {
        this.functionProcessor = functionProcessor;
    }

    public void process() {
        XStream stream = XStreamFactory
                .getXStream(DataBaseUtil.DATABASE_XSTREAM);
        for (FileObject fileObject : deleteList) {
            LOGGER.info( "正在移除function文件[{0}]",
                    fileObject.getAbsolutePath());
            DialectFunctions functions = (DialectFunctions) caches.get(fileObject.getAbsolutePath());
            if (functions != null) {
                functionProcessor.removeDialectFunctions(functions);
                caches.remove(fileObject.getAbsolutePath());
            }
            LOGGER.info( "移除function文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
        for (FileObject fileObject : changeList) {
            LOGGER.info( "正在加载function文件[{0}]",
                    fileObject.getAbsolutePath());
            DialectFunctions oldFunctions = (DialectFunctions) caches.get(fileObject.getAbsolutePath());
            if (oldFunctions != null) {
                functionProcessor.removeDialectFunctions(oldFunctions);
            }
            DialectFunctions functions = convertFromXml(stream, fileObject);
            functionProcessor.addDialectFunctions(functions);
            caches.put(fileObject.getAbsolutePath(), functions);
            LOGGER.info( "加载function文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
    }

    @Override
    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(FUNCTION_EXTFILENAME) || fileObject.getFileName().endsWith(".dialectfunction");
    }

}
