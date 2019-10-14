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

import org.tinygroup.weixin.message.TextMessage;
import org.tinygroup.weixin.replymessage.TextReplyMessage;

/**
 * 微信菜单处理器(文本消息)
 *
 * @author yancheng11334
 */
public class TextMenuConfigHandler extends AbstractMenuConfigHandler {

    protected <T> String getContent(T message) {
        TextMessage textMessage = (TextMessage) message;
        return textMessage.getContent();
    }

    @SuppressWarnings("unchecked")
    protected <T, OUTPUT> OUTPUT wrapperReplyMessage(
            T message, String content) {
        TextMessage textMessage = (TextMessage) message;

        TextReplyMessage replyMessage = new TextReplyMessage();
        replyMessage.setContent(content);
        replyMessage.setToUserName(textMessage.getFromUserName());
        replyMessage.setFromUserName(textMessage.getToUserName());
        return (OUTPUT) replyMessage;
    }

    @Override
    protected <T> boolean isMatchType(T message) {
        return message instanceof TextMessage;
    }

}
