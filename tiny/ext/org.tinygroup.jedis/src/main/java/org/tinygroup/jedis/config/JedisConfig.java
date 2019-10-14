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
package org.tinygroup.jedis.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.tinygroup.jedis.util.JedisUtil;

@XStreamAlias("jedis-config")
public class JedisConfig extends BaseAuthConfig {
    /**
     * 唯一标识,必填项
     */
    @XStreamAsAttribute
    private String id;

    /**
     * redis转换编码，默认utf-8，选填项
     */
    @XStreamAsAttribute
    private String charset;

    /**
     * redis服务器地址，必填项
     */
    @XStreamAsAttribute
    private String host;

    /**
     * redis服务器端口，必填项
     */
    @XStreamAsAttribute
    private int port;

    /**
     * 客户端超时时间，非必填项
     */
    @XStreamAsAttribute
    private int timeout;


    /**
     * 数据库物理序号，默认是0，非必填项
     */
    @XStreamAsAttribute
    private int database;

    /**
     * 客户端名称，非必填项
     */
    @XStreamAsAttribute
    @XStreamAlias("client-name")
    private String clientName;

    /**
     * 连接池配置
     */
    @XStreamAsAttribute
    @XStreamAlias("pool-config")
    private String poolConfig;

    public String getPoolConfig() {
        return poolConfig;
    }

    public void setPoolConfig(String poolConfig) {
        this.poolConfig = poolConfig;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
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

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public String toString() {
        return "JedisConfig [id=" + id + ", charset=" + charset + ", host="
                + host + ", port=" + port + ", timeout=" + timeout
                + ", password=" + getPassword() + ", database=" + database
                + ", clientName=" + clientName + "] " + poolConfig;
    }

    public String toSimpleString() {
        return JedisUtil.toSimpleString(host, port);
    }

}
