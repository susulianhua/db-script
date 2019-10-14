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

public class IRemoteConfigZKConstant {

    /**
     * 配置文件名
     */
    public static final String REMOTE_CONFIG_NAME = "/remoteconfig.properties";
    public static final String REMOTE_URLS = "urls";
    public static final String REMOTE_APP = "app";
    public static final String REMOTE_ENV = "env";
    public static final String REMOTE_VERSION = "version";
    public static final String REMOTE_USERNAME = "username";
    public static final String REMOTE_PASSWORD = "password";
    public static final String REMOTE_NEW_ENV_ABLE = "new_env_able";
    public static final String REMOTE_ZOOKEEPER_ROOT = "zookeeper_rootpath";
    /**
     * 配置项根节点
     */
    public static String REMOTE_BASE_DIR = "/remoteconfig";
    /**
     * 环境根节点
     */
    public static String REMOTE_ENVIRONMENT_BASE_DIR = "/environment";
    /**
     * 配置更改发布根节点
     */
    public static String REMOTE_PUBLISH_DIR = "/remoteconfigpublish";
    
    /**
     * 用户自定义zookeeper保存数据的根路径
     */
    public static String REMOTE_ROOT_PATH = "";
}
