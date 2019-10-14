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
package org.tinygroup.xmlsignature.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * XML数字签名配置项
 *
 * @author yancheng11334
 */
@XStreamAlias("xml-signature")
public class XmlSignatureConfig {

    //使用者ID
    @XStreamAlias("user-id")
    @XStreamAsAttribute
    private String userId;

    //私钥文件路径
    @XStreamAlias("private-key-path")
    private String privateKeyPath;

    //私钥存储类型
    @XStreamAlias("private-store-type")
    private String privateStoreType;

    //公钥证书路径
    @XStreamAlias("public-key-path")
    private String publicKeyPath;

    //公钥存储类型
    @XStreamAlias("public-store-type")
    private String publicStoreType;

    //别名
    private String alias;

    //密码
    private String password;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPrivateKeyPath() {
        return privateKeyPath;
    }

    public void setPrivateKeyPath(String privateKeyPath) {
        this.privateKeyPath = privateKeyPath;
    }

    public String getPublicKeyPath() {
        return publicKeyPath;
    }

    public void setPublicKeyPath(String publicKeyPath) {
        this.publicKeyPath = publicKeyPath;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrivateStoreType() {
        return privateStoreType;
    }

    public void setPrivateStoreType(String privateStoreType) {
        this.privateStoreType = privateStoreType;
    }

    public String getPublicStoreType() {
        return publicStoreType;
    }

    public void setPublicStoreType(String publicStoreType) {
        this.publicStoreType = publicStoreType;
    }


    public String toString() {
        return "XmlSignatureConfig [userId=" + userId + ", privateKeyPath="
                + privateKeyPath + ", privateStoreType=" + privateStoreType
                + ", publicKeyPath=" + publicKeyPath + ", publicStoreType="
                + publicStoreType + ", alias=" + alias + ", password="
                + password + "]";
    }

}
