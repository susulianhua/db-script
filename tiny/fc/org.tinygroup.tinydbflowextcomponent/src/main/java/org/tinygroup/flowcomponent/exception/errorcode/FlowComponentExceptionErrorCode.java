/**
 * Copyright (c) 2012-2016, www.tinygroup.org (luo_guo@icloud.com).
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
package org.tinygroup.flowcomponent.exception.errorcode;

public class FlowComponentExceptionErrorCode {
    /**
     * 对象实例化失败
     */
    public static final String CLASS_INSTANTIATION_FAILED = "0TE12" + "0123" + "005";
    /**
     * 未找到指定的类
     */
    public static final String CLASS_NOT_FOUND = "0TE12" + "0123" + "006";
    /**
     * 数据库更新失败
     */
    public static final String DB_UPDATE_FAILED = "0TE12" + "0123" + "011";

    /**
     * 流水号生成失败
     */
    public static final String SERIANUM_CREATE_FAILED = "0TE12" + "0123" + "012";

}
