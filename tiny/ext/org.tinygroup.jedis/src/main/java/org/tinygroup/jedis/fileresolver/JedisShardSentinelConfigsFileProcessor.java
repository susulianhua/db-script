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
package org.tinygroup.jedis.fileresolver;

import com.thoughtworks.xstream.XStream;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.jedis.ShardJedisSentinelManager;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

/**
 * Jedis配置文件处理器，加载Jedis客户端配置信息
 *
 * @author yancheng11334
 */
public class JedisShardSentinelConfigsFileProcessor extends AbstractFileProcessor {

    public static final String JEDIS_SHARD_SENTINEL_XSTREAM_NAME = "jedis";
    private static final String JEDIS_SHARD_SENTINEL_CONFIG_EXT_NAME = ".jedisshardsentrinelconfig.xml";
    private ShardJedisSentinelManager shardJedisSentinelManager;


    public ShardJedisSentinelManager getShardJedisSentinelManager() {
        return shardJedisSentinelManager;
    }

    public void setShardJedisSentinelManager(
            ShardJedisSentinelManager shardJedisSentinelManager) {
        this.shardJedisSentinelManager = shardJedisSentinelManager;
    }

    public void process() {
        XStream stream = XStreamFactory.getXStream(JEDIS_SHARD_SENTINEL_XSTREAM_NAME);

    }

    @Override
    protected boolean checkMatch(FileObject fileObject) {
        return fileObject.getFileName().endsWith(JEDIS_SHARD_SENTINEL_CONFIG_EXT_NAME);
    }

}
