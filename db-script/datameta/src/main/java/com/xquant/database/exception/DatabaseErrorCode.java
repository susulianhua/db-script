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
package com.xquant.database.exception;

/**
 * Created by wangwy11342 on 2016/6/8.
 */
public class DatabaseErrorCode {
    public static final String ERROR_CODE_PREFIX = "0TE120055";

    //表格被重复添加
    public static final String TABLE__ADD_ALREADY_ERROR = ERROR_CODE_PREFIX + "001";

    //视图被重复添加
    public static final String VIEW__ADD_ALREADY_ERROR = ERROR_CODE_PREFIX + "002";

    //表空间被重复添加
    public static final String TABLESPACE__ADD_ALREADY_ERROR = ERROR_CODE_PREFIX + "003";

    //初始数据被重复添加
    public static final String INIT_DATA__ADD_ALREADY_ERROR = ERROR_CODE_PREFIX + "004";

    //存储过程被重复添加
    public static final String PROCEDURE__ADD_ALREADY_ERROR = ERROR_CODE_PREFIX + "005";

}
