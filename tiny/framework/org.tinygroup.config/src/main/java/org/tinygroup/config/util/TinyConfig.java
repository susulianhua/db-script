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
package org.tinygroup.config.util;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.config.ConfigurationManager;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class TinyConfig {
    private final static Logger LOGGER = LoggerFactory.getLogger(TinyConfig.class);

    /**
     * 获取对应key的对应的配置值，若不存在或者不符合规则，则返回defaultValue
     * @param key 参数key值
     * @param defaultValue 若取不到该配置值，则采用此值
     * @param allowBlank 是否允许取取值为空串或者null。
     * 若为true，则只有在key不存在时才会返回defaultValue,其他情况军返回key对应取值。
     * 若为false，则对应key的取值为空串或者null时，返回defaultValue，其他情况均返回key对应取值。
     * @return
     */
    public static String getString(String key, String defaultValue, boolean allowBlank) {
        if (!contain(key)) {
            return defaultValue;
        }
        String value = getValue(key);
        if (StringUtil.isBlank(value)) {
            if (allowBlank) {
                return value;
            } else {
                return defaultValue;
            }
        } else {
            return value;
        }
    }

    /**
     * @param key 对应的配置项key
     * @param defaultValue 配置项默认取值
     * @param notallowParseException 是否抛出数据类型转换过程中产生的异常
     * 若为true，当将配置值转换为Boolean时发生异常，则将该异常抛出
     * 若未false，则该异常不抛出，并返回defaultValue
     * @return
     */
    public static Boolean getBoolean(String key, Boolean defaultValue, boolean notallowParseException) {
        String value = getValue(key);
        if (StringUtil.isBlank(value)) {
            return defaultValue;
        } else if (notallowParseException) {
            return Boolean.valueOf(value);
        }
        try {
            return Boolean.valueOf(value);
        } catch (Exception e) {
            LOGGER.warnMessage("转换配置参数:{}为Boolean时出错,值:{}", key, value);
            return defaultValue;
        }
    }

    /**
     * @param key 对应的配置项key
     * @param defaultValue 配置项默认取值
     * @param notallowParseException 是否抛出数据类型转换过程中产生的异常
     * 若为true，当将配置值转换为Int时发生异常，则将该异常抛出
     * 若未false，则该异常不抛出，并返回defaultValue
     * @return
     */
    public static int getInt(String key, int defaultValue, boolean notallowParseException) {
        String value = getValue(key);
        if (StringUtil.isBlank(value)) {
            return defaultValue;
        } else if (notallowParseException) {
            return Integer.parseInt(value);
        }
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            LOGGER.warnMessage("转换配置参数:{}为Int时出错,值:{}", key, value);
            return defaultValue;
        }
    }

    /**
     * @param key 对应的配置项key
     * @param defaultValue 配置项默认取值
     * @param notallowParseException 是否抛出数据类型转换过程中产生的异常
     * 若为true，当将配置值转换为Integer时发生异常，则将该异常抛出
     * 若未false，则该异常不抛出，并返回defaultValue
     * @return
     */
    public static Integer getInteger(String key, Integer defaultValue, boolean notallowParseException) {
        String value = getValue(key);
        if (StringUtil.isBlank(value)) {
            return defaultValue;
        } else if (notallowParseException) {
            return Integer.valueOf(value);
        }
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
            LOGGER.warnMessage("转换配置参数:{}为Integer时出错,值:{}", key, value);
            return defaultValue;
        }
    }

    public static String getValue(String key) {
        ConfigurationManager manager = ConfigurationUtil.getConfigurationManager();
        return manager.getConfiguration(key);
    }

    public static boolean contain(String key) {
        ConfigurationManager manager = ConfigurationUtil.getConfigurationManager();
        return manager.getConfiguration().containsKey(key);
    }

    public static void setValue(String key, String value) {
        ConfigurationManager manager = ConfigurationUtil.getConfigurationManager();
        manager.setConfiguration(key, value);
    }
}
