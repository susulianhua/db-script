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
package org.tinygroup.menucommand.handler;

import org.tinygroup.context.Context;
import org.tinygroup.menucommand.config.MenuCommand;

/**
 * 时间转换有两种做法：<br/>
 * 1. 在Handler处理，page渲染显示<br/>
 * 2. 直接利用模板函数formatDate在页面处理<br/>
 * 测试例子采用方案二
 *
 * @author yancheng11334
 */
public class TimeHandler extends MenuCommandHandler {

    @Override
    protected void execute(String command, MenuCommand menuCommand,
                           Context context) {
        // TODO Auto-generated method stub

    }

}
