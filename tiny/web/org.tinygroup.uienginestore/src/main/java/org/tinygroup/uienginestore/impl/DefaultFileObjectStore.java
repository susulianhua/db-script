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

import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.vfs.FileObject;

/**
 * 默认的资源存储接口实现
 * @author yancheng11334
 *
 */
public class DefaultFileObjectStore extends AbstractFileObjectStore {


    public void store(FileObject in, FileObject out, boolean closeIn,
                      boolean closeOut) throws Exception {
        checkFileObject(in, out);
        LOGGER.logMessage(LogLevel.INFO, "资源从[{0}]写入到[{1}]开始...", in.getAbsolutePath(), out.getAbsolutePath());
        try {
            StreamUtil.io(in.getInputStream(), out.getOutputStream(), closeIn, closeOut);
        } catch (Exception e) {
            LOGGER.errorMessage("资源从[{0}]写入到[{1}]发生异常:", e, in.getAbsolutePath(), out.getAbsolutePath());
        }
        LOGGER.logMessage(LogLevel.INFO, "资源从[{0}]写入到[{1}]结束.", in.getAbsolutePath(), out.getAbsolutePath());
    }

}
