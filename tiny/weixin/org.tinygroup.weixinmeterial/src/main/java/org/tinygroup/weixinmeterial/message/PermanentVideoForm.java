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

import org.tinygroup.convert.objectjson.fastjson.ObjectToJson;

/**
 * 上传视频表单
 *
 * @author yancheng11334
 */
public class PermanentVideoForm {

    private String title;

    private String introduction;

    public PermanentVideoForm() {

    }

    public PermanentVideoForm(String title, String introduction) {
        this.title = title;
        this.introduction = introduction;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String toString() {
        ObjectToJson<PermanentVideoForm> json = new ObjectToJson<PermanentVideoForm>();
        return json.convert(this);
    }
}
