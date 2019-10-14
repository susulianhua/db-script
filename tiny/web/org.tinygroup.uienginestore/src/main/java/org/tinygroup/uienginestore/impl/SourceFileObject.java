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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * UI资源的来源对象
 * @author yancheng11334
 *
 */
public class SourceFileObject extends UIResourceFileObjectWrapper {
    /**
     *
     */
    private static final long serialVersionUID = 4612775362386786525L;

    private ByteArrayInputStream inputStream;

    public SourceFileObject(String path, byte[] source) {
        this.path = path.startsWith("/") ? path : "/" + path;
        this.inputStream = new ByteArrayInputStream(source);
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public long getSize() {
        return inputStream.available();
    }

    public OutputStream getOutputStream() {
        return null;
    }

    public void clean() {
        if (inputStream != null) {
            try {
                inputStream.close();
                inputStream = null;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
