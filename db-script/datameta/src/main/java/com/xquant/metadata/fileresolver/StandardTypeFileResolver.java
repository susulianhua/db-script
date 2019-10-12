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
import com.xquant.metadata.config.stddatatype.StandardTypes;
import com.xquant.metadata.stddatatype.StandardTypeProcessor;
import com.xquant.metadata.util.MetadataUtil;
import com.xquant.xml.XStreamFactory;

public class StandardTypeFileResolver extends AbstractFileProcessor {

    private static final String DATATYPE_EXTFILENAME = ".datatype.xml";
    private StandardTypeProcessor standardDataTypeProcessor;

    public StandardTypeProcessor getStandardDataTypeProcessor() {
        return standardDataTypeProcessor;
    }

    public void setStandardDataTypeProcessor(
            StandardTypeProcessor standardDataTypeProcessor) {
        this.standardDataTypeProcessor = standardDataTypeProcessor;
    }

    public void process() {
        XStream stream = XStreamFactory
                .getXStream(MetadataUtil.METADATA_XSTREAM);
        for (FileObject fileObject : deleteList) {
            LOGGER.info( "正在移除datatype文件[{0}]",
                    fileObject.getAbsolutePath());
            StandardTypes standardTypes = (StandardTypes) caches.get(fileObject.getAbsolutePath());
            if (standardTypes != null) {
                standardDataTypeProcessor.removeStandardTypes(standardTypes);
                caches.remove(fileObject.getAbsolutePath());
            }
            LOGGER.info( "移除datatype文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
        for (FileObject fileObject : changeList) {
            LOGGER.info( "正在加载datatype文件[{0}]",
                    fileObject.getAbsolutePath());
            StandardTypes oldStandardTypes = (StandardTypes) caches.get(fileObject.getAbsolutePath());
            if (oldStandardTypes != null) {
                standardDataTypeProcessor.removeStandardTypes(oldStandardTypes);
            }
            StandardTypes standardTypes = convertFromXml(stream, fileObject);
            standardDataTypeProcessor.addStandardTypes(standardTypes);
            caches.put(fileObject.getAbsolutePath(), standardTypes);
            LOGGER.info( "加载datatype文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
    }

    @Override
    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(DATATYPE_EXTFILENAME) || fileObject.getFileName().endsWith(".datatype");
    }

}
