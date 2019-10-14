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
package org.tinygroup.commons.file;

import org.tinygroup.commons.io.ByteArrayOutputStream;

import java.io.InputStream;
import java.io.OutputStream;

public class IOUtils {
    public static String readFromInputStream(InputStream inputStream,
                                             String encode) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int n = 0;
        while ((n = inputStream.read(buf)) > 0) {
            outputStream.write(buf, 0, n);
        }
        return new String(outputStream.toByteArray().toByteArray(), encode);
    }

    public static void writeToOutputStream(OutputStream outputStream,
                                           String content, String encode) throws Exception {
        outputStream.write(content.getBytes(encode));
    }

}
