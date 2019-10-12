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
package com.xquant.metadata.defaultvalue.impl;


import com.xquant.metadata.config.defaultvalue.DefaultValue;
import com.xquant.metadata.config.defaultvalue.DefaultValues;
import com.xquant.metadata.config.stddatatype.DialectType;
import com.xquant.metadata.defaultvalue.DefaultValueProcessor;
import com.xquant.metadata.exception.MetadataRuntimeException;
import com.xquant.metadata.util.ConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.xquant.metadata.exception.MetadataErrorCode.DEFAULTVALUE_ADD_ALREADY_ERROR;
import static com.xquant.metadata.exception.MetadataErrorCode.DEFAULTVALUE_NOT_EXISTS_ERROR;

/**
 * Created by wangwy11342 on 2016/7/16.
 */
public class DefaultValueProcessorImpl implements DefaultValueProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultValueProcessorImpl.class);
    private static DefaultValueProcessor defaultValueProcessor = new DefaultValueProcessorImpl();
    private Map<String, DefaultValue> defaultValueMap = new HashMap<String, DefaultValue>();

    public static DefaultValueProcessor getDefaultValueProcessor() {
        return defaultValueProcessor;
    }

    public String getValue(String id, String language) {
        DefaultValue defaultValue = defaultValueMap.get(id);
        if (defaultValue == null) {
            throw new MetadataRuntimeException(DEFAULTVALUE_NOT_EXISTS_ERROR, id, language);
        }
        if (defaultValue.getDialectTypeList() != null) {
            for (DialectType dialectType : defaultValue.getDialectTypeList()) {
                if (dialectType.getLanguage().equals(language)) {
                    return dialectType.getDefaultValue();
                }
            }
        }
        throw new RuntimeException(String.format(
                "不存在, 默认值ID:[%s],语言:[%s]对应的默认值对象", id, language));
    }

    public void addDefaultValues(DefaultValues defaultValues) {
        if (defaultValues != null
                && defaultValues.getDefaultValueList() != null) {
            for (DefaultValue defaultValue : defaultValues
                    .getDefaultValueList()) {
                if (defaultValueMap.containsKey(defaultValue.getId())) {
                    if (ConfigUtil.isCheckStrict()) {
                        //重复id
                        throw new MetadataRuntimeException(DEFAULTVALUE_ADD_ALREADY_ERROR, defaultValue.getName(), defaultValue.getId());
                    } else {
                        LOGGER.error((new MetadataRuntimeException(DEFAULTVALUE_ADD_ALREADY_ERROR, defaultValue.getName(), defaultValue.getId())).toString());
                    }
                }
                defaultValueMap.put(defaultValue.getId(), defaultValue);
            }
        }
    }

    public DefaultValue getDefaultValue(String id) {
        if (defaultValueMap.containsKey(id))
            return defaultValueMap.get(id);
        throw new RuntimeException(String.format("不存在ID:[%s]对应的默认值 ", id));
    }

    public Map<String, DefaultValue> getDefaultValueMap() {
        return defaultValueMap;
    }

    public void removeDefaultValues(DefaultValues defaultValues) {
        if (defaultValues != null
                && defaultValues.getDefaultValueList() != null) {
            for (DefaultValue defaultValue : defaultValues
                    .getDefaultValueList()) {
                defaultValueMap.remove(defaultValue.getId());
            }
        }
    }
}
