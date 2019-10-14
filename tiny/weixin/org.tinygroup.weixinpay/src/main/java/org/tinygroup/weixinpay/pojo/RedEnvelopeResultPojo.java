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
package org.tinygroup.weixinpay.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 基本红包业务值通用对象
 *
 * @author yancheng11334
 */
public class RedEnvelopeResultPojo extends CommunicationResultPojo {

    /**
     * 业务代码
     */
    @XStreamAlias("result_code")
    private String businessCode;

    /**
     * 错误代码
     */
    @XStreamAlias("err_code")
    private String errorCode;

    /**
     * 错误代码描述
     */
    @XStreamAlias("err_code_des")
    private String errorMsg;

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
