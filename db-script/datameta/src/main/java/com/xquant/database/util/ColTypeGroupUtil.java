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
package com.xquant.database.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类型分组
 * Created by wangwy11342 on 2016/8/4.
 */
public class ColTypeGroupUtil {
    private static Map<String, String> transTypeMap = new HashMap<String, String>();

    private static List<String> dbNoLengthTypes = new ArrayList<String>();

    static {
        transTypeMap.put("INTEGER", "NUMBER");

        dbNoLengthTypes.add("NUMBER");
        dbNoLengthTypes.add("INTEGER");
        dbNoLengthTypes.add("INT");
 /*
        dbNoLengthTypes.add("TINYINT");*/
    }

    public static String getSpecialType(String typeName) {
        return transTypeMap.get(typeName);
    }

    public static boolean isNumberType(String typeName) {
        return dbNoLengthTypes.contains(typeName);
    }
}
