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
package org.tinygroup.flow.test.newtestcase.inherit.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 辅助测试流程继承和重入的组件
 *
 * @author zhangliang08072
 * @version $Id: FlowInheritComponent.java, v 0.1 2016年4月28日 下午4:51:47 zhangliang08072 Exp $
 */
public class FlowInheritComponent implements ComponentInterface {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(FlowInheritComponent.class);

    String appendStr;

    public void execute(Context context) {
        LOGGER.logMessage(LogLevel.DEBUG, "====>流程继承组件开始运行");
        String resultStr = context.get("resultStr") + appendStr;
        context.put("resultStr", resultStr);

        LOGGER.logMessage(LogLevel.DEBUG, "====>流程继承组件结束运行");
    }

    /**
     * Getter method for property <tt>appendStr</tt>.
     *
     * @return property value of appendStr
     */
    public String getAppendStr() {
        return appendStr;
    }

    /**
     * Setter method for property <tt>appendStr</tt>.
     *
     * @param appendStr value to be assigned to property appendStr
     */
    public void setAppendStr(String appendStr) {
        this.appendStr = appendStr;
    }


}
