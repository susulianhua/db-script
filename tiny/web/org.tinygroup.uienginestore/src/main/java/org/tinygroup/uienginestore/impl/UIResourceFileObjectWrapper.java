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
package org.tinygroup.uienginestore.impl;

import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.FileObjectFilter;
import org.tinygroup.vfs.FileObjectProcessor;
import org.tinygroup.vfs.SchemaProvider;

import java.net.URL;
import java.util.List;

/**
 * 简化UI资源接口包装
 * @author yancheng11334
 *
 */
public abstract class UIResourceFileObjectWrapper implements FileObject {

    /**
     *
     */
    private static final long serialVersionUID = 5130380979173748469L;

    protected String path;

    public SchemaProvider getSchemaProvider() {
        return null;
    }

    public boolean isModified() {
        return false;
    }

    public void resetModified() {

    }

    public URL getURL() {
        return null;
    }

    public String getAbsolutePath() {
        return getPath();
    }

    public String getPath() {
        return path;
    }

    public String getFileName() {
        int n = path.lastIndexOf("/");
        return n >= 0 ? path.substring(n + 1, path.length()) : path;
    }

    public String getExtName() {
        return null;
    }

    public boolean isFolder() {
        return false;
    }

    public boolean isInPackage() {
        return false;
    }

    public boolean isExist() {
        return true;
    }

    public long getLastModifiedTime() {
        return 0;
    }

    public long getSize() {
        return 0;
    }

    public FileObject getParent() {
        return null;
    }

    public void setParent(FileObject fileObject) {

    }

    public List<FileObject> getChildren() {
        return null;
    }

    public FileObject getChild(String fileName) {
        return null;
    }

    public void foreach(FileObjectFilter fileObjectFilter,
                        FileObjectProcessor fileObjectProcessor, boolean parentFirst) {

    }

    public void foreach(FileObjectFilter fileObjectFilter,
                        FileObjectProcessor fileObjectProcessor) {

    }

    public void delete() {

    }

    public FileObject getFileObject(String path) {
        return null;
    }

}
