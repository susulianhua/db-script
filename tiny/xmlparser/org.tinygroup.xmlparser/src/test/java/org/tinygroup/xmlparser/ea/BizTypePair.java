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
package org.tinygroup.xmlparser.ea;

import org.apache.commons.lang.StringUtils;

public class BizTypePair {

    private String type;

    private String length;

    public BizTypePair(String type, String length) {
        super();
        this.type = type;
        this.length = length;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return type + length;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BizTypePair) {
            if (StringUtils.equals(((BizTypePair) obj).getType(), getType()) && StringUtils.equals(((BizTypePair) obj).getLength(), getLength())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

}
