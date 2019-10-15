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
import com.xquant.database.config.sequence.Sequences;
import com.xquant.database.sequence.SequenceProcessor;
import com.xquant.database.util.DataBaseUtil;
import com.xquant.vfs.FileObject;
import com.xquant.fileresolver.impl.AbstractFileProcessor;
import com.xquant.xml.XStreamFactory;

/**
 * 序列文件处理
 *
 * @author renhui
 */
public class SequenceFileProcessor extends AbstractFileProcessor {

    private static final String TRIGGER_EXTFILENAME = ".sequence.xml";
    SequenceProcessor processor;

    public SequenceProcessor getProcessor() {
        return processor;
    }

    public void setProcessor(SequenceProcessor processor) {
        this.processor = processor;
    }

    public void process() {
        XStream stream = XStreamFactory
                .getXStream(DataBaseUtil.DATABASE_XSTREAM);
        for (FileObject fileObject : deleteList) {
            LOGGER.info( "正在移除sequence文件[{0}]",
                    fileObject.getAbsolutePath());
            Sequences sequences = (Sequences) caches
                    .get(fileObject.getAbsolutePath());
            if (sequences != null) {
                processor.removeSequences(sequences);
                caches.remove(fileObject.getAbsolutePath());
            }
            LOGGER.info( "移除sequence文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
        for (FileObject fileObject : changeList) {
            LOGGER.info( "正在加载sequence文件[{0}]",
                    fileObject.getAbsolutePath());
            Sequences oldSequences = (Sequences) caches
                    .get(fileObject.getAbsolutePath());
            if (oldSequences != null) {
                processor.removeSequences(oldSequences);
            }
            Sequences sequences = convertFromXml(stream, fileObject);
            processor.addSequences(sequences);
            caches.put(fileObject.getAbsolutePath(), sequences);
            LOGGER.info( "加载sequence文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }

    }

    @Override
    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(TRIGGER_EXTFILENAME);
    }

}
