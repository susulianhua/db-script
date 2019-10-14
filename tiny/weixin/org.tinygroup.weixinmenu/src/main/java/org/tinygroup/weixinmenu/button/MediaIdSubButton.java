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
package org.tinygroup.weixinmenu.button;

import com.alibaba.fastjson.annotation.JSONField;
import org.tinygroup.weixin.util.WeiXinEventMode;

/**
 * media_id：下发消息（除文本消息）。用户点击media_id类型按钮后，微信服务器会将开发者填写的永久素材id对应的素材下发给用户，永久素材类型可以是图片、音频、视频、图文消息。请注意：永久素材id必须是在“素材管理/新增永久素材”接口上传后获得的合法id。
 *
 * @author yancheng11334
 */
public class MediaIdSubButton extends CommonSubButton {

    @JSONField(name = "media_id")
    private String mediaId;

    public MediaIdSubButton() {
        this(null, null);
    }

    public MediaIdSubButton(String name) {
        this(name, null);
    }

    public MediaIdSubButton(String name, String mediaId) {
        super(name, WeiXinEventMode.MEDIA_ID.getEvent());
        this.mediaId = mediaId;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }


}

