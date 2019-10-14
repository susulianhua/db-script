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

import org.tinygroup.weixin.common.Client;
import org.tinygroup.weixin.common.GetTicket;
import org.tinygroup.weixin.common.ToServerMessage;
import org.tinygroup.weixin.result.AccessToken;
import org.tinygroup.weixin.result.JsApiTicket;
import org.tinygroup.weixinhttp.WeiXinHttpConnector;
import org.tinygroup.weixinhttp.WeiXinHttpUpload;

import java.util.List;

/**
 * 负责处理微信的发送请求
 *
 * @author yancheng11334
 */
public interface WeiXinSender {

    /**
     * 默认的bean配置名称
     */
    public static final String DEFAULT_BEAN_NAME = "weiXinSender";
    public static final String CONNECTOR_URL_KEY = "token";

    WeiXinManager getWeiXinManager();

    void setWeiXinManager(WeiXinManager manager);

    /**
     * HTTP协议操作者
     *
     * @return
     */
    WeiXinHttpConnector getWeiXinHttpConnector();

    List<WeiXinHandler> getMatchWeiXinHandlers(WeiXinHandlerMode mode);

    /**
     * 批量注册Handler
     *
     * @param sendHandlerList
     */
    void setSendHandlerList(List<WeiXinHandler> sendHandlerList);

    /**
     * 发送微信消息
     *
     * @param message
     */
    void send(ToServerMessage message, WeiXinContext context);

    /**
     * 上传微信文件
     *
     * @param upload
     */
    void upload(WeiXinHttpUpload upload, WeiXinContext context);

    /**
     * 取得微信认证
     *
     * @param client
     */
    AccessToken connect(Client client);

    /**
     * 微信的js临时票据
     *
     * @param ticket
     * @return
     */
    JsApiTicket getJsApiTicket(GetTicket ticket);
}
