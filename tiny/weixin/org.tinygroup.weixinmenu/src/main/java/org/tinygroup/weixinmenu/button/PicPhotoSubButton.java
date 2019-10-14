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

import org.tinygroup.weixin.util.WeiXinEventMode;

/**
 * pic_photo_or_album：弹出拍照或者相册发图。用户点击按钮后，微信客户端将弹出选择器供用户选择“拍照”或者“从手机相册选择”。用户选择后即走其他两种流程。
 *
 * @author yancheng11334
 */
public class PicPhotoSubButton extends CommonSubButton {

    private String key;

    public PicPhotoSubButton() {
        this(null, null);
    }

    public PicPhotoSubButton(String name) {
        this(name, null);
    }

    public PicPhotoSubButton(String name, String key) {
        super(name, WeiXinEventMode.PIC_PHOTO_OR_ALBUM.getEvent());
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

