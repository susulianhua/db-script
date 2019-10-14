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
package org.tinygroup.earthworm.impl;

import org.tinygroup.earthworm.LogSupport;
import org.tinygroup.earthworm.RpcContext;

public class BaseLogSupport implements LogSupport {

    public String getLogMessage(String stage) {
        return getLogMessage(stage, System.currentTimeMillis());
    }

    @Override
    public String getLogMessage(String stage, long time) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("support_type:BaseLogSupport").append(";");
        sb.append("module_type:").append(stage).append(";");
        sb.append("timestamp:").append(time).append(";");
        sb.append(RpcContext.get().toString());
        sb.append("}");
        return sb.toString();
    }
}
