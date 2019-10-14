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
package org.tinygroup.remoteconfig.zk.client;

import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.KeeperException.NoAuthException;
import org.apache.zookeeper.data.Stat;
import org.tinygroup.exception.BaseRuntimeException;
import org.tinygroup.remoteconfig.config.ConfigPath;
import org.tinygroup.remoteconfig.config.Version;
import org.tinygroup.remoteconfig.zk.utils.PathHelper;
import org.tinygroup.remoteconfig.zk.utils.SerializeUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZKVersionManager extends BaseManager {

    public static Version get(String key, ConfigPath configPath) {
        try {
            key = PathHelper.createPath(key, configPath);
            return getSimple(key);
        } catch (Exception e) {
            throw new BaseRuntimeException("0TE120119003", e, key);
        }
    }

    public static Map<String, Version> getAll(ConfigPath configPath) {
        Map<String, Version> dataMap = new HashMap<String, Version>();
        String node = PathHelper.createPath("", configPath);
        try {
            List<String> subNodes = curator.getChildren().forPath(node);
            if (subNodes != null && !subNodes.isEmpty()) {
                for (String subNode : subNodes) {
                    Version version = getSimple(node.concat("/").concat(subNode));
                    if (version != null) {
                        dataMap.put(subNode, version);
                    }
                }
            }
        } catch (Exception e) {
            throw new BaseRuntimeException("0TE120119006", e, node);
        }
        return dataMap;
    }

    public static void set(String key, Version version, ConfigPath configPath) {
        key = PathHelper.createPath(key, configPath);

        try {
            Stat stat = curator.checkExists().forPath(key);
            if (stat == null) {
                addSimple(key, version);
                return;
            }
            curator.setData().forPath(key, SerializeUtil.serialize(version));
        } catch (Exception e) {
            throw new BaseRuntimeException("0TE120119002", e, key, version);
        }
    }

    private static Stat addSimple(String node, Version version) throws BaseRuntimeException {
        try {
            Stat stat = curator.checkExists().forPath(node);
            if (stat == null) {
                //  create parent znodePath
                String parentPath = PathHelper.generateParentPath(node);
                if (StringUtils.isNotBlank(parentPath)) {
                    Stat parentStat = curator.checkExists().forPath(parentPath);
                    if (parentStat == null) {
                        throw new BaseRuntimeException("0TE120119013", node);
                    }
                }
                curator.create().forPath(node, SerializeUtil.serialize(version));
            }
            return curator.checkExists().forPath(node);
        } catch (Exception e) {
            throw new BaseRuntimeException("0TE120119011", e, node, version);
        }
    }

    private static Version getSimple(String key) throws Exception {
        Stat stat = curator.checkExists().forPath(key);
        if (stat != null) {
            try {
                byte[] resultData = curator.getData().forPath(key);
                if (resultData != null) {
                    return SerializeUtil.unserialize(resultData);
                }
            } catch (NoAuthException e) {
                //无权限访问，直接跳过
            }
        }
        return null;
    }

}
