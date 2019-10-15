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
package com.xquant.vfs.impl;


import com.xquant.common.StreamUtil;
import com.xquant.vfs.FileObject;
import com.xquant.vfs.SchemaProvider;
import com.xquant.vfs.VFSRuntimeException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 功能说明: zip协议的文件对象
 * <p/>
 * <p/>
 * 开发人员: renhui <br>
 * 开发时间: 2013-8-29 <br>
 * <br>
 */
public class ZipFileObject extends AbstractFileObject {

    private List<FileObject> children;
    private ZipFile zipFile = null;
    private File file = null;
    private ZipEntry zipEntry = null;
    private File cacheFile = null;

    public ZipFileObject(SchemaProvider schemaProvider, String resource) {
        super(schemaProvider);
        try {
            this.file = new File(resource);
            if (file.exists()) {
                this.zipFile = new ZipFile(resource);
            }
        } catch (IOException e) {
            throw new VFSRuntimeException(resource + "打开失败，错误：" + e.getMessage(), e);
        }

    }

    public ZipFileObject(ZipFileObject parent, ZipEntry entry) {
        super(parent.getSchemaProvider());
        this.zipFile = parent.zipFile;
        this.file = parent.file;
        this.zipEntry = entry;
    }

    public void clean() {
        try {
            zipFile.close();
        } catch (IOException e) {
            throw new VFSRuntimeException(e);
        }
    }

    public String getFileName() {
        if (zipEntry != null) {
            String[] names = zipEntry.getName().split("/");
            return names[names.length - 1];
        } else {
            return file.getName();
        }
    }

    public String getPath() {
        if (zipEntry != null) {
            return "/" + zipEntry.getName();
        }
        return "/";
    }

    public String getAbsolutePath() {
        String path = file.getAbsolutePath();
        if (File.separatorChar == '\\') {
            path = path.replaceAll("\\\\", "/");
        }
        if (zipEntry != null) {
            path = path + "!/" + zipEntry.getName();
        }
        return path;
    }

    public String getExtName() {
        String name;
        if (zipEntry != null) {
            if (!zipEntry.isDirectory()) {
                name = zipEntry.getName();
            } else {
                return null;
            }
        } else {
            name = file.getName();
        }
        int lastIndexOfDot = name.lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            // 如果不存在
            return null;
        }
        return name.substring(lastIndexOfDot + 1);
    }

    public long getSize() {
        if (zipEntry != null) {
            return zipEntry.getSize();
        }
        if (file.exists() && file.isFile()) {
            return file.length();
        }
        return 0;
    }

    public InputStream getInputStream() {
        try {
            if (zipEntry != null) {
                if (cacheFile == null || !cacheFile.exists()
                        || cacheFile.length() != this.getSize()) {
                    createCacheFile();
                    FileOutputStream out = new FileOutputStream(cacheFile);
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(
                            zipFile.getInputStream(zipEntry));
                    StreamUtil.io(bufferedInputStream, out, true, true);
                }
                return new BufferedInputStream(new FileInputStream(cacheFile));
            }
            if (file.exists() && file.isFile()) {
                return new ZipInputStream(new FileInputStream(file));
            } else {
                throw new VFSRuntimeException(file.getAbsolutePath()
                        + "不存在，或不是文件。");
            }
        } catch (Exception e) {
            throw new VFSRuntimeException(file.getAbsolutePath()
                    + "获取FileInputStream出错，原因" + e);
        }
    }

    private void createCacheFile() {
        String tempPath = System.getProperty("java.io.tmpdir");
        if (!tempPath.endsWith(File.separator)) {
            tempPath = tempPath + File.separator;
        }
        tempPath = tempPath + getExtName() + File.separator;
        File tempPathFile = new File(tempPath);
        if (!tempPathFile.exists()) {
            boolean created = tempPathFile.mkdirs();
            if (!created) {
                throw new VFSRuntimeException("创建临时目录" + tempPath + "失败！");
            }
        }
        cacheFile = new File(tempPath + getFileName() + "_"
                + getLastModifiedTime());
    }

    public String toString() {
        return getURL().toString();
    }

    public boolean isFolder() {
        if (zipEntry != null) {
            return zipEntry.isDirectory();
        }
        return file.exists();
    }


    @SuppressWarnings("unchecked")
    public List<FileObject> getChildren() {
        if (!file.exists()) {
            return null;
        }
        if (children == null) {
            children = new ArrayList<FileObject>();
            Enumeration<ZipEntry> e = (Enumeration<ZipEntry>) zipFile.entries();
            while (e.hasMoreElements()) {
                ZipEntry entry = e.nextElement();
                if (getParent() == null) {
                    String[] names = entry.getName().split("/");
                    // 如果当前是jar文件，如果
                    if (names.length == 1) {
                        addSubItem(entry);
                    }

                } else {
                    // 如果不是根目录
                    String parentName = zipEntry.getName();
                    if (!entry.getName().equals(zipEntry.getName()) && entry.getName().startsWith(parentName)) {
                        String fn = entry.getName().substring(parentName.length());
                        String[] names = fn.split("/");
                        if (names.length == 1) {
                            addSubItem(entry);
                        }
                    }
                }
            }
        }
        return children;
    }

    private void addSubItem(ZipEntry entry) {
        ZipFileObject zipFileObject = new ZipFileObject(this, entry);
        zipFileObject.setParent(this);
        children.add(zipFileObject);
    }

    public long getLastModifiedTime() {
        if (zipEntry != null) {
            return zipEntry.getTime();
        }
        return file.lastModified();
    }


    public boolean isExist() {
        if (zipEntry != null) {
            return true;
        }
        return file.exists();
    }

    public boolean isInPackage() {
        return zipFile != null;
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
            if (zipEntry != null) {
                return new URL(ZipSchemaProvider.ZIP_PROTOCAL + FileSchemaProvider.FILE_PROTOCOL + getAbsolutePath());
            } else {
                return new URL(FileSchemaProvider.FILE_PROTOCOL + getAbsolutePath());
            }
        } catch (MalformedURLException e) {
            throw new VFSRuntimeException(e);
        }
    }

    public OutputStream getOutputStream() {
        try {
            if (zipEntry != null) {
                if (cacheFile == null || !cacheFile.exists()
                        || cacheFile.length() != this.getSize()) {
                    createCacheFile();
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(
                            zipFile.getInputStream(zipEntry));
                    FileOutputStream out = new FileOutputStream(cacheFile);
                    StreamUtil.io(bufferedInputStream, out, true, true);
                }
                return new BufferedOutputStream(new FileOutputStream(cacheFile));
            }
            if (file.exists() && file.isFile()) {
                return new ZipOutputStream(new FileOutputStream(file));
            } else {
                throw new VFSRuntimeException(file.getAbsolutePath()
                        + "不存在，或不是文件。");
            }
        } catch (Exception e) {
            throw new VFSRuntimeException(file.getAbsolutePath()
                    + "获取outputStream出错，原因" + e);
        }
    }


}
