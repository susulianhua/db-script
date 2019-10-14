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
/**
 *
 */
package org.tinygroup.remoteconfig.zk.client;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.tinygroup.exception.BaseRuntimeException;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.remoteconfig.config.ConfigPath;
import org.tinygroup.remoteconfig.zk.config.RemoteConfig;
import org.tinygroup.remoteconfig.zk.utils.PathHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
public class BaseManager {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(BaseManager.class);

    protected static CuratorFramework curator;

    protected static RemoteConfig config;

    private static int RE_TRY_SLEEP = 10000;

    public static RemoteConfig getConfig() {
        return config;
    }

    public static void setConfig(RemoteConfig config) {
        BaseManager.config = config;
    }

    public static void start() {
        LOGGER.logMessage(LogLevel.INFO, "开始客户端远程配置初始化...");
        try {
            LOGGER.logMessage(LogLevel.INFO, "创建远程链接开始...");

            final String usernamePassword = getConfig().getUsernamePassword();

            Builder builder = CuratorFrameworkFactory.builder().
                    connectString(getConfig().getUrls()).
                    retryPolicy(new RetryNTimes(Integer.MAX_VALUE, RE_TRY_SLEEP));
            if (StringUtils.isNotBlank(usernamePassword)) {
                ACLProvider aclProvider = new ACLProvider() {
                    private List<ACL> acls;

                    @Override
                    public List<ACL> getDefaultAcl() {
                        if (acls == null) {
                            acls = new ArrayList<ACL>();
                            acls.add(new ACL(Perms.ALL, new Id("auth", usernamePassword)));
                        }
                        return acls;
                    }

                    public List<ACL> getAclForPath(String path) {
                        return acls;
                    }
                };
                builder.aclProvider(aclProvider).authorization("digest", usernamePassword.getBytes());
            }
            curator = builder.build();

            curator.start();
            // 赋予权限
            LOGGER.logMessage(LogLevel.INFO, "创建远程链接成功...");


        } catch (Exception e) {
            throw new BaseRuntimeException("0TE120119001", e, getConfig()
                    .toString());
        }
    }

    public static void stop() {
        if (curator != null) {
            curator.close();
        }
    }

    public static void delete(String key, ConfigPath configPath) {
        key = PathHelper.createPath(key, configPath);
        clearSimple(key);
    }

    private static void deleteSimple(String node) {
        try {
            Stat stat = curator.checkExists().forPath(node);
            if (stat != null) {
                curator.delete().forPath(node);
                LOGGER.logMessage(LogLevel.DEBUG,
                        String.format("节点[%s]，删除成功", node));
            } else {
                LOGGER.logMessage(LogLevel.DEBUG,
                        String.format("节点[%s]不存在", node));
            }
        } catch (Exception e) {
            throw new BaseRuntimeException("0TE120119007", e, node);
        }
    }

    private static void clearSimple(String node) {
        try {
            Stat stat = curator.checkExists().forPath(node);
            if (stat != null) {

                for (String subNode : curator.getChildren().forPath(node)) {
                    clearSimple(node.concat("/").concat(subNode));
                }
                deleteSimple(node);
            }
        } catch (Exception e) {
            throw new BaseRuntimeException("0TE120119009", e, node);
        }
    }

    public static boolean exists(String key, ConfigPath configPath) {
        try {
            key = PathHelper.createPath(key, configPath);
            Stat stat = curator.checkExists().forPath(key);
            if (stat == null) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            throw new BaseRuntimeException(e);
        }
    }

}
