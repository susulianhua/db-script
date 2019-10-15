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
import com.xquant.database.ProcessorManager;
import com.xquant.database.config.processor.Processors;
import com.xquant.database.util.DataBaseUtil;
import com.xquant.vfs.FileObject;
import com.xquant.fileresolver.impl.AbstractFileProcessor;
import com.xquant.xml.XStreamFactory;

public class ProcessorFileResolver extends AbstractFileProcessor {
    private static final String PROCESSOR_EXTFILENAME = ".database.processor.xml";
    ProcessorManager processorManager;


    public ProcessorManager getProcessorManager() {
        return processorManager;
    }

    public void setProcessorManager(ProcessorManager processorManager) {
        this.processorManager = processorManager;
    }

    public void process() {
        LOGGER.info( "开始读取database.processor文件");
        XStream stream = XStreamFactory
                .getXStream(DataBaseUtil.PROCESSOR_XSTREAM);
        for (FileObject fileObject : deleteList) {
            LOGGER.info( "开始移除database.processor文件{0}",
                    fileObject.getAbsolutePath());
            Processors processors = (Processors) caches.get(fileObject.getAbsolutePath());
            if (processors != null) {
                processorManager.removeProcessors(processors);
                caches.remove(fileObject.getAbsolutePath());
            }
            LOGGER.info( "移除database.processor文件{0}完毕",
                    fileObject.getAbsolutePath());
        }
        for (FileObject fileObject : changeList) {
            LOGGER.info( "开始读取database.processor文件{0}",
                    fileObject.getAbsolutePath());
            Processors oldProcessors = (Processors) caches.get(fileObject.getAbsolutePath());
            if (oldProcessors != null) {
                processorManager.removeProcessors(oldProcessors);
            }
            Processors processors = convertFromXml(stream, fileObject);
            processorManager.addProcessors(processors);
            caches.put(fileObject.getAbsolutePath(), processors);
            LOGGER.info( "读取database.processor文件{0}完毕",
                    fileObject.getAbsolutePath());
        }
        LOGGER.info( "database.processor文件读取完毕");

    }

    @Override
    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(PROCESSOR_EXTFILENAME);
    }

}
