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
package com.xquant.vfs.impl;


import com.xquant.vfs.SchemaProvider;
import com.xquant.vfs.VFSRuntimeException;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;

/**
 * 功能说明: https协议对象
 * <p>
 * 开发人员: renhui <br>
 * 开发时间: 2013-8-30 <br>
 * <br>
 */
public class HttpsFileObject extends URLFileObject {

    public HttpsFileObject(SchemaProvider schemaProvider, String resource) {
        super(schemaProvider, resource);
    }

    public long getSize() {
        try {
            HttpsURLConnection con = (HttpsURLConnection) getURL().openConnection();
            return con.getContentLength();
        } catch (IOException e) {
            throw new VFSRuntimeException(e);
        }
    }

    public long getLastModifiedTime() {
        try {
            HttpsURLConnection con = (HttpsURLConnection) getURL().openConnection();
            return con.getLastModified();
        } catch (IOException e) {
            throw new VFSRuntimeException(e);
        }

    }

    /**
     * http资源，都是文件，不会有路径存在
     */
    public boolean isFolder() {
        return false;
    }
}
