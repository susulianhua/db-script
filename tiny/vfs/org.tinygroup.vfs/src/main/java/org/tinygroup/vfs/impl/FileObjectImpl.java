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

import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.commons.tools.FileUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.SchemaProvider;
import org.tinygroup.vfs.VFS;
import org.tinygroup.vfs.VFSRuntimeException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FileObjectImpl extends AbstractFileObject {

    long lastModifiedTime = 0;
    private String path;
    private List<FileObject> children;
    private File file = null;
    private byte[] lockObject = new byte[0];

    public FileObjectImpl(SchemaProvider schemaProvider, String resource) {
        super(schemaProvider);

        init(new File(resource));
    }

    public FileObjectImpl(SchemaProvider schemaProvider, File file) {
        super(schemaProvider);
        init(file);
    }

    public boolean isModified() {
        return lastModifiedTime != file.lastModified();
    }

    @Override
    public void resetModified() {
        lastModifiedTime = file.lastModified();
    }

    private void init(File file) {
        this.file = file;
        if (file.exists()) {
            lastModifiedTime = file.lastModified();
        }
    }

    public String toString() {
        return file.getAbsolutePath();
    }

    public String getFileName() {
        return file.getName();
    }

    public String getPath() {
        // 如果没有计算过
        if (path == null) {
            // 如果有父亲
            if (getParent() != null) {
                path = getParent().getPath() + "/" + getFileName();
            } else {
                if (file.isDirectory()) {
                    return "";
                } else {
                    return "/" + file.getName();
                }
            }
        }
        return path;
    }

    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }

    public String getExtName() {
        int lastIndexOfDot = file.getName().lastIndexOf(".");
        if (lastIndexOfDot == -1) {
            // 如果不存在
            return null;
        }
        return file.getName().substring(lastIndexOfDot + 1);
    }

    public long getSize() {
        if (file.exists() && file.isFile()) {
            return file.length();
        }
        return 0;
    }

    public InputStream getInputStream() {
        try {
            if (file.exists() && file.isFile()) {
                return new BufferedInputStream(new FileInputStream(file));
            } else {
                return null;
            }
        } catch (FileNotFoundException e) {
            throw new VFSRuntimeException(file.getAbsolutePath()
                    + "获取FileInputStream出错，原因" + e);
        }
    }

    public boolean isFolder() {
        if (file.exists()) {
            return file.isDirectory();
        }
        return false;
    }

    public List<FileObject> getChildren() {
        if (isModified() || CollectionUtil.isEmpty(children)) {
            synchronized (lockObject) {
                children = forEachFile();
            }
        }
        return children;
    }

    private List<FileObject> forEachFile() {
        List<FileObject> children = new ArrayList<FileObject>();
        if (file.exists() && file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if (subFiles != null && subFiles.length > 0) {
                for (File subfile : subFiles) {
                    FileObject fileObject = null;
                    // TODO:也许需要判断"zip"
                    // 20150907先判断是否是目录，如果不是目录才会去读取，有可能是一个以.jar结尾的目录
                    if (subfile.getName().endsWith(".jar")
                            && !subfile.isDirectory()) {
                        fileObject = VFS.resolveFile(subfile.getAbsolutePath());
                    } else {
                        fileObject = new FileObjectImpl(getSchemaProvider(),
                                subfile);
                        fileObject.setParent(this);
                    }
                    children.add(fileObject);
                }
            }
        }
        return children;
    }

    public long getLastModifiedTime() {
        return file.lastModified();
    }

    public boolean isExist() {
        return file.exists();
    }

    public boolean isInPackage() {
        return false;
    }

    public FileObject getChild(String fileName) {
        if (getChildren() != null) {
            for (FileObject fileObject : getChildren()) {
                if (fileObject.getFileName().equals(fileName)) {
                    return fileObject;
                }
            }
        }
        return null;
    }

    public URL getURL() {
        try {
            return new URL(FileSchemaProvider.FILE_PROTOCOL + getAbsolutePath());
        } catch (MalformedURLException e) {
            throw new VFSRuntimeException(e);
        }
    }

    public OutputStream getOutputStream() {
        try {
            return new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new VFSRuntimeException(e);
        }
    }

    public void clean() {
        file = null;
    }

    public void delete() {
        FileUtil.delete(file);
    }
}
