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
package org.tinygroup.remoteconfig.zk.config;

import org.apache.commons.lang.StringUtils;
import org.tinygroup.commons.cryptor.Cryptor;
import org.tinygroup.commons.cryptor.DefaultCryptor;


/**
 * @author yanwj
 */
public class RemoteConfig {

    // ZK服务器地址列表，格式：127.0.0.0:80,127.0.0.0:81,127.0.0.0:82
    String urls;
    // app名称
    String app;
    // 环境
    String env;
    // 版本
    String version;
    // 账户
    String username;
    // 密码
    String password;
    // zookeeper数据存储的根路径 格式/xxxx或 /xxxx/xxxx
    String zookeeperRootPath;

    boolean newEnvAble = false;

    public RemoteConfig() {
    }

    public RemoteConfig(String urls, String app, String env, String version) {
        super();
        this.urls = urls;
        this.app = app;
        this.env = env;
        this.version = version;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getZookeeperRootPath() {
		return zookeeperRootPath;
	}

	public void setZookeeperRootPath(String zookeeperRootPath) {
		this.zookeeperRootPath = zookeeperRootPath;
	}

	public String getUsernamePassword() {
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            Cryptor cryptor = new DefaultCryptor();
            try {
                return username + ":" + cryptor.decrypt(password);
            } catch (Exception e) {
                throw new RuntimeException("密码解密失败：" + password);
            }
        }
        return StringUtils.EMPTY;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("urls=%s \r\n", urls));
        sb.append(String.format("app=%s \r\n", app));
        sb.append(String.format("env=%s \r\n", env));
        sb.append(String.format("version=%s \r\n", version));
        sb.append(String.format("username=%s \r\n", username));
        sb.append(String.format("password=%s \r\n", password));
        sb.append(String.format("zookeeperRootPath=%s \r\n", zookeeperRootPath));
        return sb.toString();
    }

    public boolean isNewEnvAble() {
        return newEnvAble;
    }

    public void setNewEnvAble(boolean newEnvAble) {
        this.newEnvAble = newEnvAble;
    }

}
