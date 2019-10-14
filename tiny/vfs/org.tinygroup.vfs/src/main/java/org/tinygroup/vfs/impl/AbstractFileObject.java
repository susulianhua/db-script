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
package org.tinygroup.vfs.impl;

import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.FileObjectFilter;
import org.tinygroup.vfs.FileObjectProcessor;
import org.tinygroup.vfs.SchemaProvider;

import java.util.List;

public abstract class AbstractFileObject implements FileObject {

    private SchemaProvider schemaProvider;
    private FileObject parent;

    public AbstractFileObject(SchemaProvider schemaProvider) {
        this.schemaProvider = schemaProvider;
    }

    public FileObject getParent() {
        return parent;
    }

    public void setParent(FileObject parent) {
        this.parent = parent;
    }

    public SchemaProvider getSchemaProvider() {
        return schemaProvider;
    }

    public int hashCode() {
        return this.getAbsolutePath().hashCode();
    }

    public boolean isModified() {
        return false;
    }

    public void resetModified() {

    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof FileObject) {
            FileObject fileObject = (FileObject) obj;
            return this.getAbsolutePath().equalsIgnoreCase(fileObject.getAbsolutePath());
        }
        return false;
    }

    /**
     * 对文件对象及其所有子对象都通过文件对象过滤器进行过滤，如果匹配，则执行文件对外处理器
     *
     * @param fileObjectFilter
     * @param fileObjectProcessor
     * @param parentFirst         true:如果父亲和儿子都命中，则先处理父亲；false:如果父亲和儿子都命中，则先处理儿子
     */
    public void foreach(FileObjectFilter fileObjectFilter, FileObjectProcessor fileObjectProcessor,
                        boolean parentFirst) {
        //先处理父对象，后处理子对象
        if (parentFirst && fileObjectFilter.accept(this)) {
            fileObjectProcessor.process(this);
        }
        //如果是目录，递归调用子对象的查询
        if (isFolder()) {
            //遍历当前文件对象的子文件列表
            for (FileObject subFileObject : getChildren()) {
                subFileObject.foreach(fileObjectFilter, fileObjectProcessor, parentFirst);
            }
        }
        //先处理子对象，后处理父对象
        if (!parentFirst && fileObjectFilter.accept(this)) {
            fileObjectProcessor.process(this);
        }
    }

    /**
     * 对文件对象及其所有子对象都通过文件对象过滤器进行过滤，如果匹配，则执行文件对外处理器，父文件先处理
     *
     * @param fileObjectFilter
     * @param fileObjectProcessor
     */
    public void foreach(FileObjectFilter fileObjectFilter, FileObjectProcessor fileObjectProcessor) {
        foreach(fileObjectFilter, fileObjectProcessor, true);
    }

    public void clean() {

    }

    public void delete() {
        throw new RuntimeException("本FileObject实现不支持delete操作!");
    }

    public FileObject getFileObject(String path) {
        //此处不用File.separator
        //@luoguo path必须是linux规范
        if (path.equals("/")) {
            return this;
        }
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        String[] pathLayers = path.split("/");
        if (isFolder()) {
            List<FileObject> fileObjects = getChildren();
            for (FileObject subFileObject : fileObjects) {
                if (subFileObject.getFileName().equals(pathLayers[0])) {
                    if (pathLayers.length > 1) {
                        return subFileObject.getFileObject(substringAfter(path, "/"));
                    } else {
                        return subFileObject;
                    }
                }
            }

        }

        return null;
    }

    public String substringAfter(String str, String separator) {
        if (str == null || str.length() == 0) {
            return str;
        }

        int pos = str.indexOf(separator);

        if (pos >= 0) {
            return str.substring(pos + separator.length());
        }
        return "";
    }

}
