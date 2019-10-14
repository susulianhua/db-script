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
import org.tinygroup.weixingroupmessage.item.VideoJsonItem;

public class VideoUserMessage extends UserGroupMessage {

    @JSONField(name = "mpvideo")
    private VideoJsonItem videoJsonItem;

    public VideoUserMessage() {
        this(null);
    }

    public VideoUserMessage(String mediaId) {
        setMsgType("mpvideo");
        videoJsonItem = new VideoJsonItem(mediaId);
    }

    public VideoJsonItem getVideoJsonItem() {
        return videoJsonItem;
    }

    public void setVideoJsonItem(VideoJsonItem videoJsonItem) {
        this.videoJsonItem = videoJsonItem;
    }

    public String toString() {
        ObjectToJson<VideoUserMessage> json = new ObjectToJson<VideoUserMessage>();
        return json.convert(this);
    }

}
