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
package org.tinygroup.weixin.message;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.tinygroup.weixin.util.WeiXinMessageMode;

/**
 * 语音消息
 *
 * @author yancheng11334
 */
@XStreamAlias("xml")
public class VoiceMessage extends CommonMessage {

    @XStreamAlias("MediaId")
    private String mediaId;
    @XStreamAlias("Format")
    private String format;
    @XStreamAlias("Recognition")
    private String recognition;

    public VoiceMessage() {
        setMsgType(WeiXinMessageMode.VOICE.getType());
    }

    public String getRecognition() {
        return recognition;
    }

    public void setRecognition(String recognition) {
        this.recognition = recognition;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

}
