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
package com.xquant.file;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.List;

/**
 * @author luoguo
 *         <p>
 *         文件路径必须是Linux规范
 */
public interface FileObject extends Serializable {

    // 返回绝对路径
    String getAbsolutePath();

    // 返回路径
    String getPath();

    // 返回文件名
    String getFileName();

    // 返回扩展名
    String getExtName();

    // 返回是否是目录，如果是目录，则getInputStream无效。
    boolean isFolder();

    // 是否存在
    boolean isExist();

    // 返回修改时间
    long getLastModifiedTime();

    // 返回文件大小
    long getSize();

    // 返回输入流
    InputStream getInputStream();

    // 返回输出流
    OutputStream getOutputStream();

    // 返回上级文件
    FileObject getParent();

    // 设置上级文件
    void setParent(FileObject fileObject);

    // 返回下级文件列表
    List<FileObject> getChildren();

    // 获取参数名称指定的fileobject
    FileObject getChild(String fileName);

    void foreach(FileObjectFilter fileObjectFilter, FileObjectProcessor fileObjectProcessor, boolean parentFirst);

    void foreach(FileObjectFilter fileObjectFilter, FileObjectProcessor fileObjectProcessor);

    /**
     * 对FileObject对象执行清理操作，清理完成后，可能会导致此对象不再可用
     */
    void clean();

    /**
     * 删除目录或文件
     */
    void delete();

    /**
     * 根据路径查找子对象
     *
     * @param path
     * @return
     */
    FileObject getFileObject(String path);

}
