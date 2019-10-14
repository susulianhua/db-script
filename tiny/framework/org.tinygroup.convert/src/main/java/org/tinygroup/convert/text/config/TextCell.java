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
package org.tinygroup.convert.text.config;

import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.io.UnsupportedEncodingException;

public class TextCell {
    private static final Logger LOGGER = LoggerFactory.getLogger(TextCell.class);
    private String value;
    private int length;

    public TextCell(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String toString(boolean checkLength) {
        if (checkLength) {
            return adjustValueLength(length, value);
        }
        return value;
    }

    private String adjustValueLength(int propertyMaxLength, String data) {
        String newData = data;
        int adjustLength = propertyMaxLength - getLength(data);
        if (adjustLength > 0) {
            newData = String.format("%s%" + adjustLength + "s", newData, " ");
        }
        return newData;
    }

    private int getLength(String s) {
        try {
            return s.getBytes("GBK").length;
        } catch (UnsupportedEncodingException e) {
            LOGGER.errorMessage("获取字符串{0}的gbk编码长度时出现异常", e, s);
        }
        return 0;
    }
}
