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

import org.tinygroup.weblayer.webcontext.parser.upload.FileUploadReName;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

public class DiskFileItem extends AbstractFileItem {
    private static final long serialVersionUID = 4225039123863446602L;
    private boolean temporary;

    public DiskFileItem(String fieldName, String contentType, boolean isFormField, boolean saveInFile, boolean temporary, String fileName, int sizeThreshold,
                        boolean keepFormFieldInMemory, File repository, HttpServletRequest request, FileUploadReName rename) {
        super(fieldName, contentType, isFormField, saveInFile, fileName, sizeThreshold, keepFormFieldInMemory, repository, request, rename);
        this.temporary = temporary;
    }

    /**
     * Removes the file contents from the temporary storage.
     */

    protected void finalize() throws Throwable {
        if (temporary) {
            try {
                File outputFile = dfos.getFile();
                if (outputFile != null && outputFile.exists()) {
                    outputFile.delete();
                }
            } finally {
                super.finalize();
            }
        }
    }

    public boolean isTemporary() {
        return temporary;
    }

    public void setTemporary(boolean temporary) {
        this.temporary = temporary;
    }

}
