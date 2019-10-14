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
import org.tinygroup.weixin.common.ToServerMessage;
import org.tinygroup.weixin.result.AccessToken;
import org.tinygroup.weixin.result.JsApiTicket;
import org.tinygroup.weixinhttp.WeiXinHttpUpload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 微信连接统一管理
 *
 * @author yancheng11334
 */
public interface WeiXinConnector {

    /**
     * 默认的bean配置名称
     */
    public static final String DEFAULT_BEAN_NAME = "weiXinConnector";

    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";

    /**
     * 获取当前的管理号客户端信息
     *
     * @return
     */
    Client getClient();

    /**
     * 获得微信消息发送者，负责往微信服务器发送消息
     *
     * @return
     */
    WeiXinSender getWeiXinSender();

    /**
     * 获得微信消息接收者，负责解析微信服务器推送过来的消息
     *
     * @return
     */
    WeiXinReceiver getWeiXinReceiver();

    /**
     * 获取微信的会话管理者
     *
     * @return
     */
    WeiXinSessionManager getWeiXinSessionManager();

    /**
     * 获取微信验证令牌
     *
     * @return
     */
    AccessToken getAccessToken();

    /**
     * 获得微信的JS访问票据
     *
     * @return
     */
    JsApiTicket getJsApiTicket();

    /**
     * 发送微信消息
     *
     * @param message
     */
    void send(ToServerMessage message);

    /**
     * 上传微信文件
     *
     * @param upload
     */
    void upload(WeiXinHttpUpload upload);

    /**
     * 接收微信消息
     *
     * @param request
     * @param response
     */
    void receive(HttpServletRequest request, HttpServletResponse response);

}
