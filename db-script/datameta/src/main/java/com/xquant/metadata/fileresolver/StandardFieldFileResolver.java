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
package com.xquant.metadata.fileresolver;

import com.thoughtworks.xstream.XStream;
import com.xquant.file.FileObject;
import com.xquant.fileresolver.impl.AbstractFileProcessor;
import com.xquant.metadata.config.stdfield.StandardFields;
import com.xquant.metadata.stdfield.StandardFieldProcessor;
import com.xquant.metadata.util.MetadataUtil;
import com.xquant.xml.XStreamFactory;

public class StandardFieldFileResolver extends AbstractFileProcessor {

    private static final String STANDARDFIELD_EXTFILENAME = ".stdfield.xml";
    private StandardFieldProcessor standardFieldProcessor;

    public StandardFieldProcessor getStandardFieldProcessor() {
        return standardFieldProcessor;
    }

    public void setStandardFieldProcessor(
            StandardFieldProcessor standardFieldProcessor) {
        this.standardFieldProcessor = standardFieldProcessor;
    }

    public void process() {
        XStream stream = XStreamFactory
                .getXStream(MetadataUtil.METADATA_XSTREAM);
        for (FileObject fileObject : deleteList) {
            LOGGER.info( "正在移除stdfield文件[{0}]",
                    fileObject.getAbsolutePath());
            StandardFields standardFields = (StandardFields) caches.get(fileObject.getAbsolutePath());
            if (standardFields != null) {
                standardFieldProcessor.removeStandardFields(standardFields);
                caches.remove(fileObject.getAbsolutePath());
            }
            LOGGER.info( "移除stdfield文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
        for (FileObject fileObject : changeList) {
            LOGGER.info( "正在加载stdfield文件[{0}]",
                    fileObject.getAbsolutePath());
            StandardFields oldStandardFields = (StandardFields) caches.get(fileObject.getAbsolutePath());
            if (oldStandardFields != null) {
                standardFieldProcessor.removeStandardFields(oldStandardFields);
            }
            StandardFields standardFields = convertFromXml(stream, fileObject);
            standardFieldProcessor.addStandardFields(standardFields);
            caches.put(fileObject.getAbsolutePath(), standardFields);
            LOGGER.info( "加载stdfield文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
    }

    @Override
    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(STANDARDFIELD_EXTFILENAME) || fileObject.getFileName().endsWith(".stdfield");
    }

}
