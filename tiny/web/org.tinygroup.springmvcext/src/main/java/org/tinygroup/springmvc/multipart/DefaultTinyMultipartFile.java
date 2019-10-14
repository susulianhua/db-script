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
package org.tinygroup.springmvc.multipart;

import org.apache.commons.fileupload.FileItem;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.weblayer.webcontext.parser.fileupload.TinyFileItem;
import org.tinygroup.weblayer.webcontext.parser.impl.DiskFileItem;
import org.tinygroup.weblayer.webcontext.parser.impl.FileObjectInDisk;
import org.tinygroup.weblayer.webcontext.parser.impl.FileObjectInMemory;
import org.tinygroup.weblayer.webcontext.parser.impl.InMemoryFormFieldItem;

/**
 * TinyMultipartFile接口默认实现
 *
 * @author renhui
 */
public class DefaultTinyMultipartFile extends CommonsMultipartFile implements
        TinyMultipartFile {


    public DefaultTinyMultipartFile(FileItem fileItem) {
        super(fileItem);
    }

    public FileObject toFileObject() {
        FileObject fileObject = null;
        FileItem item = getFileItem();
        if (item instanceof InMemoryFormFieldItem) {
            fileObject = new FileObjectInMemory((InMemoryFormFieldItem) item);
        } else if (item instanceof DiskFileItem) {
            fileObject = new FileObjectInDisk((DiskFileItem) item);
        } else if (item instanceof TinyFileItem) {
            fileObject = ((TinyFileItem) item).getFileObject();
        }
        return fileObject;
    }

}
