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
package org.tinygroup.weixin;

import org.tinygroup.context.Context;

/**
 * 微信上下文
 *
 * @author yancheng11334
 */
public interface WeiXinContext extends Context {

    public static final String DEFAULT_INPUT_NAME = "_default_wexin_input";

    public static final String DEFAULT_OUTPUT_NAME = "_default_wexin_output";

    public static final String WEIXIN_CLIENT = "_weixin_client";

    public static final String WEIXIN_SESSION = "_weixin_session";

    public static final String WEIXIN_MENU_CONFIG = "_weixin_menu_config";

    public static final String WEIXIN_MENU_TXET = "_weixin_menu_text";

    public static final String WEIXIN_MENU_COMMAND = "_weixin_menu_command";

    public static final String WEIXIN_ALL_COMMAND = "allMenuCommands";

    /**
     * 获取微信输入消息
     *
     * @param <INPUT>
     * @return
     */
    <INPUT> INPUT getInput();

    /**
     * 设置微信输入消息
     *
     * @param <INPUT>
     * @param input
     */
    <INPUT> void setInput(INPUT input);

    /**
     * 获取微信输出结果
     *
     * @param <OUTPUT>
     * @return
     */
    <OUTPUT> OUTPUT getOutput();

    /**
     * 设置微信输出结果
     *
     * @param <OUTPUT>
     * @param output
     */
    <OUTPUT> void setOutput(OUTPUT output);

    /**
     * 得到当前用户的会话
     *
     * @return
     */
    WeiXinSession getWeiXinSession();
}
