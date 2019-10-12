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
import com.xquant.database.config.trigger.Triggers;
import com.xquant.database.trigger.TriggerProcessor;
import com.xquant.database.util.DataBaseUtil;
import com.xquant.file.FileObject;
import com.xquant.fileresolver.impl.AbstractFileProcessor;
import com.xquant.xml.XStreamFactory;

/**
 * 触发器文件处理
 *
 * @author renhui
 */
public class TriggerFileProcessor extends AbstractFileProcessor {

    private static final String TRIGGER_EXTFILENAME = ".trigger.xml";
    TriggerProcessor processor;

    public TriggerProcessor getProcessor() {
        return processor;
    }

    public void setProcessor(TriggerProcessor processor) {
        this.processor = processor;
    }

    public void process() {

        XStream stream = XStreamFactory
                .getXStream(DataBaseUtil.DATABASE_XSTREAM);
        for (FileObject fileObject : deleteList) {
            LOGGER.info( "正在移除trigger文件[{0}]",
                    fileObject.getAbsolutePath());
            Triggers triggers = (Triggers) caches
                    .get(fileObject.getAbsolutePath());
            if (triggers != null) {
                processor.removeTriggers(triggers);
                caches.remove(fileObject.getAbsolutePath());
            }
            LOGGER.info( "移除trigger文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
        for (FileObject fileObject : changeList) {
            LOGGER.info( "正在加载trigger文件[{0}]",
                    fileObject.getAbsolutePath());
            Triggers oldTriggers = (Triggers) caches
                    .get(fileObject.getAbsolutePath());
            if (oldTriggers != null) {
                processor.removeTriggers(oldTriggers);
            }
            Triggers triggers = convertFromXml(stream, fileObject);
            processor.addTriggers(triggers);
            caches.put(fileObject.getAbsolutePath(), triggers);
            LOGGER.info( "加载trigger文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }

    }

    @Override
    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(TRIGGER_EXTFILENAME);
    }

}
