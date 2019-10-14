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

/**
 * 上传永久图片、缩略图的结果
 *
 * @author yancheng11334
 */
public class PermanentUrlResult {

    private String url;

    private String mediaId;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @JSONField(name = "media_id")
    public String getMediaId() {
        return mediaId;
    }

    @JSONField(name = "media_id")
    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
}
