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
package org.tinygroup.remoteconfig.zk.utils;

import org.apache.commons.lang.StringUtils;
import org.tinygroup.remoteconfig.zk.client.IRemoteConfigZKConstant;

public class EnvPathHelper {

    public static String createPath(String node) {
        String baseDir = IRemoteConfigZKConstant.REMOTE_ROOT_PATH + IRemoteConfigZKConstant.REMOTE_ENVIRONMENT_BASE_DIR;
        if (StringUtils.isNotBlank(node)) {
            node = PathHelper.appendSplit(node);
        } else {
            return baseDir;
        }
        return baseDir.concat(node);
    }

}
