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
package org.tinygroup.httpclient451.client;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.tinygroup.context.Context;
import org.tinygroup.httpclient451.response.DefaultResponse;
import org.tinygroup.httpvisitor.Request;
import org.tinygroup.httpvisitor.Response;
import org.tinygroup.httpvisitor.execption.HttpVisitorException;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 线程使用一个HttpClient
 *
 * @author yancheng11334
 */
public class SingleClientImpl extends AbstractHttpClient451 {

    public Response execute(Request request) {
        Charset requestCharset = getCharset(request);
        HttpRequestBase method = dealHttpMethod(requestCharset, request);
        dealHeaders(method, request.getHeaders());
        dealCookies(method, request.getCookies());
        dealBodyElement(method, requestCharset, request.getBodyElements());
        // 具体执行逻辑
        try {
            CloseableHttpResponse closeableHttpResponse = httpClient.execute(
                    method, httpClientContext);
            return new DefaultResponse(method, httpClientContext.getCookieStore(),
                    closeableHttpResponse);
        } catch (ClientProtocolException e) {
            throw new HttpVisitorException("Http协议标识存在错误", e);
        } catch (IOException e) {
            throw new HttpVisitorException("Http通讯发生异常", e);
        }
    }

    public boolean allowMultiton() {
        return false;
    }

    protected HttpClientConnectionManager buildHttpClientConnectionManager(
            Context context) {
        return new BasicHttpClientConnectionManager();
    }

}
