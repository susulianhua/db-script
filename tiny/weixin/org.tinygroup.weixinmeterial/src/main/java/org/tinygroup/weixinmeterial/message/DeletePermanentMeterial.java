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
import org.tinygroup.convert.objectjson.fastjson.ObjectToJson;
import org.tinygroup.weixin.common.ToServerMessage;

/**
 * 删除永久素材(不支持临时素材)
 *
 * @author yancheng11334
 */
public class DeletePermanentMeterial implements ToServerMessage {

    @JSONField(name = "media_id")
    private String mediaId;

    public DeletePermanentMeterial() {

    }

    public DeletePermanentMeterial(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String toString() {
        ObjectToJson<DeletePermanentMeterial> json = new ObjectToJson<DeletePermanentMeterial>();
        return json.convert(this);
    }

    @JSONField(serialize = false)
    public String getWeiXinKey() {
        return "delMaterial";
    }
}
