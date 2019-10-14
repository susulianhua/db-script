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
package org.tinygroup.httpvisitor.client;

import org.tinygroup.httpvisitor.RequestInterceptor;
import org.tinygroup.httpvisitor.struct.KeyCert;
import org.tinygroup.httpvisitor.struct.PasswordCert;
import org.tinygroup.httpvisitor.struct.Proxy;

import java.util.List;

/**
 * 链式客户端构造器接口
 *
 * @param <Builder>
 * @author yancheng11334
 */
public interface ClientBuilderInterface<Builder> {

    /**
     * how long http connection keep, in milliseconds. default -1, get from server response
     */
    Builder timeToLive(long timeToLive);

    /**
     * set userAgent
     */
    Builder userAgent(String userAgent);

    /**
     * if verify http certificate, default true
     */
    Builder verify(boolean verify);

    /**
     * If follow get/head redirect, default true.
     * This method not set following redirect for post/put/delete method, use {@code allowPostRedirects} if you want this
     */
    Builder allowRedirects(boolean allowRedirects);

    /**
     * if send compress requests. default true
     */
    Builder compress(boolean compress);

    /**
     * Set socket timeout and connect timeout in millis
     */
    Builder timeout(int timeout);

    /**
     * Set socket timeout in millis
     */
    Builder socketTimeout(int timeout);

    /**
     * Set connect timeout in millis
     */
    Builder connectTimeout(int timeout);

    /**
     * 带验证代理
     *
     * @param host
     * @param port
     * @param proxyName
     * @param password
     * @return
     */
    Builder proxy(String host, int port, String proxyName, String password);

    /**
     * 无验证代理
     *
     * @param host
     * @param port
     * @return
     */
    Builder proxy(String host, int port);

    /**
     * 代理
     *
     * @param proxy
     * @return
     */
    Builder proxy(Proxy proxy);

    /**
     * 口令认证
     *
     * @param userName
     * @param password
     * @return
     */
    Builder auth(String userName, String password);

    /**
     * 口令认证
     *
     * @param cert
     * @return
     */
    Builder auth(PasswordCert cert);

    /**
     * 秘钥认证
     *
     * @param certPath
     * @param password
     * @param certType
     * @return
     */
    Builder auth(String certPath, String password, String certType);

    /**
     * 秘钥认证
     *
     * @param cert
     * @return
     */
    Builder auth(KeyCert cert);

    /**
     * 注册请求拦截器
     * @param interceptor
     * @return
     */
    Builder intercept(RequestInterceptor interceptor);

    /**
     * 批量注册请求拦截器
     * @param interceptorList
     * @return
     */
    Builder intercept(List<RequestInterceptor> interceptorList);
}
