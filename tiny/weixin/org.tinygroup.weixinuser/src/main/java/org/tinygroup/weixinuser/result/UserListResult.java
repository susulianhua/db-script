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
package org.tinygroup.weixinuser.result;

import com.alibaba.fastjson.annotation.JSONField;
import org.tinygroup.convert.objectjson.fastjson.ObjectToJson;
import org.tinygroup.weixin.common.ToServerResult;

/**
 * 获取用户列表(只包含openid)
 *
 * @author yancheng11334
 */
public class UserListResult implements ToServerResult {

    private int total;

    private int count;

    @JSONField(name = "data")
    private UserListItem userListItem;

    @JSONField(name = "next_openid")
    private String nextOpenId;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public UserListItem getUserListItem() {
        return userListItem;
    }

    public void setUserListItem(UserListItem userListItem) {
        this.userListItem = userListItem;
    }

    public String getNextOpenId() {
        return nextOpenId;
    }

    public void setNextOpenId(String nextOpenId) {
        this.nextOpenId = nextOpenId;
    }

    public String toString() {
        ObjectToJson<UserListResult> json = new ObjectToJson<UserListResult>();
        return json.convert(this);
    }

}
