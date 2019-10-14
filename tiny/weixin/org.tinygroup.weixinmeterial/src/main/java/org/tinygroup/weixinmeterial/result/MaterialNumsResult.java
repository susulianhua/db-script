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

/**
 * 永久素材总数
 *
 * @author yancheng11334
 */
public class MaterialNumsResult implements ToServerResult {

    private int voiceNums;

    private int imageNums;

    private int newsNums;

    private int videoNums;

    @JSONField(name = "voice_count")
    public int getVoiceNums() {
        return voiceNums;
    }

    @JSONField(name = "voice_count")
    public void setVoiceNums(int voiceNums) {
        this.voiceNums = voiceNums;
    }

    @JSONField(name = "image_count")
    public int getImageNums() {
        return imageNums;
    }

    @JSONField(name = "image_count")
    public void setImageNums(int imageNums) {
        this.imageNums = imageNums;
    }

    @JSONField(name = "news_count")
    public int getNewsNums() {
        return newsNums;
    }

    @JSONField(name = "news_count")
    public void setNewsNums(int newsNums) {
        this.newsNums = newsNums;
    }

    @JSONField(name = "video_count")
    public int getVideoNums() {
        return videoNums;
    }

    @JSONField(name = "video_count")
    public void setVideoNums(int videoNums) {
        this.videoNums = videoNums;
    }

}
