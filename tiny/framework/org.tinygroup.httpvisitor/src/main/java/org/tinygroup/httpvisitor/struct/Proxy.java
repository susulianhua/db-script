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
package org.tinygroup.httpvisitor.struct;

/**
 * 代理信息类
 *
 * @author yancheng11334
 */
public class Proxy {

    private String host;
    private int port;
    private String proxyName;
    private String password;

    public Proxy(String host, int port, String proxyName, String password) {
        super();
        this.host = host;
        this.port = port;
        this.proxyName = proxyName;
        this.password = password;
    }

    public Proxy(String host, int port) {
        super();
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getProxyName() {
        return proxyName;
    }

    public String getPassword() {
        return password;
    }

}
