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
package com.xquant.metadata.exception;

/**
 * Created by wangwy11342 on 2016/6/8.
 */
public class MetadataErrorCode {
    public static final String ERROR_CODE_PREFIX = "0TE120061";

    //标准类型被重复添加
    public static final String STDTYPE_ADD_ALREADY_ERROR = ERROR_CODE_PREFIX + "001";

    //标准类型不存在
    public static final String STDTYPE_NOT_EXISTS_ERROR = ERROR_CODE_PREFIX + "002";

    //业务类型被重复添加
    public static final String BIZTYPE_ADD_ALREADY_ERROR = ERROR_CODE_PREFIX + "011";
    //业务类型不存在
    public static final String BIZTYPE_NOT_EXISTS_ERROR = ERROR_CODE_PREFIX + "012";
    //业务类型对应语言类型不存在
    public static final String BIZTYPE_LANGUAGE_TYPE_NOT_EXISTS_ERROR = ERROR_CODE_PREFIX + "013";

    //标准字段被重复添加
    public static final String STDFIELD_ADD_ALREADY_ERROR = ERROR_CODE_PREFIX + "021";
    //标准字段不存在
    public static final String STDFIELD_NOT_EXISTS_ERROR = ERROR_CODE_PREFIX + "022";
    //标准字段对应语言类型不存在
    public static final String STDFIELD_LANGUAGE_TYPE_NOT_EXISTS_ERROR = ERROR_CODE_PREFIX + "023";

    //默认值被重复添加
    public static final String DEFAULTVALUE_ADD_ALREADY_ERROR = ERROR_CODE_PREFIX + "031";

    //默认值不存在
    public static final String DEFAULTVALUE_NOT_EXISTS_ERROR = ERROR_CODE_PREFIX + "032";

    //方言类型被重复添加
    public static final String LANGUAGE_ADD_ALREADY_ERROR = ERROR_CODE_PREFIX + "041";
    //方言类型不存在
    public static final String LANGUAGE_NOT_EXISTS_ERROR = ERROR_CODE_PREFIX + "042";

    //方言字段不存在
    public static final String LANGUAGE_FIELD_NOT_EXISTS_ERROR = ERROR_CODE_PREFIX + "043";

    //数据字典被重复添加
    public static final String DICT_ADD_ALREADY_ERROR = ERROR_CODE_PREFIX + "051";
    //数据字典不存在
    public static final String DICT_NOT_EXISTS_ERROR = ERROR_CODE_PREFIX + "052";


}
