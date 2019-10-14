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

import org.tinygroup.commons.tools.UUID;
import org.tinygroup.weblayer.webcontext.parser.upload.FileUploadReName;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

public class FileUploadRenameImpl implements FileUploadReName {

    private File repository;

    private UUID uuid = new UUID();

    public FileUploadRenameImpl() {
        super();
    }

    public FileUploadRenameImpl(File repository) {
        super();
        this.repository = repository;

    }

    public File getRepository() {
        return repository;
    }


    public void setRepository(File repository) {
        this.repository = repository;
    }


    public String reName(String localFileName, HttpServletRequest request) {
        String UID = uuid.nextID().replace(':', '_').replace('-', '_');
        return String.format("%s/%s", repository.getPath(), "upload_" + UID + ".tmp");
    }

}
