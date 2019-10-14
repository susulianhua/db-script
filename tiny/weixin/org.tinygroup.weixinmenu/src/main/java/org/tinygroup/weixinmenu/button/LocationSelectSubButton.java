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
 * location_select：弹出地理位置选择器。用户点击按钮后，微信客户端将调起地理位置选择工具，完成选择操作后，将选择的地理位置发送给开发者的服务器，同时收起位置选择工具，随后可能会收到开发者下发的消息。
 *
 * @author yancheng11334
 */
public class LocationSelectSubButton extends CommonSubButton {

    private String key;

    public LocationSelectSubButton() {
        this(null, null);
    }

    public LocationSelectSubButton(String name) {
        this(name, null);
    }

    public LocationSelectSubButton(String name, String key) {
        super(name, WeiXinEventMode.LOCATION_SELECT.getEvent());
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

