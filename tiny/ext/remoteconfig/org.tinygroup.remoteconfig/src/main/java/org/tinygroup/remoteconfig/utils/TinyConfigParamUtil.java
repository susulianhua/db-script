/**
 * Copyright (c) 2012-2017, www.tinygroup.org (luo_guo@icloud.com).
 * <p>
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * <p>
 *       http://www.gnu.org/licenses/gpl.html
 * <p>
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.remoteconfig.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.tinygroup.remoteconfig.RemoteConfigReadClient;

public class TinyConfigParamUtil {
    public final static String REMOTE_CONFIG_CLASS = "remote-config-class";

    private static Map<String, String> map = new HashMap<String, String>();
    
    private static String clientId;//客户端ID，用于向配置中心注册
    private static Date lastUpdateConfigTime;//最近一次从配置中心获取配置的时间
    private static long version;//远程配置的版本，应用启动时第一次读取版本为1

    /**
     * web.xml的配置中心配置优先级最高
     * 在配置了的情况下，其他同等配置失效
     */
    private static Boolean enable;
    
    private static RemoteConfigReadClient readClient;

    public static RemoteConfigReadClient getReadClient() {
        return readClient;
    }

    public static void setReadClient(RemoteConfigReadClient readClient) {
        TinyConfigParamUtil.readClient = readClient;
    }

    public static Boolean getEnable() {
		return enable;
	}

	public static void setEnable(Boolean enable) {
		TinyConfigParamUtil.enable = enable;
	}

	public static void putConfig(String param, String value) {
        map.put(param, value);
    }

    public static void removeConfig(String param) {
        map.remove(param);
    }

    public static String getConfig(String name) {
        return map.get(name);
    }

    public static void clear() {
        map.clear();
        readClient = null;
    }

	public static String getClientId() {
		return clientId;
	}

	public static void setClientId(String clientId) {
		TinyConfigParamUtil.clientId = clientId;
	}

	public static Date getLastUpdateConfigTime() {
		return lastUpdateConfigTime;
	}

	public static void setLastUpdateConfigTime(Date lastUpdateConfigTime) {
		TinyConfigParamUtil.lastUpdateConfigTime = lastUpdateConfigTime;
	}

	public static long getVersion() {
		return version;
	}

	public static void setVersion(long version) {
		TinyConfigParamUtil.version = version;
	}
}
