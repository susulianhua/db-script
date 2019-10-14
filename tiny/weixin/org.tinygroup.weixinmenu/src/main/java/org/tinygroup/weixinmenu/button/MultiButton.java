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

import java.util.ArrayList;
import java.util.List;

/**
 * 包含子菜单的一级菜单
 *
 * @author yancheng11334
 */
public class MultiButton extends CommonButton {

    @JSONField(name = "sub_button")
    private List<CommonSubButton> subButtons;

    public MultiButton() {
        super();
    }

    public MultiButton(String name) {
        super(name);
    }

    public List<CommonSubButton> getSubButtons() {
        if (subButtons == null) {
            subButtons = new ArrayList<CommonSubButton>();
        }
        return subButtons;
    }

    public void setSubButtons(List<CommonSubButton> subButtons) {
        this.subButtons = subButtons;
    }

    public void addSubButton(CommonSubButton subButton) {
        getSubButtons().add(subButton);
    }

    public void removeSubButton(CommonSubButton subButton) {
        getSubButtons().remove(subButton);
    }
}
