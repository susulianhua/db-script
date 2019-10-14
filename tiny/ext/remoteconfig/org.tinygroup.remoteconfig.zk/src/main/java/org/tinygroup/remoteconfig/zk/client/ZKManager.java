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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.data.Stat;
import org.tinygroup.exception.BaseRuntimeException;
import org.tinygroup.remoteconfig.config.ConfigPath;
import org.tinygroup.remoteconfig.config.ConfigValue;
import org.tinygroup.remoteconfig.config.Module;
import org.tinygroup.remoteconfig.zk.exception.ConfigItemNotExistException;
import org.tinygroup.remoteconfig.zk.utils.PathHelper;
import org.tinygroup.remoteconfig.zk.utils.SerializeUtil;

public class ZKManager extends BaseManager {

    public static ConfigValue get(String key, ConfigPath configPath) {
        try {
            key = PathHelper.createPath(key, configPath);
            ConfigValue value = getSimple(key);
            if (value == null) {
                throw new ConfigItemNotExistException("0TE120119014", key);
            }
            return value;
        } catch (ConfigItemNotExistException e){
        	throw e;
        } catch (Exception e) {
            throw new BaseRuntimeException("0TE120119003", e, key);
        }
    }

    public static Map<String, ConfigValue> getAll(ConfigPath configPath) {
        Map<String, ConfigValue> dataMap = new HashMap<String, ConfigValue>();
        String node = PathHelper.createPath("", configPath);
        try {
            List<String> subNodes = curator.getChildren().forPath(node);
            if (subNodes != null && !subNodes.isEmpty()) {
                for (String subNode : subNodes) {
                    try {
                        ConfigValue znodeValue = getSimple(node.concat("/").concat(subNode));
                        if (znodeValue != null) {
                            dataMap.put(subNode, znodeValue);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e) {
            throw new BaseRuntimeException("0TE120119006", e, node);
        }
        return dataMap;
    }

    public static void set(String key, ConfigValue value, ConfigPath configPath) {
        key = PathHelper.createPath(key, configPath);
        try {
            Stat stat = curator.checkExists().forPath(key);
            if (stat == null) {
                addSimple(key, value);
                return;
            }
            curator.setData().forPath(key, SerializeUtil.serialize(value));
        } catch (Exception e) {
            throw new BaseRuntimeException("0TE120119002", e, key, value);
        }
    }

    private static Stat addSimple(String node, ConfigValue value) throws BaseRuntimeException {
        try {
            Stat stat = curator.checkExists().forPath(node);
            if (stat == null) {
                //  create parent znodePath
                String parentPath = PathHelper.generateParentPath(node);
                if (StringUtils.isNotBlank(parentPath)) {
                    Stat parentStat = curator.checkExists().forPath(parentPath);
                    if (parentStat == null) {
                        addModule(parentPath, new Module());
                    }
                }
                curator.create().forPath(node, SerializeUtil.serialize(value));
            }
            return curator.checkExists().forPath(node);
        } catch (Exception e) {
            throw new BaseRuntimeException("0TE120119011", e, node, value);
        }
    }

    private static Stat addModule(String node, Module module) throws BaseRuntimeException {
        try {
            Stat stat = curator.checkExists().forPath(node);
            if (stat == null) {
                //  create parent znodePath
                String parentPath = PathHelper.generateParentPath(node);
                if (StringUtils.isNotBlank(parentPath)) {
                    Stat parentStat = curator.checkExists().forPath(parentPath);
                    if (parentStat == null) {
                        addModule(parentPath, new Module());
                    }
                }
                String name = StringUtils.substringAfterLast(node, "/");
                if (StringUtils.isBlank(module.getName())) {
                    module.setName(name);
                }
                if (StringUtils.isBlank(module.getModuleName())) {
                    module.setModuleName(name);
                }
                curator.create().forPath(node, SerializeUtil.serialize(module));
            }
            return curator.checkExists().forPath(node);
        } catch (Exception e) {
            throw new BaseRuntimeException("0TE120119011", e, node, module);
        }
    }

    private static ConfigValue getSimple(String key) throws Exception {
        Stat stat = curator.checkExists().forPath(key);
        if (stat != null) {
            byte[] resultData = curator.getData().forPath(key);
            if (resultData != null) {
                Object obj = SerializeUtil.unserialize(resultData);
                if (obj == null) {
                    return new ConfigValue(new String(resultData, "UTF-8"));
                } else if (obj instanceof String) {
                    return new ConfigValue(obj.toString());
                } else if (obj instanceof ConfigValue) {
                    return (ConfigValue) obj;
                }
            }
        }
        return null;
    }

}
