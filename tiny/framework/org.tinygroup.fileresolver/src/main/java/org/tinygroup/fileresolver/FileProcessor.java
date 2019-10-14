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

import org.tinygroup.commons.order.Ordered;
import org.tinygroup.config.Configuration;
import org.tinygroup.vfs.FileObject;

/**
 * 文件处理器
 *
 * @author luoguo
 */
public interface FileProcessor extends Configuration, Ordered {
    /**
     * 该文件处理器是否可以处理该文件对象
     *
     * @return 可以处理返回true, 否则返回false
     */
    boolean isMatch(FileObject fileObject);

    /**
     * 是否支持刷新
     *
     * @return true支持刷新
     */
    boolean supportRefresh();

    /**
     * 设置文件搜索器
     *
     * @param fileResolver 文件搜索器
     */
    void setFileResolver(FileResolver fileResolver);

    /**
     * 为文件处理器新增文件对象
     *
     * @param fileObject 文件对象
     */
    void add(FileObject fileObject);

    /**
     * 文件内容没有变化的处理
     *
     * @param fileObject 文件对象
     */
    void noChange(FileObject fileObject);

    /**
     * 文件修改后的处理
     *
     * @param fileObject 文件对象
     */
    void modify(FileObject fileObject);

    /**
     * 文件删除时的处理
     *
     * @param fileObject 文件对象
     */
    void delete(FileObject fileObject);

    /**
     * 对文件进行处理
     */
    void process();

    /**
     * 处理完成后执行
     */
    void clean();
}
