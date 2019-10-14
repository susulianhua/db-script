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
package org.tinygroup.weixinmenu.message;

import com.alibaba.fastjson.annotation.JSONField;
import org.tinygroup.convert.objectjson.fastjson.ObjectToJson;
import org.tinygroup.weixin.common.ToServerMessage;
import org.tinygroup.weixinmenu.button.CommonButton;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建微信自定义菜单
 *
 * @author yancheng11334
 */
public class CreateMenu implements ToServerMessage {

    @JSONField(name = "button")
    private List<CommonButton> buttons;

    public List<CommonButton> getButtons() {
        if (buttons == null) {
            buttons = new ArrayList<CommonButton>();
        }
        return buttons;
    }

    public void setButtons(List<CommonButton> buttons) {
        this.buttons = buttons;
    }

    public void addButton(CommonButton button) {
        getButtons().add(button);
    }

    public void removeButton(CommonButton button) {
        getButtons().remove(button);
    }

    public String toString() {
        ObjectToJson<CreateMenu> json = new ObjectToJson<CreateMenu>();
        return json.convert(this);
    }

    @JSONField(serialize = false)
    public String getWeiXinKey() {
        return "createMenu";
    }
}
