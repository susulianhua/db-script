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

import org.tinygroup.convert.ConvertException;
import org.tinygroup.weixin.WeiXinContext;
import org.tinygroup.weixin.WeiXinHandlerMode;
import org.tinygroup.weixin.convert.NumberConvert;
import org.tinygroup.weixin.message.VoiceMessage;

/**
 * 语音转换处理器<br>
 * 1. 语音回复去除标点符号<br>
 * 2. 语音为数字的转成阿拉伯数字 <br>
 *
 * @author yancheng11334
 */
public class VoiceConvertHandler extends AbstractWeiXinHandler {

    private static final String[] FILTER_SYMBOL = new String[]{"!", "！"};

    private NumberConvert numberConvert = new NumberConvert();

    public VoiceConvertHandler() {
        this.setPriority(-100);
    }

    public WeiXinHandlerMode getWeiXinHandlerMode() {
        return WeiXinHandlerMode.RECEIVE;
    }

    public <T> boolean isMatch(T message, WeiXinContext context) {
        return message instanceof VoiceMessage;
    }

    public <T> void process(T message, WeiXinContext context) {
        VoiceMessage voiceMessage = (VoiceMessage) message;
        if (voiceMessage.getRecognition() != null) {
            String text = voiceMessage.getRecognition();
            //过滤标点
            for (String symbol : FILTER_SYMBOL) {
                text = text.replaceAll(symbol, "");
            }

            try {
                text = numberConvert.convert(text);
            } catch (ConvertException e) {
                //转换失败，记录日志
                LOGGER.errorMessage("处理语音的文字转换发生异常,text:{0}", e, text);
            }
            voiceMessage.setRecognition(text);
            context.setInput(voiceMessage);
        }
    }

}
