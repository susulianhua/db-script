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
package org.tinygroup.remoteconfig.placeholder;

import org.apache.commons.lang.StringUtils;
import org.tinygroup.remoteconfig.IRemoteConfigConstant;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 占位符规则
 *
 * @author yanwj06282
 *
 */
public class HostNameHander implements PlaceHolderHander {

    public String handle(String str) {
        if (StringUtils.indexOf(str, IRemoteConfigConstant.HOST_NAME) > -1) {
            return StringUtils.replace(str, IRemoteConfigConstant.HOST_NAME, getHostName());
        }
        return str;
    }

    private String getHostName() {
        InetAddress addr;
        try {
            addr = InetAddress.getLocalHost();
            return addr.getHostName();
        } catch (UnknownHostException e) {
            throw new RuntimeException("获取主机hostname失败！", e);
        }
    }

}
