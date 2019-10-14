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
import org.tinygroup.weixin.common.FromServerMessage;

/**
 * 加密消息
 *
 * @author yancheng11334
 */
@XStreamAlias("xml")
public class EncryptMessage implements FromServerMessage {

    @XStreamAlias("Encrypt")
    private String encrypt;

    @XStreamAlias("ToUserName")
    private String toUserName;

    public String getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(String encrypt) {
        this.encrypt = encrypt;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

}
