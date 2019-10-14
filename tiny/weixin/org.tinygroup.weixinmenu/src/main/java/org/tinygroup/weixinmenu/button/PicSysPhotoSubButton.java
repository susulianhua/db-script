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
 * pic_sysphoto：弹出系统拍照发图。用户点击按钮后，微信客户端将调起系统相机，完成拍照操作后，会将拍摄的相片发送给开发者，并推送事件给开发者，同时收起系统相机，随后可能会收到开发者下发的消息。
 *
 * @author yancheng11334
 */
public class PicSysPhotoSubButton extends CommonSubButton {

    private String key;

    public PicSysPhotoSubButton() {
        this(null, null);
    }

    public PicSysPhotoSubButton(String name) {
        this(name, null);
    }

    public PicSysPhotoSubButton(String name, String key) {
        super(name, WeiXinEventMode.PIC_SYSPHOTO.getEvent());
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
