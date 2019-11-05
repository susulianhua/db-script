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

import com.xquant.configuration.ConfigurationManager;
import com.xquant.configuration.impl.ConfigurationManagerImpl;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 应用配置工具类，用于把父对象中的配置参数应用到子对象中。
 *
 * @author luoguo
 */
public final class ConfigurationUtil extends ConfigurationXmlUtil {

    private static ConfigurationManager configurationManager = new ConfigurationManagerImpl();

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationUtil.class);

    private ConfigurationUtil() {
    }
    
    public static ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    public static void clear() {
        configurationManager.clear();
    }
   
    @Deprecated
    public static String replace(String content, String name, String value) {

        Pattern pattern = Pattern.compile("[{]" + name + "[}]");
        Matcher matcher = pattern.matcher(content);
        StringBuilder buf = new StringBuilder();
        int curpos = 0;
        while (matcher.find()) {
            buf.append(content.substring(curpos, matcher.start()));
            curpos = matcher.end();
            buf.append(value);
            continue;
        }
        buf.append(content.substring(curpos));
        return buf.toString();
    }

    /**
     * 交叉替换两个Map中的变量引用,不考虑多层及嵌套引用，只处理单层引用
     * 支持：
     *  Map1: key1 = {key2}  key4=a
     *  Map2: key3 = {key4}  key2=b
     *   不支持：
     *  Map1: key1 = {key3}  key4={key2}
     *  Map2: key3 = {key4}  key2=b
     * @param proMap
     * @param proMap2
     */
    public static void replace(Map<String, String> proMap,Map<String, String> proMap2){
    	for(String key:proMap.keySet()){
    		proMap.put(key, replace(proMap.get(key), proMap2));
    	}
    	for(String key:proMap2.keySet()){
    		proMap2.put(key, replace(proMap2.get(key), proMap));
    	}
    }
    
    /**
     * 对map中的value进行{}占位符或环境变量占位符的替换
     * @param proMap
     * @return
     */
    public static Map<String, String> replace (Map<String, String> proMap){
        Map<String, String> tempMap = new HashMap<String, String>();
        for (String key:proMap.keySet() ) {
            tempMap.put(key, replace(proMap.get(key), proMap));
        }
        return tempMap;
    }
    
    /**
     * 替换字符串中的变量占位符
     * @param value
     * @param proMap
     * @return
     */
    public static String replace(String value, Map<String, String> proMap) {
        Pattern pattern = Pattern.compile("(\\{[^\\}]*\\})");
        Matcher matcher = pattern.matcher(value);
        int curpos = 0;
        StringBuilder buf = new StringBuilder();
        while (matcher.find()) {
            buf.append(value.substring(curpos, matcher.start()));
            curpos = matcher.end();
            String var = value.substring(matcher.start(), curpos);
            String key = StringUtils.substring(var, 1, var.length() - 1);
            if (proMap.containsKey(key)) { // 如果是存在于变量列表中
                buf.append(proMap.get(key));
            } else if (!StringUtils.isBlank(key) && key.startsWith(TinyConfigConstants.TINY_ENV_PRE)) {
                String envKey = key.substring(TinyConfigConstants.TINY_ENV_PRE.length());
                if (!VariableUtil.hasEnvVariable(envKey)) {
                	//TODO:这里统一，要么都只打警告，那么都抛出异常
                    throw new RuntimeException("not found environment variables,key:" + envKey);
                } else {
                    buf.append(VariableUtil.getEnvVariable(envKey));
                }

            } else {
            	//TODO:这里统一，要么都只打警告，那么都抛出异常
            	LOGGER.warn("not found var:" + var);
                buf.append(var);
            }
            continue;
        }
        buf.append(value.substring(curpos));
        return buf.toString();
    }
}
