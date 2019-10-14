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
package org.tinygroup.vfs.impl.sftp;

import org.tinygroup.vfs.FileObject;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public interface IFileObject extends FileObject {
    /**
     * 上传资源
     *
     * @param 可以是流 、文件、文件路径(上传位置不存在时会自动创建)
     * @throws 上传资源不存在或获取出错时会抛出运行时异常 ，上传位置为文件夹时也会抛出运行时异常
     */
    public void upload(InputStream in);

    public void upload(String sourceFile);

    public void upload(File sourceFile);

    /**
     * 下载资源
     *
     * @param 可以是输出到流 、文件、文件路径
     * @throws 输出资源不存在或获取出错时会抛出运行时异常 ，下载资源为文件夹时也会抛出运行时异常
     */
    public void download(OutputStream out);

    public void download(String dstFile);

    public void download(File dstFile);
}
