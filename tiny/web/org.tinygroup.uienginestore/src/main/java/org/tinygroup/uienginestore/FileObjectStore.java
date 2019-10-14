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
package org.tinygroup.uienginestore;

import org.tinygroup.vfs.FileObject;

/**
 * 资源存储接口
 * @author yancheng11334
 *
 */
public interface FileObjectStore {

    /**
     * 资源从输入流写入输出流
     * @param in
     * @param out
     * @param closeIn
     * @param closeOut
     * @throws Exception
     */
    void store(FileObject in, FileObject out, boolean closeIn, boolean closeOut) throws Exception;

    /**
     * 资源从输入流写入输出流
     * @param in
     * @param out
     * @throws Exception
     */
    void store(FileObject in, FileObject out) throws Exception;
}
