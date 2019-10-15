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
import com.xquant.fileresolver.impl.AbstractFileProcessor;
import com.xquant.metadata.bizdatatype.BusinessTypeProcessor;
import com.xquant.metadata.config.bizdatatype.BusinessTypes;
import com.xquant.metadata.util.MetadataUtil;
import com.xquant.vfs.FileObject;
import com.xquant.xml.XStreamFactory;



public class BusinessTypeFileResolver extends AbstractFileProcessor {
    private static final String BIZDATATYPE_EXTFILENAME = ".bizdatatype.xml";
    private BusinessTypeProcessor businessTypeProcessor;


    public BusinessTypeProcessor getBusinessTypeProcessor() {
        return businessTypeProcessor;
    }

    public void setBusinessTypeProcessor(BusinessTypeProcessor businessTypeProcessor) {
        this.businessTypeProcessor = businessTypeProcessor;
    }

    public void process() {
//		BusinessTypeProcessor businessTypeProcessor = SpringBeanContainer
//				.getBean(MetadataUtil.BUSINESSTYPEPROCESSOR_BEAN);
        XStream stream = XStreamFactory
                .getXStream(MetadataUtil.METADATA_XSTREAM);
        for (FileObject fileObject : deleteList) {
            LOGGER.info( "正在移除bizdatatype文件[{0}]",
                    fileObject.getAbsolutePath());
            BusinessTypes businessTypes = (BusinessTypes) caches.get(fileObject
                    .getAbsolutePath());
            if (businessTypes != null) {
                businessTypeProcessor.removeBusinessTypes(businessTypes);
                caches.remove(fileObject.getAbsolutePath());
            }
            LOGGER.info( "移除bizdatatype文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
        for (FileObject fileObject : changeList) {
            LOGGER.info( "正在加载bizdatatype文件[{0}]",
                    fileObject.getAbsolutePath());
            BusinessTypes oldBusinessTypes = (BusinessTypes) caches.get(fileObject.getAbsolutePath());
            if (oldBusinessTypes != null) {
                businessTypeProcessor.removeBusinessTypes(oldBusinessTypes);
            }
            BusinessTypes businessTypes = convertFromXml(stream, fileObject);
            businessTypeProcessor.addBusinessTypes(businessTypes);
            caches.put(fileObject.getAbsolutePath(), businessTypes);
            LOGGER.info( "加载bizdatatype文件[{0}]结束",
                    fileObject.getAbsolutePath());
        }
    }

    @Override
    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(BIZDATATYPE_EXTFILENAME) || fileObject.getFileName().endsWith(".bizdatatype");
    }

}
