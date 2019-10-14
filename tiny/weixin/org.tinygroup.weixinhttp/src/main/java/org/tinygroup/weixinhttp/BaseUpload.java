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
package org.tinygroup.weixinhttp;

import org.tinygroup.vfs.FileObject;

public abstract class BaseUpload implements WeiXinHttpUpload {

    private String fileName;

    private FileObject fileObject;

    private String formName;

    private String content;

    public BaseUpload(FileObject fileObject) {
        this(null, fileObject, null, null);
    }

    public BaseUpload(String fileName, FileObject fileObject) {
        this(fileName, fileObject, null, null);
    }

    public BaseUpload(String fileName, FileObject fileObject, String formName, String content) {
        this.fileName = fileName;
        this.fileObject = fileObject;
        this.formName = formName;
        this.content = content;
    }

    public FileObject getFileObject() {
        return fileObject;
    }

    public String getFormName() {
        return formName;
    }

    public String getContent() {
        return content;
    }

    public String getFileName() {
        return fileName != null ? fileName : fileObject.getFileName();
    }

}
