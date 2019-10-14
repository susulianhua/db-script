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
package org.tinygroup.flow.exception.errorcode;


public class FlowExceptionErrorCode {
    /**
     * 流程节点名称非空校验
     */
    public static final String FLOW_NODE_NAME_VALIDATE_EXCEPTION = "0TE12" + "0081" + "001";

    /**
     * 流程节点参数校验失败异常
     */
    public static final String FLOW_PROPERTY_VALIDATE_EXCEPTION = "0TE12" + "0081" + "002";

    /**
     * 未找到后续节点
     */
    public static final String FLOW_NEXT_NODE_NOT_FOUND_EXCEPTION = "0TE12" + "0081" + "003";

    /**
     * 组件名称为空
     */
    public static final String FLOW_NODE_COMPONENT_NAME_IS_NULL = "0TE12" + "0081" + "004";

    /**
     * 流程不存在
     */
    public static final String FLOW_NOT_EXIST = "0TE12" + "0081" + "005";

    /**
     * 流程节点不存在
     */
    public static final String FLOW_NODE_NOT_EXIST = "0TE12" + "0081" + "006";

    /**
     * 流程需要的输入参数不足
     */
    public static final String FLOW_IN_PARAM_NOT_EXIST = "0TE12" + "0081" + "007";

    /**
     * 流程需要的输出参数不足
     */
    public static final String FLOW_OUT_PARAM_NOT_EXIST = "0TE12" + "0081" + "008";

    /**
     * 找不到名称:{0}的校验对象
     */
    public static final String FLOW_VALIDATOR_NOT_EXIST = "0TE12" + "0081" + "009";

    /**
     * 组件找不到
     */
    public static final String FLOW_COMPONENT_NOT_EXIST = "0TE12" + "0081" + "010";
}
