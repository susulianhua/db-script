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
package org.tinygroup.weixinmeterial.result;

import com.alibaba.fastjson.annotation.JSONField;
import org.tinygroup.weixin.common.ToServerResult;

import java.util.List;

/**
 * 视频、图片、语音列表请求结果
 *
 * @author yancheng11334
 */
public class OtherListResult implements ToServerResult {

    //总记录数
    private int totalNum;

    //本批记录数
    private int itemNum;

    private List<OtherListItem> items;


    @JSONField(name = "item")
    public List<OtherListItem> getItems() {
        return items;
    }

    @JSONField(name = "item")
    public void setItems(List<OtherListItem> items) {
        this.items = items;
    }

    @JSONField(name = "total_count")
    public int getTotalNum() {
        return totalNum;
    }

    @JSONField(name = "total_count")
    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    @JSONField(name = "item_count")
    public int getItemNum() {
        return itemNum;
    }

    @JSONField(name = "item_count")
    public void setItemNum(int itemNum) {
        this.itemNum = itemNum;
    }

}
