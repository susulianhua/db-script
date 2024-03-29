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
import org.tinygroup.convert.objectjson.fastjson.ObjectToJson;
import org.tinygroup.weixingroupmessage.item.NewsJsonItem;

/**
 * 按分组的群发图文消息
 *
 * @author yancheng11334
 */
public class NewsFilterMessage extends FilterGroupMessage {

    @JSONField(name = "mpnews")
    private NewsJsonItem newsJsonItem;

    public NewsFilterMessage() {
        this(null);
    }

    public NewsFilterMessage(String mediaId) {
        setMsgType("mpnews");
        newsJsonItem = new NewsJsonItem(mediaId);
    }

    public NewsJsonItem getNewsJsonItem() {
        return newsJsonItem;
    }

    public void setNewsJsonItem(NewsJsonItem newsJsonItem) {
        this.newsJsonItem = newsJsonItem;
    }

    public String toString() {
        ObjectToJson<NewsFilterMessage> json = new ObjectToJson<NewsFilterMessage>();
        return json.convert(this);
    }
}
