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
package org.tinygroup.weixin.util;

/**
 * 微信消息枚举类
 *
 * @author yancheng11334
 */
public enum WeiXinMessageMode {

    /**
     * 文本消息
     */
    TEXT("text"),
    /**
     * 图像消息
     */
    IMAGE("image"),
    /**
     * 语音消息
     */
    VOICE("voice"),
    /**
     * 视频消息
     */
    VIDEO("video"),
    /**
     * 小视频消息
     */
    SHORT_VIDEO("shortvideo"),
    /**
     * 地理位置消息
     */
    LOCATION("location"),
    /**
     * 链接消息
     */
    LINK("link"),
    /**
     * 音乐消息
     */
    MUSIC("music"),
    /**
     * 多图文消息
     */
    NEWS("news"),
    /**
     * 群发消息中的图文消息
     */
    MP_NEWS("mpnews"),
    /**
     * 群发消息中的视频消息
     */
    MP_VIDEO("mpvideo");


    private final String type;

    private WeiXinMessageMode(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
