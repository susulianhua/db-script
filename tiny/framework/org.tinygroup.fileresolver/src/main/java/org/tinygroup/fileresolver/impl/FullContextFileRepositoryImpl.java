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
package org.tinygroup.fileresolver.impl;

import org.tinygroup.commons.i18n.LocaleUtil;
import org.tinygroup.fileresolver.FullContextFileRepository;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能说明: 全路径静态资源文件搜索的实现类，保存搜索的静态资源文件信息
 * <p>
 * <p>
 * 开发人员: renhui <br>
 * 开发时间: 2013-2-22 <br>
 * <br>
 */
public class FullContextFileRepositoryImpl implements FullContextFileRepository {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(FullContextFileRepositoryImpl.class);
    // key值为 类型:路径
    private Map<String, FileObject> fileMap = new HashMap<String, FileObject>();
    private Map<String, String> fileTypeMap;
    private Map<String, FileObject> searchPathMap = new HashMap<String, FileObject>();

    private ExcludeContextFileFinder excludeContextFileFinder;

    // String searchPath;

    public void setExcludeContextFileFinder(
            ExcludeContextFileFinder excludeContextFileFinder) {
        this.excludeContextFileFinder = excludeContextFileFinder;
    }

    public void addSearchPath(String searchPath) {
        FileObject fileObject = VFS.resolveFile(searchPath);
        searchPathMap.put(searchPath, fileObject);
        addFileObject(fileObject);
    }

    private void addFileObject(FileObject fileObject) {

        if (fileObject.isFolder()) {
            if (fileObject.getChildren() != null) {
                for (FileObject child : fileObject.getChildren()) {
                    addFileObject(child);
                }
            }
        } else {
            addFileObject(fileObject.getPath(), fileObject);
        }
    }

    public void addFileObject(String path, FileObject fileObject) {
        if (excludeContextFileFinder.checkMatch(fileObject)) {
            fileMap.put(path, fileObject);
        }
    }

    public FileObject getFileObject(String path) {
        FileObject fileObject = fileMap.get(path);
        if (fileObject == null && !searchPathMap.isEmpty()) {
            for (String searchPath : searchPathMap.keySet()) {
                FileObject searchPathObject = searchPathMap.get(searchPath);
                if (searchPathObject == null) {
                    throw new RuntimeException(String.format("[searchPath:%s],不是搜索的跟路径", searchPath));
                }
                fileObject = searchPathObject.getFileObject(path);
                if (fileObject != null && fileObject.isExist() && fileObject != searchPathObject) {
                    addFileObject(path, fileObject);
                    break;
                } else {
                    fileObject = null;
                }
            }
        }
        if (fileObject != null && excludeContextFileFinder.checkMatch(fileObject)) {
            return fileObject;
        }
        return null;
    }

    public void removeFileObject(String path) {
        fileMap.remove(path);
    }

    public void setFileTypeMap(Map<String, String> fileTypeMap) {
        this.fileTypeMap = fileTypeMap;

    }

    public String getFileContentType(String extName) {
        return fileTypeMap.get(extName);
    }

    public FileObject getRootFileObject(String path) {

        String fullPath = getFileObject(path).getAbsolutePath();
        return VFS.resolveFile(fullPath.substring(0,
                fullPath.length() - path.length() + 1));
    }

    public FileObject getFileObjectDetectLocale(String path) {
        StringBuilder sb = new StringBuilder();
        sb.append(path.substring(0, path.lastIndexOf('.')));
        sb.append(".");
        String locale = LocaleUtil.getContext().getLocale().toString();
        sb.append(locale);
        sb.append(path.substring(path.lastIndexOf('.')));
        FileObject fileObject = getFileObject(sb.toString());
        if (fileObject != null && fileObject.isExist()) {
            LOGGER.logMessage(LogLevel.DEBUG, "找到并使用[{}]的[{}]语言文件:{}", path, locale, fileObject.getPath());
            return fileObject;
        }
        return getFileObject(path);
    }

    @Override
    public Map<String, FileObject> getAllFileObjects() {
        return fileMap;
    }

}
