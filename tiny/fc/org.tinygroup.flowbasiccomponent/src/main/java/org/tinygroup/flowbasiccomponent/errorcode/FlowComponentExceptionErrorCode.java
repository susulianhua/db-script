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
package org.tinygroup.flowbasiccomponent.errorcode;

public class FlowComponentExceptionErrorCode {

    /**
     * 流程参数值为空
     */
    public static final String FLOW_PARAMETER_NULL = "0TE12" + "0123" + "001";

    /**
     * 响应码转换器未发现
     */
    public static final String RESPCODE_CONVERT_NOT_FOUND = "0TE12" + "0123" + "002";

    /**
     * 文件转对象时出错
     */
    public static final String FILE_SAVE_TO_OBJECT_FAILED = "0TE12" + "0123" + "003";

    /**
     * 对象转文件时出错
     */
    public static final String OBJECT_SAVE_TO_FILE_FAILED = "0TE12" + "0123" + "004";

    /**
     * 对象实例化失败
     */
    public static final String CLASS_INSTANTIATION_FAILED = "0TE12" + "0123" + "005";

    /**
     * 未找到指定的类
     */
    public static final String CLASS_NOT_FOUND = "0TE12" + "0123" + "006";

    /**
     * 文件未找到
     */
    public static final String FILE_NOT_FOUND = "0TE12" + "0123" + "007";

    /**
     * 文件格式化工具未找到
     */
    public static final String FILE_FORMAT_NOT_FOUND = "0TE12" + "0123" + "008";

    /**
     * 未找到指定的xml节点
     */
    public static final String XML_NODE_NOT_FOUND = "0TE12" + "0123" + "009";

    /**
     * 文件合并失败
     */
    public static final String FILE_MERGE_FAILED = "0TE12" + "0123" + "010";

    /**
     * 找不到指定的枚举转换器
     */
    public static final String ENUM_CONVERTER_NOT_FOUNT = "0TE12" + "0123" + "013";
    /**
     * 找不到指定的枚举类
     */
    public static final String ENUM_NOT_FOUND = "0TE12" + "0123" + "014";
    /**
     * 指定的枚举转换类不支持该枚举的转换
     */
    public static final String ENUM_CONVERTER_NOT_SUPPORT = "0TE12" + "0123" + "015";
    /**
     * 加密失败
     */
    public static final String ENCRYPT_FAILED = "0TE12" + "0123" + "016";
    /**
     * 解密失败
     */
    public static final String DECRYPT_FAILED = "0TE12" + "0123" + "017";
    /**
     * 编码转换失败
     */
    public static final String ENCODING_TRANSFORM_FAILED = "0TE12" + "0123" + "018";
    /**
     * ini文件属性读取失败
     */
    public static final String INI_PROPERTY_READ_FAILED = "0TE12" + "0123" + "019";
    /**
     * properties文件读取失败
     */
    public static final String PROPERTIES_FILE_READ_FAILED = "0TE12" + "0123" + "020";
    /**
     * 写监控失败
     */
    public static final String PRINT_MONITOR_FAILED = "0TE12" + "0123" + "021";
    /**
     * 文件转xmlNode失败
     */
    public static final String FILE_CONVERT_TO_XMLNODE_FAILED = "0TE12" + "0123" + "022";
    /**
     * 对象转换为XML字符串失败
     */
    public static final String OBJECT_CONVERT_TO_XML_FAILED = "0TE12" + "0123" + "023";

}
