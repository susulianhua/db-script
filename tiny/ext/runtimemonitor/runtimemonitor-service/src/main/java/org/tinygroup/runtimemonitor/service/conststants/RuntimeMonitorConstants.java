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
package org.tinygroup.runtimemonitor.service.conststants;

/**
 *
 * @author zhangliang08072
 * @version $Id: RuntimeMonitorConstants.java, v 0.1 2017年1月1日 下午4:10:17 zhangliang08072 Exp $
 */
public final class RuntimeMonitorConstants {

    public final static String RUNTIME_BUSINESSTYPE_TYNYSERVICE = "tinyservice";//运行时间业务类型之tiny服务。


    public final static String RUNTIME_TYNYSERVICE_SUBTYPE_LOCAL = "local";//tiny服务子类型之本地服务
    public final static String RUNTIME_TYNYSERVICE_SUBTYPE_REMOTE = "remote";//tiny服务子类型之远程服务

    public final static String RUNTIME__RUNRESULT_SUCCESS = "success";//运行结果之成功
    public final static String RUNTIME__RUNRESULT_FAILURE = "failure";//运行结果之失败
    public final static String RUNTIME__RUNRESULT_TIMEOUT = "timeout";//运行结果之超时
}
