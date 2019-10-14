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

import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.weixin.WeiXinContext;
import org.tinygroup.weixin.WeiXinConvertMode;
import org.tinygroup.weixin.WeiXinHandlerMode;
import org.tinygroup.weixin.exception.WeiXinException;
import org.tinygroup.weixin.util.WeiXinParserUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 * 默认微信消息转换
 *
 * @author yancheng11334
 */
public class ReceiveParseHandler extends AbstractWeiXinHandler {

    public WeiXinHandlerMode getWeiXinHandlerMode() {
        return WeiXinHandlerMode.RECEIVE;
    }

    public <T> boolean isMatch(T message, WeiXinContext context) {
        return message instanceof String || message instanceof InputStream;
    }

    public <T> void process(T message, WeiXinContext context) {
        if (message instanceof String) {
            String content = (String) message;
            parseString(content, context);
        } else if (message instanceof InputStream) {
            InputStream inputStream = (InputStream) message;
            try {
                parseString(StreamUtil.readText(inputStream, "UTF-8", false), context);
            } catch (IOException e) {
                throw new WeiXinException(e);
            }
        }

    }

    private void parseString(String content, WeiXinContext context) {
        context.setInput(WeiXinParserUtil.parse(content, context, WeiXinConvertMode.RECEIVE));
    }

    /**
     * 优先级高
     */
    public int getPriority() {
        return Integer.MIN_VALUE + 100;
    }


}
