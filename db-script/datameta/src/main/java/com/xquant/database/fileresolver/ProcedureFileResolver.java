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
import com.xquant.database.config.procedure.Procedures;
import com.xquant.database.procedure.ProcedureProcessor;
import com.xquant.database.util.DataBaseUtil;
import com.xquant.vfs.FileObject;
import com.xquant.fileresolver.impl.AbstractFileProcessor;
import com.xquant.xml.XStreamFactory;

public class ProcedureFileResolver extends AbstractFileProcessor {
    private static final String PROCEDURE_EXTFILENAME = ".procedure.xml";
    ProcedureProcessor procedureProcessor;

    public ProcedureProcessor getProcedureProcessor() {
        return procedureProcessor;
    }

    public void setProcedureProcessor(ProcedureProcessor procedureProcessor) {
        this.procedureProcessor = procedureProcessor;
    }

    public void process() {
        XStream stream = XStreamFactory
                .getXStream(DataBaseUtil.DATABASE_XSTREAM);
        for (FileObject fileObject : deleteList) {
            LOGGER.info( "正在移除procedure文件[{0}]",
                    fileObject.getAbsolutePath());
            Procedures procedures = (Procedures) caches.get(fileObject.getAbsolutePath());
            if (procedures != null) {
                procedureProcessor.removeProcedures(procedures);
                caches.remove(fileObject.getAbsolutePath());
            }
            LOGGER.info( "移除procedure文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
        for (FileObject fileObject : changeList) {
            LOGGER.info( "正在加载procedure文件[{0}]",
                    fileObject.getAbsolutePath());
            Procedures oldProcedures = (Procedures) caches.get(fileObject.getAbsolutePath());
            if (oldProcedures != null) {
                procedureProcessor.removeProcedures(oldProcedures);
            }
            Procedures procedures = (Procedures) stream.fromXML(fileObject
                    .getInputStream());
            procedureProcessor.addProcedures(procedures);
            caches.put(fileObject.getAbsolutePath(), procedures);
            LOGGER.info( "加载procedure文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
    }

    @Override
    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(PROCEDURE_EXTFILENAME) || fileObject.getFileName().endsWith(".procedure");
    }

}
