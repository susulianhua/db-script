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
package com.xquant.configuration.util;

import java.util.Map;
import java.util.Properties;

public class VariableUtil {

    /**
     * 获取环境变量
     *
     * @param key
     * @return
     */
    public static String getEnvVariable(String key) {
        Map<String, String> map = System.getenv();
        return map.get(key);
    }

    /**
     * 判断是否存在环境变量
     *
     * @param key
     * @return
     */
    public static boolean hasEnvVariable(String key) {
        Map<String, String> map = System.getenv();
        return map.containsKey(key);
    }

    /**
     * 获取系统变量
     *
     * @param key
     * @return
     */
    public static Object getSysVariable(String key) {
        Properties p = System.getProperties();
        return p.get(key);
    }
}