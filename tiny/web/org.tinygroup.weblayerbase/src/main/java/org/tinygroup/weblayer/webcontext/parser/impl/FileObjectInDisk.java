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
import org.tinygroup.commons.tools.FileUtil;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.impl.AbstractFileObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class FileObjectInDisk extends AbstractFileObject implements ItemFileObject {

    private static final String FILE_PROTOCAL = "file:";
    private DiskFileItem fileItem;

    public FileObjectInDisk(DiskFileItem fileItem) {
        super(null);
        this.fileItem = fileItem;
    }

    public String getFileName() {
        File file = fileItem.getStoreLocation();
        if (file != null) {
            return file.getName();
        }
        return null;
    }

    public String getPath() {
        String repository = fileItem.getRepository().getAbsolutePath();
        String filePath = getAbsolutePath();
        if (filePath != null && filePath.startsWith(repository)) {
            return resolverFilePath(filePath, repository);
        }
        return "/";
    }

    private String resolverFilePath(String filePath, String separator) {
        String path = StringUtil.substringAfterLast(filePath, separator);
        return StringUtil.replace(path, "\\", "/");
    }

    public String getAbsolutePath() {
        File file = fileItem.getStoreLocation();
        if (file != null) {
            return file.getAbsolutePath();
        }
        return null;
    }

    public String getExtName() {
        File file = fileItem.getStoreLocation();
        if (file != null) {
            return FileUtil.getExtension(file.getName());
        }
        return null;
    }

    public boolean isExist() {
        File file = fileItem.getStoreLocation();
        if (file != null) {
            return file.exists();
        }
        return false;
    }

    public long getSize() {
        return fileItem.getSize();
    }

    public InputStream getInputStream() {
        try {
            return fileItem.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException("获取上传文件输入流出现异常", e);
        }
    }

    public boolean isFolder() {
        File file = fileItem.getStoreLocation();
        if (file != null) {
            return file.isDirectory();
        }
        return false;
    }


    public List<FileObject> getChildren() {
        return null;
    }

    public FileObject getChild(String fileName) {
        return null;
    }

    public long getLastModifiedTime() {
        return 0;
    }


    public boolean isInPackage() {
        return false;
    }

    public URL getURL() {
        try {
            return new URL(FILE_PROTOCAL + getAbsolutePath());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public OutputStream getOutputStream() {
        try {
            return fileItem.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException("获取上传文件输出流出现异常", e);
        }
    }

    public FileItem getFileItem() {
        return fileItem;
    }

}
