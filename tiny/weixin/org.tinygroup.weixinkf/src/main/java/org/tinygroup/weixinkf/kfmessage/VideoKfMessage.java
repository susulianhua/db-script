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
package org.tinygroup.weixinkf.kfmessage;

import com.alibaba.fastjson.annotation.JSONField;
import org.tinygroup.convert.objectjson.fastjson.ObjectToJson;
import org.tinygroup.weixinkf.kfmessage.item.VideoJsonItem;

/**
 * 客服视频消息Json包装
 *
 * @author yancheng11334
 */
public class VideoKfMessage extends CommonKfMessage {

    @JSONField(name = "video")
    private VideoJsonItem videoJsonItem;

    public VideoKfMessage() {
        setMsgType("video");
    }

    public VideoJsonItem getVideoJsonItem() {
        return videoJsonItem;
    }

    public void setVideoJsonItem(VideoJsonItem videoJsonItem) {
        this.videoJsonItem = videoJsonItem;
    }

    public String toString() {
        ObjectToJson<VideoKfMessage> json = new ObjectToJson<VideoKfMessage>();
        return json.convert(this);
    }
}
