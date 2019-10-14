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

import org.tinygroup.menucommand.CommandExecutor;
import org.tinygroup.menucommand.CommandResult;
import org.tinygroup.weixin.WeiXinContext;
import org.tinygroup.weixin.WeiXinSession;
import org.tinygroup.weixin.event.ClickEvent;
import org.tinygroup.weixin.exception.WeiXinException;
import org.tinygroup.weixin.replymessage.TextReplyMessage;

/**
 * 微信菜单处理器(点击推送消息)
 *
 * @author yancheng11334
 */
public class ClickMenuConfigHandler extends AbstractMenuConfigHandler {

    protected <T> boolean isMatchType(T message) {
        return message instanceof ClickEvent;
    }

    @SuppressWarnings("unchecked")
    protected <T, OUTPUT> OUTPUT wrapperReplyMessage(T message, String content) {
        ClickEvent clickEvent = (ClickEvent) message;

        TextReplyMessage replyMessage = new TextReplyMessage();
        replyMessage.setContent(content);
        replyMessage.setToUserName(clickEvent.getFromUserName());
        replyMessage.setFromUserName(clickEvent.getToUserName());
        return (OUTPUT) replyMessage;
    }


    public <T> void process(T message, WeiXinContext context) {
        WeiXinSession session = context.getWeiXinSession();
        if (session == null) {
            throw new WeiXinException("没有找到该消息对应的会话");
        }
        //对于click事件，菜单Id就是content
        String menuId = getContent(message);
        try {
            CommandExecutor executor = getMenuConfigManager().getCommandExecutor(menuId, "", context);
            CommandResult result = executor.execute(context);
            if (result != null) {
                context.setOutput(wrapperReplyMessage(message, result.getMessage()));
                session.setParameter(MENU_ID_NAME, result.getMenuId());
                getWeiXinSessionManager().addWeiXinSession(session);
            }

        } catch (Exception e) {
            throw new WeiXinException("菜单信息处理器发生异常", e);
        }

    }

    protected <T> String getContent(T message) {
        ClickEvent clickEvent = (ClickEvent) message;
        return clickEvent.getEventKey();
    }

    protected boolean isMatchMessage(String content, WeiXinContext context) {
        //这里消息内容就是菜单Id
        return (content != null && getMenuConfigManager().getCommandExecutor(content, "", context) != null) ? true : false;
    }

}
