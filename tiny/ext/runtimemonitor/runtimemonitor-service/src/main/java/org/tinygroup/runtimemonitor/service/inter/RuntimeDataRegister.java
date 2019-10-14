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
package org.tinygroup.runtimemonitor.service.inter;

import java.util.Map;

/**
 * 运行时间信息登记
 * @author zhangliang08072
 * @version $Id: RuntimeDataRegister.java, v 0.1 2016年12月29日 上午12:01:08 zhangliang08072 Exp $
 */
public interface RuntimeDataRegister {

    /**
     * 某次操作运行之前
     * @param businessType 业务类型，如tiny服务或者java方法
     * @param businessId
     * @param runtimeId
     * @param subType
     */
    void beforeExecute(String businessType, String businessId, String runtimeId, String subType, Map<String, Object> itemMap);

    /**
     * 某次操作成功运行之后
     * @param businessType
     * @param businessId
     * @param runtimeId
     * @param subType
     */
    void successExecute(String businessType, String businessId, String runtimeId, String subType, Map<String, Object> itemMap);

    /**
     * 设置某次操作超时
     * @param businessType
     * @param businessId
     * @param runtimeId
     * @param subType
     */
    void timeoutExecute(String businessType, String businessId, String runtimeId, String subType, Map<String, Object> itemMap);

    /**
     * 某次操作执行失败
     * @param businessType
     * @param businessId
     * @param runtimeId
     * @param subType
     * @param e
     */
    void failExecute(String businessType, String businessId, String runtimeId, String subType, Exception e, Map<String, Object> itemMap);
}
