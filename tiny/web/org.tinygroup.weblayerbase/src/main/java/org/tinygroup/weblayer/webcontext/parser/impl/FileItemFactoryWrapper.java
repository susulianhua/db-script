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
package org.tinygroup.weblayer.webcontext.parser.impl;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.weblayer.webcontext.parser.exception.UploadException;
import org.tinygroup.weblayer.webcontext.parser.fileupload.FileItemStorage;
import org.tinygroup.weblayer.webcontext.parser.fileupload.TinyFileItemFactory;
import org.tinygroup.weblayer.webcontext.parser.upload.UploadParameters;

/**
 * 功能说明: 复合的文件项创建工厂
 * <p>
 * 开发人员: renhui <br>
 * 开发时间: 2014-1-2 <br>
 * <br>
 */
public class FileItemFactoryWrapper implements FileItemFactory {

    private FileItemFactory itemFactory;

    public FileItemFactoryWrapper(UploadParameters parameters,
                                  UploadServiceImpl upload) {
        super();
        if (parameters.isDiskItemFactory()) {
            DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
            diskFileItemFactory.setRename(upload.getRename());
            diskFileItemFactory.setRepository(parameters.getRepository());
            diskFileItemFactory.setSizeThreshold((int) parameters
                    .getSizeThreshold().getValue());
            diskFileItemFactory.setKeepFormFieldInMemory(parameters
                    .isKeepFormFieldInMemory());
            diskFileItemFactory.setSaveInFile(parameters.isSaveInFile());
            diskFileItemFactory.setTemporary(parameters.isTemporary());
            itemFactory = diskFileItemFactory;
        } else {
            String storageBeanName = parameters.getItemStorageBeanName();
            TinyFileItemFactory tinyFileItemFactory = new TinyFileItemFactory();
            if (!StringUtil.isBlank(storageBeanName)) {
                FileItemStorage storage = BeanContainerFactory
                        .getBeanContainer(this.getClass().getClassLoader())
                        .getBean(storageBeanName);
                tinyFileItemFactory.setStorage(storage);
            }
            itemFactory = tinyFileItemFactory;
        }
    }

    public FileItem createItem(String fieldName, String contentType,
                               boolean isFormField, String fileName) {

        if (itemFactory != null) {
            return itemFactory.createItem(fieldName, contentType, isFormField,
                    fileName);
        }
        throw new UploadException("can not found fileItemFactory");
    }

    public FileItemFactory getFileItemFactory() {
        return itemFactory;
    }

}
