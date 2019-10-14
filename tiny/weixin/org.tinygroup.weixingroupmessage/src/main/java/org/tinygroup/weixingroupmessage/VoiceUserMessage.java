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
import org.tinygroup.weixingroupmessage.item.VoiceJsonItem;

/**
 * OpenId列表群发语音消息
 *
 * @author yancheng11334
 */
public class VoiceUserMessage extends UserGroupMessage {

    @JSONField(name = "voice")
    private VoiceJsonItem voiceJsonItem;

    public VoiceUserMessage() {
        this(null);
    }

    public VoiceUserMessage(String mediaId) {
        setMsgType("voice");
        voiceJsonItem = new VoiceJsonItem(mediaId);
    }

    public VoiceJsonItem getVoiceJsonItem() {
        return voiceJsonItem;
    }

    public void setVoiceJsonItem(VoiceJsonItem voiceJsonItem) {
        this.voiceJsonItem = voiceJsonItem;
    }

    public String toString() {
        ObjectToJson<VoiceUserMessage> json = new ObjectToJson<VoiceUserMessage>();
        return json.convert(this);
    }
}
