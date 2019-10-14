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
package org.tinygroup.convert.common;

import org.tinygroup.convert.ConvertException;
import org.tinygroup.convert.Converter;

import java.io.UnsupportedEncodingException;

public class ByteArrayToString implements Converter<byte[], String> {
    private String charset = null;

    public ByteArrayToString(String charset) {
        this.charset = charset;
    }

    public ByteArrayToString() {

    }

    public String convert(byte[] inputData) throws ConvertException {
        if (charset == null) {
            return new String(inputData);
        } else {
            try {
                return new String(inputData, charset);
            } catch (UnsupportedEncodingException e) {
                throw new ConvertException(e);
            }
        }
    }

}
