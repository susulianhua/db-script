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
package org.tinygroup.templatespringext;

import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.template.loader.FileResourceManager;
import org.tinygroup.vfs.FileObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangll13383 on 2015/9/11.
 */
public abstract class AbstractFileProcessor implements FileProcessor {
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractFileProcessor.class);
    protected List<FileObject> fileList = new ArrayList<FileObject>();
    protected Map<String, FileObject> caches = new HashMap<String, FileObject>();

    private FileResourceManager fileResourceManager;

    public FileResourceManager getFileResourceManager() {
        return fileResourceManager;
    }

    public void setFileResourceManager(FileResourceManager fileResourceManager) {
        this.fileResourceManager = fileResourceManager;
    }

    public void addFile(FileObject fileObject) {
        if (caches.containsKey(fileObject.getAbsolutePath())) {
            if (caches.get(fileObject.getAbsolutePath()).isModified()) {
                caches.remove(fileObject.getAbsolutePath());
                caches.put(fileObject.getAbsolutePath(), fileObject);
                return;
            }
        }
        fileList.add(fileObject);
        caches.put(fileObject.getAbsolutePath(), fileObject);
    }

    public void removeFile(FileObject fileObject) {

    }

    public List<FileObject> getFileObjectList() {
        return fileList;
    }
}
