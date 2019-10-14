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
import org.tinygroup.commons.tools.StringUtil;

import java.util.List;

@XStreamAlias("shard-jedis-sentinel-configs")
public class ShardJedisSentinelConfigs {
    @XStreamAsAttribute
    private String id;
    @XStreamAsAttribute
    private String pool;

    @XStreamImplicit
    private List<ShardJedisSentinelConfig> jedisShardSentinelConfigsList;

    public List<ShardJedisSentinelConfig> getJedisShardSentinelConfigsList() {
        return jedisShardSentinelConfigsList;
    }

    public void setJedisShardSentinelConfigsList(
            List<ShardJedisSentinelConfig> jedisShardSentinelConfigsList) {
        this.jedisShardSentinelConfigsList = jedisShardSentinelConfigsList;
    }

    public String getId() {
        if (StringUtil.isBlank(id)) {
            id = "";
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPool() {
        return pool;
    }

    public void setPool(String pool) {
        this.pool = pool;
    }

}
