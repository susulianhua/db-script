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
package org.tinygroup.remoteconfig.config;

import java.io.Serializable;

/**
 * 客户端配置的配置路径 根据本对象信息可以定位到一组k-v
 *
 * @author chenjiao
 */
public class ConfigPath implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 8247326389867349390L;
    /**
     * 整个产品的名字,例:tiny
     */
    private String productName;
    /**
     * 当前的版本,例:v2.0.33
     */
    private String versionName;
    /**
     * 当前的环境
     */
    private String environmentName;
    /**
     * 当前模块(moduleId)全路径(不包含产品名),父子模块之间以/分割,例:productModuleId/
     * productManagerModuleId
     */
    private String modulePath;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getEnvironmentName() {
        return environmentName;
    }

    public void setEnvironmentName(String environmentName) {
        this.environmentName = environmentName;
    }

    public String getModulePath() {
        return modulePath;
    }

    public void setModulePath(String modulePath) {
        this.modulePath = modulePath;
    }

    @Override
    public String toString() {
        return String.format("[app=%s] ,[version=%s] ,[env=%s] ,[modulepath=%s]", getProductName(), getVersionName(), getEnvironmentName(), getModulePath());
    }

}
