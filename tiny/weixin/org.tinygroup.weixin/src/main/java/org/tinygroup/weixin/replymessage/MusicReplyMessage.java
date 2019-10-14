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
package org.tinygroup.weixin.replymessage;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 音乐回复消息
 *
 * @author yancheng11334
 */
@XStreamAlias("xml")
public class MusicReplyMessage extends CommonReplyMessage {

    @XStreamAlias("Music")
    private MusicItem music;

    public MusicReplyMessage() {
        setMsgType("music");
    }

    public MusicItem getMusic() {
        return music;
    }

    public void setMusic(MusicItem music) {
        this.music = music;
    }

    public String toString() {
        XStream xstream = new XStream();
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.processAnnotations(getClass());
        return xstream.toXML(this);
    }
}
