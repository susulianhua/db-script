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
package org.tinygroup.convert.base64bytearray;

import com.thoughtworks.xstream.core.util.Base64Encoder;
import org.tinygroup.convert.Converter;

public class Base64ToByteArray implements Converter<String, byte[]> {
    private Base64Encoder base64Encoder = new Base64Encoder();

    public byte[] convert(String inputData) {
        return base64Encoder.decode(inputData.trim());
    }
}
