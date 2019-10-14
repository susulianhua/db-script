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
package org.tinygroup.weixingroupmessage;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * OpenId列表群发消息
 *
 * @author yancheng11334
 */
public class UserGroupMessage extends CommonGroupMessage {

    @JSONField(name = "touser")
    private List<String> openIds;

    public List<String> getOpenIds() {
        if (openIds == null) {
            openIds = new ArrayList<String>();
        }
        return openIds;
    }

    public void setOpenIds(List<String> openIds) {
        this.openIds = openIds;
    }

    @JSONField(serialize = false)
    public String getWeiXinKey() {
        return "sendByOpenId";
    }
}
