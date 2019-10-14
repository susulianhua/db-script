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
package org.tinygroup.jedis.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.tinygroup.commons.cryptor.DefaultCryptor;
import org.tinygroup.commons.tools.StringUtil;

/**
 * 定义密码认证相关参数
 *
 * @author yancheng11334
 */
@XStreamAlias("base-auth-config")
public class BaseAuthConfig {

    private static final String TRUE = "true";

    /**
     * 连接密码，有密码填写
     */
    @XStreamAsAttribute
    private String password;

    /**
     * 是否密文
     */
    @XStreamAsAttribute
    private String cipher;

    /**
     * 算法种子
     */
    @XStreamAsAttribute
    private String seed;

    public String getPassword() {
        if (TRUE.equals(cipher) && !StringUtil.isEmpty(password)) {
            try {
                return new DefaultCryptor().decrypt(password, seed);
            } catch (Exception e) {
                throw new RuntimeException(String.format("密文[%s],算法种子[%s]的解密过程失败", password, seed), e);
            }
        }
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCipher() {
        return cipher;
    }

    public void setCipher(String cipher) {
        this.cipher = cipher;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

}
