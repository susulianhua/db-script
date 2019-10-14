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
package org.tinygroup.fileresolver;

import org.tinygroup.vfs.FileObject;

import java.util.Map;

/**
 * 全路径静态资源文件搜索
 *
 * @author luoguo
 */
public interface FullContextFileRepository {

    String FILE_REPOSITORY_BEAN_NAME = "fullContextFileRepository";

    /**
     * 添加文件
     *
     * @param path       文件相对路径
     * @param fileObject 文件对象
     */
    void addFileObject(String path, FileObject fileObject);

    /**
     * 添加搜索路径
     *
     * @param searchPath 搜索路径
     */
    void addSearchPath(String searchPath);

    /**
     * 返回全路径搜索缓存的所有文件
     *
     * @return
     */
    Map<String, FileObject> getAllFileObjects();

    /**
     * 返回文件类型Map
     *
     * @param fileTypeMap Key为扩展名，value为content type
     */
    void setFileTypeMap(Map<String, String> fileTypeMap);

    /**
     * 获取文件对象
     *
     * @param path 文件相对路径
     * @return
     */
    FileObject getFileObject(String path);

    /**
     * 首先检查是否有匹配的语言，如果没有，则找默认的
     *
     * @param path 文件相对路径
     * @return
     */
    FileObject getFileObjectDetectLocale(String path);

    /**
     * 返回路径所在的跟路径
     *
     * @param path
     * @return
     */
    FileObject getRootFileObject(String path);

    /**
     * 返回文件的ContentType
     *
     * @param extName
     * @return
     */
    String getFileContentType(String extName);

    /**
     * 删除文件
     *
     * @param path
     */
    void removeFileObject(String path);
}
