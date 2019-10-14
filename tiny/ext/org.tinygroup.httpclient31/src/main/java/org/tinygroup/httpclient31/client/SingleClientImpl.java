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
package org.tinygroup.httpclient31.client;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.tinygroup.httpclient31.response.DefaultResponse;
import org.tinygroup.httpvisitor.Request;
import org.tinygroup.httpvisitor.Response;
import org.tinygroup.httpvisitor.execption.HttpVisitorException;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 单线程使用一个HttpClient
 *
 * @author yancheng11334
 */
public class SingleClientImpl extends AbstractHttpClient31 {

    public void close() throws IOException {
        httpClient.getHttpConnectionManager().closeIdleConnections(0);
        httpClient = null;
    }

    public Response execute(Request request) {
        Charset requestCharset = getCharset(request);
        HttpMethodBase method = dealHttpMethod(request);
        final HttpState state = httpClient.getState();
        dealHeaders(method, request.getHeaders());
        dealCookies(method, state, request.getCookies());
        dealBodyElement(method, requestCharset, request.getBodyElements());
        // 具体执行逻辑
        try {
            httpClient.executeMethod(null, method, state);
            return new DefaultResponse(method, state);
        } catch (Exception e) {
            throw new HttpVisitorException("执行HTTP访问发生异常", e);
        }

    }

    protected HttpClient buildHttpClient() {
        return new HttpClient(new SimpleHttpConnectionManager());
    }

    public boolean allowMultiton() {
        return false;
    }


}
