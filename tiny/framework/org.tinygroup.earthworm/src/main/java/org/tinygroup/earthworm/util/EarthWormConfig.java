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
package org.tinygroup.earthworm.util;

import org.tinygroup.earthworm.EarthWorm;
import org.tinygroup.earthworm.LogService;

public class EarthWormConfig {
    // 日志输出格式类，返回字符串，该字符串用于输出格式
    private static String logSupportClass = "org.tinygroup.earthworm.BaseLogSupport";
    // 取样器处理类，(返回true/false)用于记录此次是否记录
    private static String sampleTrierClass = "";
    // SS时，若当前线程中没有rpccontext，是否自己创建一个RPCContext
    boolean ssCreateIfNotExist = false;

    public static void setLogSupportClass(String clazz) {
        EarthWormConfig.logSupportClass = clazz;
        LogService.setLogSupport(clazz);
    }

    public static void setSampleTrierClass(String clazz) {
        sampleTrierClass = clazz;
        EarthWorm.setSampleTrier(clazz);

    }
}
