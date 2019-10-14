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
package org.tinygroup.httpclient31.body;

import org.apache.commons.httpclient.methods.multipart.PartSource;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamPartSource implements PartSource {

    private InputStream input;

    private String fileName;

    public InputStreamPartSource(String fileName, InputStream input) {
        this.fileName = fileName;
        this.input = input;
    }

    public long getLength() {
        try {
            return input.available();
        } catch (IOException e) {
            //忽略异常
            return 0;
        }
    }

    public String getFileName() {
        return fileName;
    }

    public InputStream createInputStream() throws IOException {
        return input;
    }

}
