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
package org.tinygroup.weixinhttp;

import org.tinygroup.vfs.FileObject;

/**
 * 微信HTTP上传通用构造接口
 *
 * @author yancheng11334
 */
public interface WeiXinHttpUpload {

    /**
     * 获取微信通讯的URL键值
     *
     * @return
     */
    String getWeiXinKey();

    /**
     * 获取构造文件名(不一定与上传文件名一致)
     *
     * @return
     */
    String getFileName();

    /**
     * 获得文件实体
     *
     * @return
     */
    FileObject getFileObject();

    /**
     * 获得表单名
     *
     * @return
     */
    String getFormName();

    /**
     * 获得表单内容
     *
     * @return
     */
    String getContent();
}
