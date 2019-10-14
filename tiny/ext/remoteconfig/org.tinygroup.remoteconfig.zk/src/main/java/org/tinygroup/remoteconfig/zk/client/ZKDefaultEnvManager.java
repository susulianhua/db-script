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
import org.apache.zookeeper.data.Stat;
import org.tinygroup.exception.BaseRuntimeException;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.remoteconfig.config.Environment;
import org.tinygroup.remoteconfig.zk.utils.EnvPathHelper;
import org.tinygroup.remoteconfig.zk.utils.PathHelper;
import org.tinygroup.remoteconfig.zk.utils.SerializeUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZKDefaultEnvManager extends BaseManager {

    public static Environment get(String key) {
        try {
            key = EnvPathHelper.createPath(key);
            return getSimple(key);
        } catch (Exception e) {
            throw new BaseRuntimeException("0TE120119003", e, key);
        }
    }

    public static Map<String, Environment> getAll() {
        Map<String, Environment> dataMap = new HashMap<String, Environment>();
        String node = EnvPathHelper.createPath("");
        LOGGER.logMessage(LogLevel.DEBUG, String.format("远程配置，批量获取环境[%s]", node));
        try {
            List<String> subNodes = curator.getChildren().forPath(node);
            if (subNodes != null && !subNodes.isEmpty()) {
                for (String subNode : subNodes) {
                    Environment environment = getSimple(node.concat("/").concat(subNode));
                    if (environment != null) {
                        dataMap.put(subNode, environment);
                    }
                }
            }
        } catch (Exception e) {
            throw new BaseRuntimeException("0TE120119006", e, node);
        }
        return dataMap;
    }

    public static void set(String key, Environment environment) {
        key = EnvPathHelper.createPath(key);
        try {
            Stat stat = curator.checkExists().forPath(key);
            if (stat == null) {
                addSimple(key, environment);
                return;
            }
            curator.setData().forPath(key, SerializeUtil.serialize(environment));
        } catch (Exception e) {
            throw new BaseRuntimeException("0TE120119002", e, key, environment);
        }
    }

    private static Stat addSimple(String node, Environment environment) throws BaseRuntimeException {
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
                curator.create().forPath(node, SerializeUtil.serialize(environment));
            }
            return curator.checkExists().forPath(node);
        } catch (Exception e) {
            throw new BaseRuntimeException("0TE120119011", e, node, environment);
        }
    }

    private static Environment getSimple(String key) throws Exception {
        Stat stat = curator.checkExists().forPath(key);
        if (stat != null) {
            byte[] resultData = curator.getData().forPath(key);
            if (resultData != null) {
                Object obj = SerializeUtil.unserialize(resultData);
                if (obj instanceof Environment) {
                    return (Environment) obj;
                }
            }
        }
        return null;
    }

    public static void delete(String key) {
        key = EnvPathHelper.createPath(key);
        try {
            Stat stat = curator.checkExists().forPath(key);
            if (stat != null) {
                curator.delete().forPath(key);
            }
        } catch (Exception e) {
            throw new BaseRuntimeException("0TE120119007", e, key);
        }
    }

}
