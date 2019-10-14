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
package org.tinygroup.httpvisitor.builder;

import org.tinygroup.httpvisitor.*;
import org.tinygroup.httpvisitor.client.ClientBuilder;
import org.tinygroup.httpvisitor.client.ClientBuilderInterface;
import org.tinygroup.httpvisitor.client.ClientInterface;
import org.tinygroup.httpvisitor.execption.HttpVisitorException;
import org.tinygroup.httpvisitor.request.HttpRequestBuilder;
import org.tinygroup.httpvisitor.request.HttpRequestBuilderInterface;
import org.tinygroup.httpvisitor.struct.KeyCert;
import org.tinygroup.httpvisitor.struct.PasswordCert;
import org.tinygroup.httpvisitor.struct.Proxy;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 结合HTTP构建器和客户端构建器
 *
 * @author yancheng11334
 */
public abstract class RequestClientBuilder<R extends RequestClientBuilder<R, H>, H extends HttpRequestBuilder<H>>
        implements ClientBuilderInterface<R>, HttpRequestBuilderInterface<R>, Executable<Response> {

    public R timeToLive(long timeToLive) {
        findClientBuilder().timeToLive(timeToLive);
        return self();
    }

    public R userAgent(String userAgent) {
        findClientBuilder().userAgent(userAgent);
        return self();
    }

    public R verify(boolean verify) {
        findClientBuilder().verify(verify);
        return self();
    }

    public R allowRedirects(boolean allowRedirects) {
        findClientBuilder().allowRedirects(allowRedirects);
        return self();
    }

    public R compress(boolean compress) {
        findClientBuilder().compress(compress);
        return self();
    }

    public R timeout(int timeout) {
        findClientBuilder().timeout(timeout);
        return self();
    }

    public R socketTimeout(int timeout) {
        findClientBuilder().socketTimeout(timeout);
        return self();
    }

    public R connectTimeout(int timeout) {
        findClientBuilder().connectTimeout(timeout);
        return self();
    }

    public R proxy(String host, int port, String proxyName, String password) {
        findClientBuilder().proxy(host, port, proxyName, password);
        return self();
    }

    public R proxy(String host, int port) {
        findClientBuilder().proxy(host, port);
        return self();
    }

    public R proxy(Proxy proxy) {
        findClientBuilder().proxy(proxy);
        return self();
    }

    public R auth(String userName, String password) {
        findClientBuilder().auth(userName, password);
        return self();
    }

    public R auth(PasswordCert cert) {
        findClientBuilder().auth(cert);
        return self();
    }

    public R auth(String certPath, String password, String certType) {
        findClientBuilder().auth(certPath, password, certType);
        return self();
    }

    public R auth(KeyCert cert) {
        findClientBuilder().auth(cert);
        return self();
    }

    public R intercept(RequestInterceptor interceptor) {
        findClientBuilder().intercept(interceptor);
        return self();
    }

    @SuppressWarnings("unchecked")
    public R intercept(List<RequestInterceptor> interceptorList) {
        findClientBuilder().intercept(interceptorList);
        return self();
    }

    public R charset(String charset) {
        findRequsetBuilder().charset(charset);
        return self();
    }

    public R charset(Charset charset) {
        findRequsetBuilder().charset(charset);
        return self();
    }

    public R param(String name, Object value) {
        findRequsetBuilder().param(name, value);
        return self();
    }

    public R params(Map<String, Object> maps) {
        findRequsetBuilder().params(maps);
        return self();
    }

    public R header(String name, String value) {
        findRequsetBuilder().header(name, value);
        return self();
    }

    public R headers(Map<String, String> maps) {
        findRequsetBuilder().headers(maps);
        return self();
    }

    public R cookie(String domain, String name,
                    String value) {
        findRequsetBuilder().cookie(domain, name, value);
        return self();
    }

    public R cookie(Cookie cookie) {
        findRequsetBuilder().cookie(cookie);
        return self();
    }

    public R cookies(Map<String, Cookie> cookies) {
        findRequsetBuilder().cookies(cookies);
        return self();
    }

    public Response execute() {
        ClientInterface client = findClientBuilder().build();
        Request request = findRequsetBuilder().execute();

        //合并用户的拦截器
        List<RequestInterceptor> interceptorList = new ArrayList<RequestInterceptor>();
        interceptorList.addAll(client.getInterceptorList());
        interceptorList.addAll(findClientBuilder().getInterceptorList());
        try {
            if (interceptorList != null && interceptorList.size() > 0) {
                for (RequestInterceptor interceptor : interceptorList) {
                    interceptor.process(request);
                }
            }
        } catch (Exception e) {
            throw new HttpVisitorException("执行拦截器发生异常:", e);
        }
        return client.execute(request);
    }

    protected abstract R self();

    protected abstract ClientBuilder findClientBuilder();

    protected abstract HttpRequestBuilder<H> findRequsetBuilder();
}
