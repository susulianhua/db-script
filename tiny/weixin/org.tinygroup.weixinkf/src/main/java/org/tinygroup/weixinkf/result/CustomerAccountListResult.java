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
package org.tinygroup.weixinkf.result;

import com.alibaba.fastjson.annotation.JSONField;
import org.tinygroup.weixin.common.ToServerResult;
import org.tinygroup.weixinkf.kfaccount.CustomerServiceAccount;

import java.util.List;

/**
 * 获得全部客服的列表结果
 *
 * @author yancheng11334
 */
public class CustomerAccountListResult implements ToServerResult {

    @JSONField(name = "kf_list")
    private List<CustomerServiceAccount> customerServiceAccountList;

    public List<CustomerServiceAccount> getCustomerServiceAccountList() {
        return customerServiceAccountList;
    }

    public void setCustomerServiceAccountList(
            List<CustomerServiceAccount> customerServiceAccountList) {
        this.customerServiceAccountList = customerServiceAccountList;
    }
}
