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
import com.xquant.vfs.FileObject;
import com.xquant.fileresolver.impl.AbstractFileProcessor;
import com.xquant.metadata.config.constants.Constants;
import com.xquant.metadata.constants.ConstantProcessor;
import com.xquant.metadata.util.MetadataUtil;
import com.xquant.xml.XStreamFactory;

public class ConstantFileResolver extends AbstractFileProcessor {

    private static final String CONSTANT_EXTFILENAME = ".const.xml";
    private ConstantProcessor constantProcessor;


    public ConstantProcessor getConstantProcessor() {
        return constantProcessor;
    }

    public void setConstantProcessor(ConstantProcessor constantProcessor) {
        this.constantProcessor = constantProcessor;
    }

    public void process() {
        XStream stream = XStreamFactory
                .getXStream(MetadataUtil.METADATA_XSTREAM);
        for (FileObject fileObject : deleteList) {
            LOGGER.info( "正在移除const文件[{0}]",
                    fileObject.getAbsolutePath());
            Constants constants = (Constants) caches.get(fileObject
                    .getAbsolutePath());
            if (constants != null) {
                constantProcessor.removeConstants(constants);
                caches.remove(fileObject.getAbsolutePath());
            }
            LOGGER.info( "移除const文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
        for (FileObject fileObject : changeList) {
            LOGGER.info( "正在加载const文件[{0}]",
                    fileObject.getAbsolutePath());
            Constants oldConstants = (Constants) caches.get(fileObject
                    .getAbsolutePath());
            if (oldConstants != null) {
                constantProcessor.removeConstants(oldConstants);
            }
            Constants constants = convertFromXml(stream, fileObject);
            constantProcessor.addConstants(constants);
            caches.put(fileObject.getAbsolutePath(), constants);
            LOGGER.info( "加载const文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
    }

    @Override
    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(CONSTANT_EXTFILENAME) || fileObject.getFileName().endsWith(".const");
    }

}
