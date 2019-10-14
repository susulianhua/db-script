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
package org.tinygroup.weblayer.webcontext.parser.upload;

import org.tinygroup.commons.tools.HumanReadableSize;

import java.io.File;


/**
 * 定义<code>UploadService</code>的参数。
 *
 * @author renhui
 */
public interface UploadConfiguration {
    /**
     * 默认值：HTTP请求的最大尺寸，超过此尺寸的请求将被抛弃。
     */
    long SIZE_MAX_DEFAULT = -1;

    /**
     * 默认值：单个文件允许的最大尺寸，超过此尺寸的请求将被抛弃。
     */
    long FILE_SIZE_MAX_DEFAULT = -1;

    /**
     * 默认值：将文件放在内存中的阈值，小于此值的文件被保存在内存中。
     */
    int SIZE_THRESHOLD_DEFAULT = 10240;

    /**
     * 取得暂存文件的目录。
     */
    File getRepository();

    /**
     * 取得HTTP请求的最大尺寸，超过此尺寸的请求将被抛弃。单位：字节，值<code>-1</code>表示没有限制。
     */
    HumanReadableSize getSizeMax();

    /**
     * 取得单个文件允许的最大尺寸，超过此尺寸的文件将被抛弃。单位：字节，值<code>-1</code>表示没有限制。
     */
    HumanReadableSize getFileSizeMax();

    /**
     * 取得将文件放在内存中的阈值，小于此值的文件被保存在内存中。单位：字节。
     */
    HumanReadableSize getSizeThreshold();

    /**
     * 是否将普通的form field保持在内存里？当<code>sizeThreshold</code>值为<code>0</code>
     * 的时候，该值自动为<code>true</code>。
     */
    boolean isKeepFormFieldInMemory();

    boolean isSaveInFile();

    /**
     * 标准的上传文件请求中，包含这样的内容：
     * <code>Content-Disposition: attachment; filename=xxx.txt</code>
     * 。然而有些不规范的应用，会取<code>fname=xxx.txt</code>。此变量为兼容这种情况而设。
     */
    String[] getFileNameKey();

    /**
     * 是否是临时目录，true：认为是临时文件，请求完毕会删除此临时文件。
     *
     * @return
     */
    boolean isTemporary();

    //是否是文件介质存储,默认是
    boolean isDiskItemFactory();

    //如果是其他存储介质，那么给定存储介质的beanname
    String getItemStorageBeanName();
}
