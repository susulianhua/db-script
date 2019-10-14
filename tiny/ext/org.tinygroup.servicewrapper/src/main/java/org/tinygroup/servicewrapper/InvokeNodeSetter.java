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
package org.tinygroup.servicewrapper;

import org.springframework.core.NamedThreadLocal;
import org.tinygroup.commons.tools.StringUtil;

public class InvokeNodeSetter {

    private static final ThreadLocal<String> NODE_NAME = new NamedThreadLocal<String>("event node name");

    public static String getNodeName() {
        return NODE_NAME.get();
    }

    public static void setNodeName(String nodeName) {
        if (!StringUtil.isBlank(nodeName)) {
            NODE_NAME.set(nodeName);
        }
    }

    public static void removeNodeName() {
        NODE_NAME.remove();
    }

}
