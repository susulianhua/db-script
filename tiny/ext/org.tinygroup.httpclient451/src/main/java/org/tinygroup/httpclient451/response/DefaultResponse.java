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
package org.tinygroup.httpclient451.response;

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.tinygroup.httpclient451.wrapper.CookieArrayWrapper;
import org.tinygroup.httpclient451.wrapper.HeaderArrayWrapper;
import org.tinygroup.httpclient451.wrapper.StatusLineWrapper;
import org.tinygroup.httpvisitor.Cookie;
import org.tinygroup.httpvisitor.Header;
import org.tinygroup.httpvisitor.Response;
import org.tinygroup.httpvisitor.StatusLine;
import org.tinygroup.httpvisitor.response.AbstractResponse;

import java.io.IOException;
import java.io.InputStream;

/**
 * 基于httpclient4.5.1的响应实现
 *
 * @author yancheng11334
 */
public class DefaultResponse extends AbstractResponse implements Response {

    private HttpRequestBase method;
    private CookieStore cookieStore;
    private CloseableHttpResponse closeableHttpResponse;
    private StatusLine statusLine;

    public DefaultResponse(HttpRequestBase method, CookieStore cookieStore,
                           CloseableHttpResponse closeableHttpResponse) {
        super();
        this.method = method;
        this.cookieStore = cookieStore;
        this.closeableHttpResponse = closeableHttpResponse;
    }

    public void close() throws IOException {
        closeableHttpResponse.close();
        method.abort();
    }

    public StatusLine getStatusLine() {
        if (statusLine == null) {
            statusLine = new StatusLineWrapper(closeableHttpResponse.getStatusLine());
        }
        return statusLine;
    }

    public Header[] getHeaders() {
        return new HeaderArrayWrapper(closeableHttpResponse.getAllHeaders()).getHeaders();
    }

    public Cookie[] getCookies() {
        return new CookieArrayWrapper(cookieStore.getCookies()).getCookies();
    }

    protected InputStream getSourceInputStream() throws IOException {
        return closeableHttpResponse.getEntity() == null ? null : closeableHttpResponse.getEntity().getContent();
    }

    protected Response self() {
        return this;
    }

}
