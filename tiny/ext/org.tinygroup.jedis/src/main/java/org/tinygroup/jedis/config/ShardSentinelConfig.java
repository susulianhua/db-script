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
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import redis.clients.jedis.Protocol;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("shard-sentinel-config")
public class ShardSentinelConfig extends BaseAuthConfig {
    @XStreamAsAttribute
    private String pattern;
    /**
     * 所监听的主从集群配置的mastername
     */
    @XStreamAsAttribute
    @XStreamAlias("master-name")
    private String masterName;

    @XStreamAsAttribute
    @XStreamAlias("pool-config")
    private String poolConfig;

    /**
     * 数据库物理序号，默认是0，非必填项
     */
    @XStreamAsAttribute
    private int database;
    /**
     * 客户端超时时间，非必填项
     */
    @XStreamAsAttribute
    private int timeout;
    /**
     * 该集群中的所有服务器
     */
    @XStreamImplicit
    private List<JedisConfig> jedisConfigList;


    public List<JedisConfig> getJedisConfigList() {
        if (jedisConfigList == null) {
            this.jedisConfigList = new ArrayList<JedisConfig>();
        }
        return jedisConfigList;
    }

    public void setJedisConfigList(List<JedisConfig> jedisConfigList) {
        this.jedisConfigList = jedisConfigList;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public int getDatabase() {
        if (database == 0) {
            this.database = Protocol.DEFAULT_DATABASE;
        }
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public int getTimeout() {
        if (timeout == 0) {
            this.timeout = Protocol.DEFAULT_TIMEOUT;
        }
        return timeout;
    }

    public void setTimeout(int timeout) {

        this.timeout = timeout;
    }

    public String getPoolConfig() {
        return poolConfig;
    }

    public void setPoolConfig(String poolConfig) {
        this.poolConfig = poolConfig;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

}
