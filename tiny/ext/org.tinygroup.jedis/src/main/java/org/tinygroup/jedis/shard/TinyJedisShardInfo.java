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
package org.tinygroup.jedis.shard;

import org.tinygroup.jedis.config.JedisConfig;
import org.tinygroup.jedis.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TinyJedisShardInfo extends JedisShardInfo {
    List<JedisConfig> allRedisConfig = new ArrayList<JedisConfig>();
    Map<String, JedisShardInfo> map = new HashMap<String, JedisShardInfo>();


    /**
     * host port是写服务器的,其他的是读服务器的
     *
     * @param host
     * @param port
     * @param timeout
     * @param list
     */
    public TinyJedisShardInfo(String host, int port, int timeout,
                              List<JedisConfig> list) {
        super(host, port, timeout);
        for (JedisConfig config : list) {
            String simpleString = config.toSimpleString();
            if (simpleString.equals(JedisUtil.toSimpleString(host, port))) {
                continue;
            }
            map.put(simpleString,
                    JedisUtil.createJedisShardInfo(config));
        }
    }

    public List<Jedis> createAllReadResource() {
        List<Jedis> list = new ArrayList<Jedis>();
        for (JedisShardInfo info : map.values()) {
            list.add(new Jedis(info));
        }
        return list;
    }
}
