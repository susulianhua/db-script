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
package org.tinygroup.weixinhttp.cert;

/**
 * 微信证书配置
 *
 * @author yancheng11334
 */
public class WeiXinCert {

    /**
     * 证书路径
     */
    private String certPath;

    /**
     * 验证密码
     */
    private String password;

    /**
     * 证书类型
     */
    private String certType;

    public WeiXinCert() {
        this(null, null);
    }

    public WeiXinCert(String certPath, String password) {
        this(certPath, password, "PKCS12");
    }

    public WeiXinCert(String certPath, String password, String certType) {
        this.certPath = certPath;
        this.password = password;
        this.certType = certType;
    }

    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    @Override
    public String toString() {
        return "WeiXinCert [certPath=" + certPath + ", password=" + password
                + ", certType=" + certType + "]";
    }

}
