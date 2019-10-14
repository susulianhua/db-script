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
 * view_limited：跳转图文消息URL。用户点击view_limited类型按钮后，微信客户端将打开开发者在按钮中填写的永久素材id对应的图文消息URL，永久素材类型只支持图文消息。请注意：永久素材id必须是在“素材管理/新增永久素材”接口上传后获得的合法id。
 *
 * @author yancheng11334
 */
public class ViewLimitSubButton extends CommonSubButton {

    @JSONField(name = "media_id")
    private String mediaId;

    public ViewLimitSubButton() {
        this(null, null);
    }

    public ViewLimitSubButton(String name) {
        this(name, null);
    }

    public ViewLimitSubButton(String name, String mediaId) {
        super(name, WeiXinEventMode.VIEW_LIMITED.getEvent());
        this.mediaId = mediaId;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
}
