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
package org.tinygroup.httpclient451.wrapper;

import org.tinygroup.httpvisitor.Header;

/**
 * 对Header数组的包装类
 *
 * @author yancheng11334
 */
public class HeaderArrayWrapper {
    private org.apache.http.Header[] headers;

    public HeaderArrayWrapper(org.apache.http.Header[] headers) {
        super();
        this.headers = headers;
    }

    public Header[] getHeaders() {
        if (headers != null) {
            Header[] newHeaders = new Header[headers.length];
            for (int i = 0; i < headers.length; i++) {
                newHeaders[i] = new HeaderWrapper(headers[i]);
            }
            return newHeaders;
        } else {
            return null;
        }
    }
}
