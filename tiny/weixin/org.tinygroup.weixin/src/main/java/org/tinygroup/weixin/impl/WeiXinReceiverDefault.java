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
package org.tinygroup.weixin.impl;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.weixin.WeiXinContext;
import org.tinygroup.weixin.WeiXinHandler;
import org.tinygroup.weixin.WeiXinHandlerMode;
import org.tinygroup.weixin.WeiXinReceiver;
import org.tinygroup.weixin.exception.RepeatMessageException;

import java.util.ArrayList;
import java.util.List;

public class WeiXinReceiverDefault implements WeiXinReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeiXinReceiverDefault.class);

    private List<WeiXinHandler> receiverHandlerList = new ArrayList<WeiXinHandler>();

    public List<WeiXinHandler> getMatchWeiXinHandlers(WeiXinHandlerMode mode) {
        List<WeiXinHandler> list = new ArrayList<WeiXinHandler>();
        for (WeiXinHandler handler : receiverHandlerList) {
            if (handler.getWeiXinHandlerMode() == mode) {
                list.add(handler);
            }
        }
        if (list.size() > 0) {
            java.util.Collections.sort(list);
        }
        return list;
    }

    public void setReceiverHandlerList(List<WeiXinHandler> receiverHandlerList) {
        this.receiverHandlerList = receiverHandlerList;
        java.util.Collections.sort(this.receiverHandlerList);
    }

    public void receive(WeiXinContext context) {
        List<WeiXinHandler> dealHandlers = getMatchWeiXinHandlers(WeiXinHandlerMode.RECEIVE);
        try {
            for (WeiXinHandler handler : dealHandlers) {
                LOGGER.logMessage(LogLevel.DEBUG, "{0}开始匹配消息", handler.getClass().getName());
                if (handler.isMatch(context.getInput(), context)) {
                    LOGGER.logMessage(LogLevel.DEBUG, "{0}开始处理消息", handler.getClass().getName());
                    handler.process(context.getInput(), context);
                    LOGGER.logMessage(LogLevel.DEBUG, "{0}处理消息结束", handler.getClass().getName());
                }
                LOGGER.logMessage(LogLevel.DEBUG, "{0}匹配消息结束", handler.getClass().getName());
            }
        } catch (RepeatMessageException e) {
            //重复消息不用处理
        }

    }

}
