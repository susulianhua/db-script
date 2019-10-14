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
package org.tinygroup.uiengineweblayer;

import java.io.IOException;

public class UiCssInfo {

    private String path;

    private byte[] bytes;

    public UiCssInfo(String path, String content) throws IOException {
        this(path, content, "UTF-8");
    }

    public UiCssInfo(String path, String content, String encode) throws IOException {
        this(path, content.getBytes(encode));
    }

    public UiCssInfo(String path, byte[] bytes) {
        super();
        this.path = path;
        this.bytes = bytes;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public String getContent() throws IOException {
        return getContent("UTF-8");
    }


    public String getContent(String encode) throws IOException {
        return new String(bytes, encode);
    }

}
