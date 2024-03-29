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
package org.tinygroup.weixinmeterial.message;

import com.alibaba.fastjson.annotation.JSONField;
import org.tinygroup.weixin.common.ToServerMessage;

/**
 * 获取图文列表消息对象
 *
 * @author yancheng11334
 */
public class GetNewsListMessage implements ToServerMessage {

    private String type;

    //从全部素材的该偏移位置开始返回，0表示从第一个素材 返回
    private int offset;

    //返回素材的数量，取值在1到20之间
    private int count;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @JSONField(serialize = false)
    public String getWeiXinKey() {
        return "getNewsList";
    }
}
