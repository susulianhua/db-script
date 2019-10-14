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
package org.tinygroup.httpclient31.response;

import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpState;
import org.tinygroup.httpclient31.wrapper.CookieArrayWrapper;
import org.tinygroup.httpclient31.wrapper.HeaderArrayWrapper;
import org.tinygroup.httpclient31.wrapper.StatusLineWrapper;
import org.tinygroup.httpvisitor.Cookie;
import org.tinygroup.httpvisitor.Header;
import org.tinygroup.httpvisitor.Response;
import org.tinygroup.httpvisitor.StatusLine;
import org.tinygroup.httpvisitor.response.AbstractResponse;

import java.io.IOException;
import java.io.InputStream;

/**
 * 基于httpclient3.1的响应实现
 *
 * @author yancheng11334
 */
public class DefaultResponse extends AbstractResponse implements Response {

    private HttpMethodBase method;
    private HttpState state;
    private StatusLine statusLine;

    public DefaultResponse(HttpMethodBase method, HttpState state) {
        this.method = method;
        this.state = state;
    }

    public void close() throws IOException {
        method.releaseConnection();
        state.clear();
    }

    public StatusLine getStatusLine() {
        if (statusLine == null) {
            statusLine = new StatusLineWrapper(method.getStatusLine());
        }
        return statusLine;
    }

    public Header[] getHeaders() {
        return new HeaderArrayWrapper(method.getResponseHeaders()).getHeaders();
    }

    public Cookie[] getCookies() {
        return new CookieArrayWrapper(state.getCookies()).getCookies();
    }

    protected InputStream getSourceInputStream() throws IOException {
        return method.getResponseBodyAsStream();
    }

    protected Response self() {
        return this;
    }


}
