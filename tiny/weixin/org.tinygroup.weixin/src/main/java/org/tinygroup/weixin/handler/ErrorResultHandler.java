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
package org.tinygroup.weixin.handler;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.weixin.WeiXinContext;
import org.tinygroup.weixin.WeiXinHandlerMode;
import org.tinygroup.weixin.result.ErrorResult;

/**
 * 错误结果处理器
 *
 * @author yancheng11334
 */
public class ErrorResultHandler extends AbstractWeiXinHandler {

    public WeiXinHandlerMode getWeiXinHandlerMode() {
        return WeiXinHandlerMode.SEND_OUPUT;
    }

    public <T> boolean isMatch(T message, WeiXinContext context) {
        return message instanceof ErrorResult;
    }

    public <T> void process(T message, WeiXinContext context) {
        ErrorResult result = (ErrorResult) message;
        LOGGER.logMessage(LogLevel.ERROR, "errcode:{0},errmsg:{1},input:{2}", result.getErrCode(), result.getErrMsg(), context.getInput());
    }

}
