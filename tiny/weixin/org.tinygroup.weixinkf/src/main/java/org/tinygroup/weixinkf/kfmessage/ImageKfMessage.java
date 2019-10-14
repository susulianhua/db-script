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
import org.tinygroup.weixinkf.kfmessage.item.ImageJsonItem;

/**
 * 客服图片消息Json包装
 *
 * @author yancheng11334
 */
public class ImageKfMessage extends CommonKfMessage {

    @JSONField(name = "image")
    private ImageJsonItem imageJsonItem;

    public ImageKfMessage() {
        this(null);
    }

    public ImageKfMessage(String mediaId) {
        setMsgType("image");
        imageJsonItem = new ImageJsonItem(mediaId);
    }

    public ImageJsonItem getImageJsonItem() {
        return imageJsonItem;
    }

    public void setImageJsonItem(ImageJsonItem imageJsonItem) {
        this.imageJsonItem = imageJsonItem;
    }

    public String toString() {
        ObjectToJson<ImageKfMessage> json = new ObjectToJson<ImageKfMessage>();
        return json.convert(this);
    }

}
