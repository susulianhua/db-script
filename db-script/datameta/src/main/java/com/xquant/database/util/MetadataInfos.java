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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangwy11342 on 2016/8/9.
 */
public class MetadataInfos {
    private static Map<String, Integer> metadataMap = new HashMap<String, Integer>();

    public static Map<String, Integer> getInfos() {
        return metadataMap;
    }

    public static void resetInfo(String key, Integer value) {
        if (metadataMap.containsKey(key)) {
            metadataMap.put(key, value);
        }
    }

    public static void initMetadataMap(Map<String, Integer> map) {
        metadataMap = map;
    }


}
