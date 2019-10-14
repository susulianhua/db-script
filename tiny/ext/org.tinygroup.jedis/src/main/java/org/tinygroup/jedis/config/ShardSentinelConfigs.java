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

import java.util.List;

@XStreamAlias("shard-sentinel-configs")
public class ShardSentinelConfigs {
    /**
     * sentinel服务器信息,结构为 ip:port 可以填写多个，多条信息以逗号分隔
     */
    @XStreamAsAttribute
    private String sentinels;

    @XStreamImplicit
    private List<ShardSentinelConfig> shardSentinelConfigLists;

    public List<ShardSentinelConfig> getShardSentinelConfigLists() {
        if (shardSentinelConfigLists == null) {
            throw new RuntimeException("未配置任何redis信息");
        }
        return shardSentinelConfigLists;
    }

    public void setShardSentinelConfigLists(
            List<ShardSentinelConfig> shardSentinelConfigLists) {
        this.shardSentinelConfigLists = shardSentinelConfigLists;
    }

    public String getSentinels() {
        return sentinels;
    }

    public void setSentinels(String sentinels) {
        this.sentinels = sentinels;
    }


}
