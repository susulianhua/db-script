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

import org.tinygroup.httpvisitor.AuthScope;

/**
 * 简易验证信息
 *
 * @author yancheng11334
 */
public class SimpleAuthScope implements AuthScope {

    /**
     * The authentication scheme the credentials apply to.
     */
    private String scheme;

    /**
     * The realm the credentials apply to.
     */
    private String realm;

    /**
     * The host the credentials apply to.
     */
    private String host;

    /**
     * The port the credentials apply to.
     */
    private int port;

    public SimpleAuthScope() {
        this(null, 0, null, null);
    }

    public SimpleAuthScope(
            final String host,
            final int port) {
        this(host, port, null, null);
    }

    public SimpleAuthScope(
            final String host,
            final int port,
            final String realm,
            final String scheme) {
        super();
        this.host = host;
        this.port = port;
        this.realm = realm;
        this.scheme = scheme;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
