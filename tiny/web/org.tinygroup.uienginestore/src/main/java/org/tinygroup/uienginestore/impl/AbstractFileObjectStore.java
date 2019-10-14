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
package org.tinygroup.uienginestore.impl;

import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.uienginestore.FileObjectStore;
import org.tinygroup.vfs.FileObject;

public abstract class AbstractFileObjectStore implements FileObjectStore {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractFileObjectStore.class);

    /**
     * 自动关闭输入流和输出流的存储实现
     */
    public void store(FileObject in, FileObject out) throws Exception {
        store(in, out, true, true);
    }

    /**
     * 数据检查
     * @throws Exception
     */
    protected void checkFileObject(FileObject in, FileObject out) throws Exception {
        if (in == null || in.getInputStream() == null) {
            throw new NullPointerException("资源的输入流不能为空.");
        }
        if (out == null || out.getOutputStream() == null) {
            throw new NullPointerException("资源的输出流不能为空.");
        }
    }

}
